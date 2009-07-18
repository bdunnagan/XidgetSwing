/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import org.xidget.IXidget;
import org.xidget.Log;
import org.xidget.config.util.Pair;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.layout.AnchorNode;
import org.xidget.layout.IComputeNode;
import org.xidget.swing.layout.AnchorLayoutManager;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of IWidgetCreationFeature which creates a Swing JFrame for the application.
 */
public class JPanelWidgetCreationFeature implements IWidgetCreationFeature
{
  public JPanelWidgetCreationFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#createWidget()
   */
  public void createWidgets()
  {
    insidePanel = new JPanel( new AnchorLayoutManager( xidget));
    insidePanel.addComponentListener( componentListener);
    insidePanel.addMouseListener( mouseListener);
    insidePanel.addMouseMotionListener( mouseListener);

    if ( System.getProperty( "debug") != null)
    {
      c += 16; if ( c > 255) c = 64;
      int d = Xlate.get( xidget.getConfig(), "background", c);
      insidePanel.setBackground( new Color( d, d, d));
    }
    
    // outside is same as inside by default
    outsidePanel = insidePanel;
    
    // create titled border if necessary (but not for tab entries)
    IXidget parent = xidget.getParent();
    String title = getTitle();
    if ( title != null && title.length() > 0 && parent != null)
    {
      outsidePanel = new JPanel( new BorderLayout());
      outsidePanel.setBorder( new TitledBorder( title));
      outsidePanel.add( insidePanel);
    }

    // add outside panel to parent container
    IWidgetContainerFeature containerFeature = xidget.getParent().getFeature( IWidgetContainerFeature.class);
    if ( containerFeature != null) containerFeature.addWidget( xidget);
    
    // optionally set the width and height nodes in case children are dependent on them
    IModelObject config = xidget.getConfig();
    Pair size = new Pair( Xlate.get( config, "size", (String)null), -1, -1);
    if ( size.x >= 0 || size.y >= 0) outsidePanel.setSize( size.x, size.y); 
  }
  
  /**
   * Returns the title of the form.
   * @return Returns null or the title of the form.
   */
  private String getTitle()
  {
    IModelObject element = xidget.getConfig();
    IExpression titleExpr = Xlate.childGet( element, "title", Xlate.get( element, "title", (IExpression)null));
    if ( titleExpr != null) return titleExpr.evaluateString();
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidgets()
  {
    Container container = outsidePanel.getParent();
    container.remove( outsidePanel);
    container.invalidate();
    outsidePanel = null;
    insidePanel = null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { outsidePanel, insidePanel};
  }

  /**
   * Returns the outside of the container.
   * @return Returns the outside of the container.
   */
  public JPanel getOutsidePanel()
  {
    return outsidePanel;
  }
  
  /**
   * Returns the inside of the container. The inside will be different from the outside
   * if the container has a title, since containers must have zero insets for the layout
   * algorithm to work.
   * @return Returns the inside of the container.
   */
  public JPanel getInsidePanel()
  {
    return insidePanel;
  }
  
  /**
   * Returns the IComputeNode under the specified mouse position.
   * @param x The x of the mouse.
   * @param y The y of the mouse.
   * @return Returns the IComputeNode under the specified mouse position.
   */
  private AnchorNode mouseGrab( int x, int y)
  {
    ILayoutFeature feature = xidget.getFeature( ILayoutFeature.class);
    
    List<IComputeNode> nodes = feature.getAllNodes();
    if ( nodes == null) return null;

    for( IComputeNode node: nodes)
    {
      if ( !(node instanceof IComputeNode)) continue;
        
      if ( node.hasXHandle())
      {
        float nx = node.getValue();
        if ( Math.abs( nx - x) < 4) 
        {
          outsidePanel.setCursor( Cursor.getPredefinedCursor( Cursor.E_RESIZE_CURSOR));
          return (AnchorNode)node;
        }
      }
      else if ( node.hasYHandle())
      {
        float ny = node.getValue();
        if ( Math.abs( ny - y) < 4)
        {
          outsidePanel.setCursor( Cursor.getPredefinedCursor( Cursor.N_RESIZE_CURSOR));
          return (AnchorNode)node;
        }
      }
    }
    
    outsidePanel.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR));
    return null;
  }
  
  private ComponentListener componentListener = new ComponentAdapter() {
    public void componentMoved( ComponentEvent e)
    {
      Point point = outsidePanel.getLocation();
      Log.printf( "layout", "MOVED: %s (%d, %d)\n", xidget, point.x, point.y);
    }
    public void componentResized( ComponentEvent e)
    {
      Log.printf( "layout", "RESIZE: %s (%d, %d)\n", xidget, outsidePanel.getWidth(), outsidePanel.getHeight());
    }
  };
  
  private MouseInputListener mouseListener = new MouseInputAdapter() {
    public void mousePressed( MouseEvent e)
    {
      if ( e.getButton() == MouseEvent.BUTTON1)
      {
        grabbed = mouseGrab( e.getX(), e.getY());
        Log.printf( "layout", "grab: (%d, %d) %s\n", e.getX(), e.getY(), grabbed);
      }
    }
    public void mouseReleased( MouseEvent e)
    {
      grabbed = null;
    }
    public void mouseExited( MouseEvent e)
    {
      if ( grabbed != null) outsidePanel.grabFocus();
      else outsidePanel.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR));
    }
    public void mouseDragged( MouseEvent e)
    {
      if ( grabbed != null)
      {
        Rectangle bounds = outsidePanel.getBounds();
        if ( grabbed.hasXHandle())
        {
          float px = (float)e.getX() / bounds.width;
          grabbed.setFraction( px);
        }
        else
        {
          float py = (float)e.getY() / bounds.height;
          grabbed.setFraction( py);
        }
        
        outsidePanel.revalidate();
      }
    }
    public void mouseMoved( MouseEvent e)
    {
      mouseGrab( e.getX(), e.getY());
    }
  };

  private static int c = 64;
  
  private IXidget xidget;
  private JPanel outsidePanel;
  private JPanel insidePanel;
  private AnchorNode grabbed;
}
