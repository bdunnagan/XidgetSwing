/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.button;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.ComputeNodeFeature;
import org.xidget.feature.button.ButtonModelFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.button.IButtonModelFeature;
import org.xidget.ifeature.button.IButtonWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingWidgetFeature;
import org.xidget.swing.feature.button.AbstractButtonWidgetCreationFeature;
import org.xidget.swing.feature.button.ButtonWidgetFeature;

/**
 * A xidget implementation for Swing AbstactButton widgets.
 */
public class AbstractButtonXidget extends Xidget
{
  /* (non-Javadoc)
   * @see org.xidget.Xidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    bindFeature = new BindFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    buttonWidgetFeature = new ButtonWidgetFeature( this);
    buttonModelFeature = new ButtonModelFeature();
    computeNodeFeature = new ComputeNodeFeature( this);
    creationFeature = new AbstractButtonWidgetCreationFeature( this);
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
    if ( clss == IButtonWidgetFeature.class) return (T)buttonWidgetFeature;
    if ( clss == IButtonModelFeature.class) return (T)buttonModelFeature;
    if ( clss == IComputeNodeFeature.class) return (T)computeNodeFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IBindFeature.class) return (T)bindFeature;
    
    if ( clss == AbstractButton.class) return (T)creationFeature.getAbstractButton();
    if ( clss == ButtonGroup.class) return (T)creationFeature.getButtonGroup();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }

  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private IButtonWidgetFeature buttonWidgetFeature;
  private IButtonModelFeature buttonModelFeature;
  private IComputeNodeFeature computeNodeFeature;
  private AbstractButtonWidgetCreationFeature creationFeature;  
  private IFeatured basicFeatureSet;
}
