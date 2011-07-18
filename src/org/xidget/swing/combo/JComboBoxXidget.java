/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JComboBoxXidget.java
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
package org.xidget.swing.combo;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.model.MultiValueModelFeature;
import org.xidget.feature.model.MultiValueUpdateFeature;
import org.xidget.feature.model.SelectionModelFeature;
import org.xidget.feature.model.SelectionUpdateFeature;
import org.xidget.feature.model.SingleValueModelFeature;
import org.xidget.feature.model.SingleValueUpdateFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.model.IMultiValueModelFeature;
import org.xidget.ifeature.model.IMultiValueUpdateFeature;
import org.xidget.ifeature.model.IMultiValueWidgetFeature;
import org.xidget.ifeature.model.ISelectionModelFeature;
import org.xidget.ifeature.model.ISelectionUpdateFeature;
import org.xidget.ifeature.model.ISelectionWidgetFeature;
import org.xidget.ifeature.model.ISingleValueModelFeature;
import org.xidget.ifeature.model.ISingleValueUpdateFeature;
import org.xidget.ifeature.model.ISingleValueWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;

/**
 * A combo xidget implemented with the Swing JComboBox widget.
 */
public class JComboBoxXidget extends Xidget
{
  public void createFeatures()
  {
    bindFeature = new BindFeature( this);
    widgetFeature = new JComboBoxWidgetFeature( this);
    creationFeature = new JComboBoxWidgetCreationFeature( this);
    multiValueModelFeature = new MultiValueModelFeature( this);    
    multiValueUpdateFeature = new MultiValueUpdateFeature( this);
    multiValueWidgetFeature = new JComboBoxMultiValueWidgetFeature( this);
    singleValueModelFeature = new SingleValueModelFeature( this);    
    singleValueUpdateFeature = new SingleValueUpdateFeature( this);    
    singleValueWidgetFeature = new JComboBoxSingleValueWidgetFeature( this);    
    selectionModelFeature = new SelectionModelFeature( this);
    selectionUpdateFeature = new SelectionUpdateFeature( this);
    selectionWidgetFeature = new JComboBoxSelectionWidgetFeature( this);
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
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == ILabelFeature.class) return (T)creationFeature;
    if ( clss == IMultiValueModelFeature.class) return (T)multiValueModelFeature;
    if ( clss == IMultiValueUpdateFeature.class) return (T)multiValueUpdateFeature;
    if ( clss == IMultiValueWidgetFeature.class) return (T)multiValueWidgetFeature;
    if ( clss == ISingleValueModelFeature.class) return (T)singleValueModelFeature;
    if ( clss == ISingleValueUpdateFeature.class) return (T)singleValueUpdateFeature;
    if ( clss == ISingleValueWidgetFeature.class) return (T)singleValueWidgetFeature;
    if ( clss == ISelectionModelFeature.class) return (T)selectionModelFeature;
    if ( clss == ISelectionUpdateFeature.class) return (T)selectionUpdateFeature;
    if ( clss == ISelectionWidgetFeature.class) return (T)selectionWidgetFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getContainer();
    if ( clss == JComponent.class) return (T)creationFeature.getContainer();
    if ( clss == JComboBox.class) return (T)creationFeature.getComboBox();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private SingleValueModelFeature singleValueModelFeature;
  private SingleValueUpdateFeature singleValueUpdateFeature;
  private MultiValueModelFeature multiValueModelFeature;
  private MultiValueUpdateFeature multiValueUpdateFeature;
  private IMultiValueWidgetFeature multiValueWidgetFeature;
  private JComboBoxSingleValueWidgetFeature singleValueWidgetFeature;
  private JComboBoxWidgetCreationFeature creationFeature;
  private ISelectionModelFeature selectionModelFeature;
  private ISelectionWidgetFeature selectionWidgetFeature;
  private ISelectionUpdateFeature selectionUpdateFeature;
  private IFeatured basicFeatureSet;
}
