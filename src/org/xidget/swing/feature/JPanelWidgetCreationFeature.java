/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
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
import org.xidget.layout.IComputeNode;
import org.xidget.layout.IComputeNode.Grab;
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
    c += 16; if ( c > 255) c = 64;
    int d = Xlate.get( xidget.getConfig(), "background", c);
    
    jPanel = new JPanel( new AnchorLayoutManager( xidget));
    jPanel.setBackground( new Color( d, d, d));
    jPanel.addComponentListener( componentListener);
    jPanel.addMouseListener( mouseListener);
    jPanel.addMouseMotionListener( mouseListener);
    
    IWidgetContainerFeature containerFeature = xidget.getParent().getFeature( IWidgetContainerFeature.class);
    if ( containerFeature != null) containerFeature.addWidget( xidget);
    
    // create titled border if necessary (but not for tab entries)
    IXidget parent = xidget.getParent();
    String title = getTitle();
    if ( title != null && title.length() > 0 && parent != null)
    {
      jPanel.setBorder( new TitledBorder( title));
    }

    // optionally set the width and height nodes in case children are dependent on them
    IModelObject config = xidget.getConfig();
    Pair size = new Pair( Xlate.get( config, "size", Xlate.childGet( config, "size", "")), 0, 0);
    if ( size.x > 0 || size.y > 0)
    {
      jPanel.setPreferredSize( new Dimension( size.x, size.y));
    }
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
    Container container = jPanel.getParent();
    container.remove( jPanel);
    container.invalidate();
    jPanel = null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jPanel};
  }

  /**
   * Returns the JPanel created for this form.
   * @return Returns the JPanel created for this form.
   */
  public JPanel getJPanel()
  {
    return jPanel;
  }
  
  /**
   * Returns the IComputeNode under the specified mouse position.
   * @param x The x of the mouse.
   * @param y The y of the mouse.
   * @return Returns the IComputeNode under the specified mouse position.
   */
  private IComputeNode mouseGrab( int x, int y)
  {
    ILayoutFeature feature = xidget.getFeature( ILayoutFeature.class);
    List<IComputeNode> nodes = feature.getAllNodes();
      
    if ( nodes == null) return null;
    
    for( IComputeNode node: nodes)
    {
      Grab grab = node.mouseGrab( x, y);
      switch( grab)
      {
        case none: jPanel.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR)); break;
        case x: jPanel.setCursor( Cursor.getPredefinedCursor( Cursor.E_RESIZE_CURSOR)); return node;
        case y: jPanel.setCursor( Cursor.getPredefinedCursor( Cursor.N_RESIZE_CURSOR)); return node;
      }
    }
    
    return null;
  }
  
  private ComponentListener componentListener = new ComponentAdapter() {
    public void componentMoved( ComponentEvent e)
    {
      Point point = jPanel.getLocation();
      Log.printf( "layout", "MOVED: %s (%d, %d)\n", xidget, point.x, point.y);
    }
    public void componentResized( ComponentEvent e)
    {
      Log.printf( "layout", "RESIZE: %s (%d, %d)\n", xidget, jPanel.getWidth(), jPanel.getHeight());
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
      if ( grabbed != null) jPanel.grabFocus();
      else jPanel.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR));
    }
    public void mouseDragged( MouseEvent e)
    {
      if ( grabbed != null)
      {
        Rectangle bounds = jPanel.getBounds();
        float px = (float)e.getX() / bounds.width;
        float py = (float)e.getY() / bounds.height;
        grabbed.move( px, py);
        jPanel.revalidate();
      }
    }
    public void mouseMoved( MouseEvent e)
    {
      mouseGrab( e.getX(), e.getY());
    }
  };

  private static int c = 64;
  
  private IXidget xidget;
  private JPanel jPanel;
  private IComputeNode grabbed;
}
