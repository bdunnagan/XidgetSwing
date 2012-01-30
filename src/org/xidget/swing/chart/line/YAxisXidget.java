/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.line;

import java.awt.Component;

import javax.swing.JComponent;

import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ITextWidgetFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.chart.IAxisFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingTextWidgetFeature;
import org.xidget.swing.feature.SwingWidgetFeature;

/**
 * A horizontal or vertical axis xidget.
 */
public class YAxisXidget extends Xidget
{
  /* (non-Javadoc)
   * @see org.xidget.Xidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    bindFeature = new BindFeature( this);
    axisFeature = new AxisFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    textFeature = new SwingTextWidgetFeature( this);
    creationFeature = new YAxisWidgetCreationFeature( this);
    basicFeatureSet = new BasicFeatureSet( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == ITextWidgetFeature.class) return (T)textFeature;
    if ( clss == IAxisFeature.class) return (T)axisFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IBindFeature.class) return (T)bindFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getComponent();
    if ( clss == JComponent.class) return (T)creationFeature.getComponent();
    if ( clss == Axis.class) return (T)creationFeature.getAxis();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }

  private IBindFeature bindFeature;
  private IAxisFeature axisFeature;
  private IWidgetFeature widgetFeature;
  private ITextWidgetFeature textFeature;
  private YAxisWidgetCreationFeature creationFeature;
  private IFeatured basicFeatureSet;
}
