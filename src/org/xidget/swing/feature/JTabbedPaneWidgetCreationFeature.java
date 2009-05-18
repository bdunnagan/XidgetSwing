/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import javax.swing.JTabbedPane;
import org.xidget.IXidget;
import org.xidget.config.util.Pair;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.layout.ConstantNode;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetCreationFeature which creates a Swing JFrame for the application.
 */
public class JTabbedPaneWidgetCreationFeature implements IWidgetCreationFeature
{
  public JTabbedPaneWidgetCreationFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#createWidget()
   */
  public void createWidgets()
  {
    jtabbedPane = new JTabbedPane();
    
    IWidgetContainerFeature containerFeature = xidget.getParent().getFeature( IWidgetContainerFeature.class);
    if ( containerFeature != null) containerFeature.addWidget( xidget);
        
    // optionally constrain size
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    IModelObject config = xidget.getConfig();
    Pair size = new Pair( Xlate.get( config, "size", Xlate.childGet( config, "size", "")), 0, 0);
    if ( size.x > 0 || size.y > 0)
    {
      computeNodeFeature.getAnchor( "w").addDependency( new ConstantNode( size.x));
      computeNodeFeature.getAnchor( "h").addDependency( new ConstantNode( size.y));
    }    
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidgets()
  {
    jtabbedPane.getParent().remove( jtabbedPane);
    jtabbedPane = null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jtabbedPane};
  }

  /**
   * Returns the JTabbedPane created for this form.
   * @return Returns the JTabbedPane created for this form.
   */
  public JTabbedPane getJTabbedPane()
  {
    return jtabbedPane;
  }

  private IXidget xidget;
  private JTabbedPane jtabbedPane;
}
