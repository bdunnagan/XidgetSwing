/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

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
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IComputeNodeFeature.Type;
import org.xidget.layout.ConstantNode;
import org.xidget.layout.DifferenceNode;
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
    System.out.printf( "%s -> %d\n", xidget, c);
    
    jpanel = new JPanel( new AnchorLayoutManager( xidget));
    jpanel.setBackground( colors[ c++]);
    jpanel.addComponentListener( componentListener);
    jpanel.addMouseListener( mouseListener);
    jpanel.addMouseMotionListener( mouseListener);
    
    IWidgetContainerFeature containerFeature = xidget.getParent().getFeature( IWidgetContainerFeature.class);
    if ( containerFeature != null) containerFeature.addWidget( xidget);
    
    // create titled border if necessary (but not for tab entries)
    IXidget parent = xidget.getParent();
    String title = getTitle();
    if ( title != null && title.length() > 0 && parent != null)
    {
      if ( parent.getConfig().isType( "form"))
        jpanel.setBorder( new TitledBorder( title));
    }

    // setup layout
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    IComputeNode top = computeNodeFeature.getAnchor( Type.top);
    IComputeNode left = computeNodeFeature.getAnchor( Type.left);
    IComputeNode right = computeNodeFeature.getAnchor( Type.right);
    IComputeNode bottom = computeNodeFeature.getAnchor( Type.bottom);
    IComputeNode width = computeNodeFeature.getAnchor( Type.width);
    IComputeNode height = computeNodeFeature.getAnchor( Type.height);

    // connect top and left to 0 by default
    top.addDependency( new ConstantNode( 0));
    left.addDependency( new ConstantNode( 0));
    
    // connect width and height to other anchors by default
    width.addDependency( new DifferenceNode( left, right));
    height.addDependency( new DifferenceNode( top, bottom));
    
    // constrain size if size attribute is specified
    IModelObject config = xidget.getConfig();
    Pair size = new Pair( Xlate.get( config, "size", Xlate.childGet( config, "size", "")), 0, 0);
    if ( size.x > 0 || size.y > 0)
    {
      if ( size.x > 0)
      {
        // set right side
        right.addDependency( new ConstantNode( size.x));
      }
      
      if ( size.y > 0)
      {
        // set bottom side
        bottom.addDependency( new ConstantNode( size.y));
      }
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
    Container container = jpanel.getParent();
    container.remove( jpanel);
    container.invalidate();
    jpanel = null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jpanel};
  }

  /**
   * Returns the JPanel created for this form.
   * @return Returns the JPanel created for this form.
   */
  public JPanel getJPanel()
  {
    return jpanel;
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
    List<IComputeNode> nodes = feature.getNodes();
      
    if ( nodes == null) return null;
    
    for( IComputeNode node: nodes)
    {
      Grab grab = node.mouseGrab( x, y);
      switch( grab)
      {
        case none: jpanel.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR)); break;
        case x: jpanel.setCursor( Cursor.getPredefinedCursor( Cursor.E_RESIZE_CURSOR)); return node;
        case y: jpanel.setCursor( Cursor.getPredefinedCursor( Cursor.N_RESIZE_CURSOR)); return node;
      }
    }
    
    return null;
  }
  
  private ComponentListener componentListener = new ComponentAdapter() {
    public void componentMoved( ComponentEvent e)
    {
      Point point = jpanel.getLocation();
      Log.printf( "layout", "MOVED: %s (%d, %d)\n", xidget, point.x, point.y);
    }
    public void componentResized( ComponentEvent e)
    {
      Log.printf( "layout", "RESIZE: %s (%d, %d)\n", xidget, jpanel.getWidth(), jpanel.getHeight());
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
      if ( grabbed != null) jpanel.grabFocus();
      else jpanel.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR));
    }
    public void mouseDragged( MouseEvent e)
    {
      if ( grabbed != null)
      {
        Rectangle bounds = jpanel.getBounds();
        float px = (float)e.getX() / bounds.width;
        float py = (float)e.getY() / bounds.height;
        grabbed.move( px, py);
        jpanel.revalidate();
      }
    }
    public void mouseMoved( MouseEvent e)
    {
      mouseGrab( e.getX(), e.getY());
    }
  };

  private static Color[] colors = new Color[] { Color.blue, Color.green, Color.red, Color.yellow, Color.orange};
  private static int c = 0;
  
  private IXidget xidget;
  private JPanel jpanel;
  private IComputeNode grabbed;
}
