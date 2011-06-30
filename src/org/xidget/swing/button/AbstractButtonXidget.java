/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * AbstractButtonXidget.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.swing.button;

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;

import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.model.SingleValueModelFeature;
import org.xidget.feature.model.SingleValueUpdateFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IIconFeature;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.model.ISingleValueModelFeature;
import org.xidget.ifeature.model.ISingleValueUpdateFeature;
import org.xidget.ifeature.model.ISingleValueWidgetFeature;
import org.xidget.swing.feature.AbstractButtonIconFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingWidgetFeature;

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
    singleValueModelFeature = new SingleValueModelFeature( this);    
    singleValueUpdateFeature = new SingleValueUpdateFeature( this);    
    singleValueWidgetFeature = new AbstractButtonSingleValueWidgetFeature( this);
    iconFeature = new AbstractButtonIconFeature( this);
    labelFeature = new ButtonLabelFeature( this);
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
    if ( clss == IIconFeature.class) return (T)iconFeature;
    if ( clss == ILabelFeature.class) return (T)labelFeature;
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == ISingleValueModelFeature.class) return (T)singleValueModelFeature;
    if ( clss == ISingleValueUpdateFeature.class) return (T)singleValueUpdateFeature;
    if ( clss == ISingleValueWidgetFeature.class) return (T)singleValueWidgetFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IBindFeature.class) return (T)bindFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getAbstractButton();
    if ( clss == JComponent.class) return (T)creationFeature.getAbstractButton();
    if ( clss == AbstractButton.class) return (T)creationFeature.getAbstractButton();
    if ( clss == ButtonGroup.class) return (T)creationFeature.getButtonGroup();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }

  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private ISingleValueModelFeature singleValueModelFeature;
  private ISingleValueUpdateFeature singleValueUpdateFeature;
  private ISingleValueWidgetFeature singleValueWidgetFeature;
  private IIconFeature iconFeature;
  private ILabelFeature labelFeature;
  private AbstractButtonWidgetCreationFeature creationFeature;  
  private IFeatured basicFeatureSet;
}
