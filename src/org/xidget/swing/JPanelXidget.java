/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.AnchorLayoutFeature;
import org.xidget.feature.BindFeature;
import org.xidget.feature.ComputeNodeFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IIconFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.ITitleFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.GenericContainerFeature;
import org.xidget.swing.feature.JPanelWidgetCreationFeature;
import org.xidget.swing.feature.SwingIconFeature;
import org.xidget.swing.feature.SwingTitleFeature;
import org.xidget.swing.feature.SwingWidgetFeature;

/**
 * A form xidget implemented with the Swing JPanel widget.
 */
public class JPanelXidget extends Xidget
{
  public void createFeatures()
  {
    bindFeature = new BindFeature( this);
    layoutFeature = new AnchorLayoutFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    titleFeature = new SwingTitleFeature( this);
    iconFeature = new SwingIconFeature( this);
    computeNodeFeature = new ComputeNodeFeature( this);
    creationFeature = new JPanelWidgetCreationFeature( this);
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
    if ( clss == ITitleFeature.class) return (T)titleFeature;
    if ( clss == IIconFeature.class) return (T)iconFeature;
    if ( clss == IComputeNodeFeature.class) return (T)computeNodeFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IWidgetContainerFeature.class) return (T)containerFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getJPanel();
    if ( clss == JComponent.class) return (T)creationFeature.getJPanel();
    if ( clss == JPanel.class) return (T)creationFeature.getJPanel();
    if ( clss == Container.class) return (T)creationFeature.getJPanel();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private ILayoutFeature layoutFeature;
  private ITitleFeature titleFeature;
  private IIconFeature iconFeature;
  private IComputeNodeFeature computeNodeFeature;
  private JPanelWidgetCreationFeature creationFeature;
  private IWidgetContainerFeature containerFeature;
  private IFeatured basicFeatureSet;
}
