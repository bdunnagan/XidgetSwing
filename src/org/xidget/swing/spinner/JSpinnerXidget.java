/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.spinner;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.text.TextModelFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.ISourceFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.combo.IChoiceListFeature;
import org.xidget.ifeature.slider.ISliderWidgetFeature;
import org.xidget.ifeature.text.ITextModelFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;
import org.xidget.swing.combo.JComboBoxTextWidgetFeature;
import org.xidget.swing.combo.JComboBoxWidgetCreationFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingWidgetFeature;

/**
 * An implementation of a spinner based on the JSpinner widget.
 */
public class JSpinnerXidget extends Xidget
{
  public void createFeatures()
  {
    bindFeature = new BindFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    textModelFeature = new TextModelFeature( this);
    textWidgetFeature = new JComboBoxTextWidgetFeature( this);
    sliderFeature = new JSpinnerWidgetFeature( this);
    choiceListFeature = new JSpinnerChoiceListFeature( this);
    creationFeature = new JComboBoxWidgetCreationFeature( this);
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
    if ( clss == ISourceFeature.class) return (T)textModelFeature;
    if ( clss == ITextModelFeature.class) return (T)textModelFeature;
    if ( clss == ITextWidgetFeature.class) return (T)textWidgetFeature;
    if ( clss == ISliderWidgetFeature.class) return (T)sliderFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IChoiceListFeature.class) return (T)choiceListFeature;
    if ( clss == ILabelFeature.class) return (T)creationFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getContainer();
    if ( clss == JComponent.class) return (T)creationFeature.getContainer();
    if ( clss == JComboBox.class) return (T)creationFeature.getComboBox();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private ITextModelFeature textModelFeature;
  private ITextWidgetFeature textWidgetFeature;
  private ISliderWidgetFeature sliderFeature;
  private JSpinnerChoiceListFeature choiceListFeature;
  private JComboBoxWidgetCreationFeature creationFeature;
  private IFeatured basicFeatureSet;
}
