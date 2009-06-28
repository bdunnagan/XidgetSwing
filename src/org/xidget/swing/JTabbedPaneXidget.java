/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.ComputeNodeFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.JTabbedPaneContainerFeature;
import org.xidget.swing.feature.JTabbedPaneWidgetCreationFeature;
import org.xidget.swing.feature.SwingWidgetFeature;

/**
 * A form xidget implemented with the Swing JPanel widget.
 */
public class JTabbedPaneXidget extends Xidget
{
  public void createFeatures()
  {
    bindFeature = new BindFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    computeNodeFeature = new ComputeNodeFeature( this);
    creationFeature = new JTabbedPaneWidgetCreationFeature( this);
    containerFeature = new JTabbedPaneContainerFeature( this);
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
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == IComputeNodeFeature.class) return (T)computeNodeFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IWidgetContainerFeature.class) return (T)containerFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getJTabbedPane();
    if ( clss == JComponent.class) return (T)creationFeature.getJTabbedPane();
    if ( clss == Container.class) return (T)creationFeature.getJTabbedPane();
    if ( clss == JTabbedPane.class) return (T)creationFeature.getJTabbedPane();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private IComputeNodeFeature computeNodeFeature;
  private JTabbedPaneWidgetCreationFeature creationFeature;
  private IWidgetContainerFeature containerFeature;
  private IFeatured basicFeatureSet;
}
