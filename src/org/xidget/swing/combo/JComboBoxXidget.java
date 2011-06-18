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
import org.xidget.feature.SelectionModelFeature;
import org.xidget.feature.SourceFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IChoiceListFeature;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.ISelectionWidgetFeature;
import org.xidget.ifeature.ISourceFeature;
import org.xidget.ifeature.IValueFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingWidgetFeature;

/**
 * A combo xidget implemented with the Swing JComboBox widget.
 */
public class JComboBoxXidget extends Xidget
{
  public void createFeatures()
  {
    bindFeature = new BindFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    sourceFeature = new SourceFeature( this);
    textWidgetFeature = new JComboBoxTextWidgetFeature( this);
    choiceListFeature = new JComboBoxChoiceListFeature( this);
    creationFeature = new JComboBoxWidgetCreationFeature( this);
    selectionModelFeature = new SelectionModelFeature( this);
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
    if ( clss == ISourceFeature.class) return (T)sourceFeature;
    if ( clss == ITextWidgetFeature.class) return (T)textWidgetFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IChoiceListFeature.class) return (T)choiceListFeature;
    if ( clss == ILabelFeature.class) return (T)creationFeature;
    if ( clss == IValueFeature.class) return (T)textWidgetFeature;
    if ( clss == ISelectionModelFeature.class) return (T)selectionModelFeature;
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
  private ISourceFeature sourceFeature;
  private JComboBoxTextWidgetFeature textWidgetFeature;
  private JComboBoxChoiceListFeature choiceListFeature;
  private JComboBoxWidgetCreationFeature creationFeature;
  private ISelectionModelFeature selectionModelFeature;
  private ISelectionWidgetFeature selectionWidgetFeature;
  private IFeatured basicFeatureSet;

}
