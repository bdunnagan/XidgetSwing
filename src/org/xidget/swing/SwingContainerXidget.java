/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import java.awt.Container;
import javax.swing.JPanel;
import org.xidget.AbstractXidget;
import org.xidget.config.util.Pair;
import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetCreationFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.layout.AnchorLayoutFeature;
import org.xidget.layout.ConstantNode;
import org.xidget.layout.IComputeNodeFeature;
import org.xidget.layout.ILayoutFeature;
import org.xidget.swing.feature.SwingTooltipErrorFeature;
import org.xidget.swing.layout.AnchorLayoutManager;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IXidget that serves as the Swing/AWT implementation of a form.
 */
public class SwingContainerXidget extends AbstractXidget implements IWidgetCreationFeature, ISwingContainerFeature
{  
  public SwingContainerXidget()
  {
    // TODO: need to codify the creation-time of ILayoutFeature
    layoutFeature = new AnchorLayoutFeature( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    super.createFeatures();
    errorFeature = new SwingTooltipErrorFeature( this);
  }

  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#configureFeatures()
   */
  @Override
  protected void configureFeatures()
  {
    super.configureFeatures();

    // upper-left corner is always (0, 0)
    IComputeNodeFeature computeNodeFeature = getFeature( IComputeNodeFeature.class);
    computeNodeFeature.getAnchor( "x0").addDependency( new ConstantNode( 0));
    computeNodeFeature.getAnchor( "y0").addDependency( new ConstantNode( 0));    
    
    // lower-right corner is set if size is defined
    IModelObject config = getConfig();
    Pair size = new Pair( Xlate.get( config, "size", Xlate.childGet( config, "size", "")), 0, 0);
    if ( size.x > 0 || size.y > 0)
    {
      computeNodeFeature.getAnchor( "x1").addDependency( new ConstantNode( size.x));
      computeNodeFeature.getAnchor( "y1").addDependency( new ConstantNode( size.y));
    }    
  }

  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#getWidgetCreationFeature()
   */
  @Override
  protected IWidgetCreationFeature getWidgetCreationFeature()
  {
    return this;
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#createWidget(java.lang.String, org.xmodel.IModelObject)
   */
  public void createWidget( String label, IModelObject element)
  {
    ISwingContainerFeature containerFeature = getParent().getFeature( ISwingContainerFeature.class);
    Container container = containerFeature.getContainer();
    
    ILayoutFeature layoutFeature = getFeature( ILayoutFeature.class);
    panel = new JPanel( new AnchorLayoutManager( layoutFeature));
    container.add( panel);
    
    widgetFeature = new SwingWidgetFeature( panel);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidget()
  {
    panel.getParent().remove( panel);
    panel = null;
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.ISwingContainerFeature#getContainer()
   */
  public Container getContainer()
  {
    return panel;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == ISwingContainerFeature.class) return (T)this;
    if ( clss == IErrorFeature.class) return (T)errorFeature;
    if ( clss == ISwingWidgetFeature.class) return (T)widgetFeature;
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == ILayoutFeature.class) return (T)layoutFeature;
        
    return super.getFeature( clss);
  }

  private JPanel panel;
  private ILayoutFeature layoutFeature;
  private SwingWidgetFeature widgetFeature;
  private SwingTooltipErrorFeature errorFeature; 
}
