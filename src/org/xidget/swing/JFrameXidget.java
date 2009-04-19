/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.xidget.Xidget;
import org.xidget.feature.AnchorLayoutFeature;
import org.xidget.feature.BindFeature;
import org.xidget.feature.ComputeNodeFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IErrorFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.swing.feature.JFrameWidgetCreationFeature;
import org.xidget.swing.feature.SwingWidgetFeature;
import org.xidget.swing.feature.TooltipErrorFeature;

/**
 * An application xidget implemented with the Swing JFrame widget.
 */
public class JFrameXidget extends Xidget
{
  public void createFeatures()
  {
    bindFeature = new BindFeature( this);
    errorFeature = new TooltipErrorFeature( this);
    layoutFeature = new AnchorLayoutFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    computeNodeFeature = new ComputeNodeFeature( this);
    creationFeature = new JFrameWidgetCreationFeature();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IBindFeature.class) return (T)bindFeature;
    if ( clss == IErrorFeature.class) return (T)errorFeature;
    if ( clss == ILayoutFeature.class) return (T)layoutFeature;
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == IComputeNodeFeature.class) return (T)computeNodeFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    
    if ( clss == JComponent.class) return (T)creationFeature.getFrame();
    if ( clss == Container.class) return (T)creationFeature.getFrame();
    if ( clss == JFrame.class) return (T)creationFeature.getFrame();
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private IErrorFeature errorFeature;
  private ILayoutFeature layoutFeature;
  private IComputeNodeFeature computeNodeFeature;
  private JFrameWidgetCreationFeature creationFeature;
}
