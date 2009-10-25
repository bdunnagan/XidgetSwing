/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.application;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.AnchorLayoutFeature;
import org.xidget.feature.BindFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.ITitleFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.GenericContainerFeature;

/**
 * An application xidget implemented with the Swing JFrame widget.
 */
public class JFrameXidget extends Xidget
{
  public void createFeatures()
  {
    bindFeature = new BindFeature( this);
    layoutFeature = new AnchorLayoutFeature( this);
    widgetFeature = new JFrameWidgetFeature( this);
    creationFeature = new JFrameWidgetCreationFeature( this);
    containerFeature = new GenericContainerFeature( this);
    basicFeatureSet = new BasicFeatureSet( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IBindFeature.class) return (T)bindFeature;
    if ( clss == ILayoutFeature.class) return (T)layoutFeature;
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == ITitleFeature.class) return (T)widgetFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IWidgetContainerFeature.class) return (T)containerFeature;
    
    if ( clss == JComponent.class) return (T)creationFeature.getFrame();
    if ( clss == Container.class) return (T)creationFeature.getFrame();
    if ( clss == JFrame.class) return (T)creationFeature.getFrame();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private ILayoutFeature layoutFeature;
  private JFrameWidgetCreationFeature creationFeature;
  private IWidgetContainerFeature containerFeature;
  private IFeatured basicFeatureSet;
}
