/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Container;
import javax.swing.JPanel;
import org.xidget.IXidget;
import org.xidget.config.util.Pair;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.layout.ConstantNode;
import org.xidget.swing.layout.AnchorLayoutManager;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

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
  public void createWidget()
  {
    Container container = xidget.getParent().getFeature( Container.class);
    
    jpanel = new JPanel( new AnchorLayoutManager( xidget));
    container.add( jpanel);
    
    // upper-left corner is always (0, 0)
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    computeNodeFeature.getAnchor( "x0").addDependency( new ConstantNode( 0));
    computeNodeFeature.getAnchor( "y0").addDependency( new ConstantNode( 0));    
    
    // lower-right corner is set if size is defined
    IModelObject config = xidget.getConfig();
    Pair size = new Pair( Xlate.get( config, "size", Xlate.childGet( config, "size", "")), 0, 0);
    if ( size.x > 0 || size.y > 0)
    {
      computeNodeFeature.getAnchor( "x1").addDependency( new ConstantNode( size.x));
      computeNodeFeature.getAnchor( "y1").addDependency( new ConstantNode( size.y));
    }    
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidget()
  {
    jpanel.getParent().remove( jpanel);
    jpanel = null;
  }

  /**
   * Returns the JPanel created for this form.
   * @return Returns the JPanel created for this form.
   */
  public JPanel getWidget()
  {
    return jpanel;
  }

  private IXidget xidget;
  private JPanel jpanel;
}
