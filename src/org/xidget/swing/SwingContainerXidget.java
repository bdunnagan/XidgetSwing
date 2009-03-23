/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import java.awt.Container;
import javax.swing.JPanel;
import org.xidget.AbstractXidget;
import org.xidget.IXidget;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.config.util.Pair;
import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.feature.IWidgetCreationFeature;
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
    layoutFeature = new AnchorLayoutFeature( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#startConfig(org.xidget.config.processor.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  @Override
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    super.startConfig( processor, parent, element);

    widgetFeature = new SwingWidgetFeature( panel);
    errorFeature = new SwingTooltipErrorFeature( panel);

    // upper-left corner is always (0, 0)
    IComputeNodeFeature computeNodeFeature = getFeature( IComputeNodeFeature.class);
    computeNodeFeature.getAnchor( "x0").addDependency( new ConstantNode( 0));
    computeNodeFeature.getAnchor( "y0").addDependency( new ConstantNode( 0));    
    
    // lower-right corner is set if size is defined
    Pair size = new Pair( Xlate.get( element, "size", Xlate.childGet( element, "size", "")), 0, 0);
    if ( size.x > 0 || size.y > 0)
    {
      computeNodeFeature.getAnchor( "x1").addDependency( new ConstantNode( size.x));
      computeNodeFeature.getAnchor( "y1").addDependency( new ConstantNode( size.y));
    }    
    
    return true;
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#createWidget(org.xidget.IXidget, java.lang.String, org.xmodel.IModelObject)
   */
  public void createWidget( IXidget xidget, String label, IModelObject element)
  {
    ISwingContainerFeature containerFeature = xidget.getParent().getFeature( ISwingContainerFeature.class);
    Container container = containerFeature.getContainer();
    
    panel = new JPanel( new AnchorLayoutManager( layoutFeature));
    container.add( panel);
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
    if ( clss.equals( ISwingContainerFeature.class)) return (T)this;
    if ( clss.equals( ISwingWidgetFeature.class)) return (T)widgetFeature;
    if ( clss.equals( IWidgetFeature.class)) return (T)widgetFeature;
    if ( clss.equals( IErrorFeature.class)) return (T)errorFeature;
    if ( clss.equals( ILayoutFeature.class)) return (T)layoutFeature;
    if ( clss.equals( IWidgetCreationFeature.class)) return (T)this;
    return super.getFeature( clss);
  }

  private JPanel panel;
  private ILayoutFeature layoutFeature;
  private SwingWidgetFeature widgetFeature;
  private SwingTooltipErrorFeature errorFeature; 
}
