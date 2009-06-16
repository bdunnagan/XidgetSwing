/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.xidget.IXidget;
import org.xidget.Log;
import org.xidget.config.util.Pair;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IComputeNodeFeature.Type;
import org.xidget.layout.ConstantNode;
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
    jpanel = new JPanel( new AnchorLayoutManager( xidget));
    jpanel.addComponentListener( componentListener);
    
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
    
    // optionally constrain size
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    IModelObject config = xidget.getConfig();
    Pair size = new Pair( Xlate.get( config, "size", Xlate.childGet( config, "size", "")), 0, 0);
    if ( size.x > 0 || size.y > 0)
    {
      computeNodeFeature.getAnchor( Type.width).addDependency( new ConstantNode( size.x));
      computeNodeFeature.getAnchor( Type.height).addDependency( new ConstantNode( size.y));
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
    jpanel.getParent().remove( jpanel);
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

  private IXidget xidget;
  private JPanel jpanel;
}
