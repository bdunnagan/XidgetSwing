/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JTabbedPaneXidget.java
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
package org.xidget.swing.tabs;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.model.SelectionModelFeature;
import org.xidget.feature.model.SelectionUpdateFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ITextWidgetFeature;
import org.xidget.ifeature.ITitleFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.model.ISelectionModelFeature;
import org.xidget.ifeature.model.ISelectionUpdateFeature;
import org.xidget.ifeature.model.ISelectionWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingContainerTextWidgetFeature;
import org.xidget.swing.feature.SwingContainerWidgetFeature;
import org.xidget.swing.feature.SwingTitleFeature;

/**
 * A form xidget implemented with the Swing JPanel widget.
 */
public class JTabbedPaneXidget extends Xidget
{
  public void createFeatures()
  {
    bindFeature = new JTabbedPaneBindFeature( this);
    widgetFeature = new SwingContainerWidgetFeature( this);
    textFeature = new SwingContainerTextWidgetFeature( this);
    titleFeature = new SwingTitleFeature( this);
    creationFeature = new JTabbedPaneWidgetCreationFeature( this);
    containerFeature = new JTabbedPaneContainerFeature( this);
    selectionModelFeature = new SelectionModelFeature( this);
    selectionUpdateFeature = new SelectionUpdateFeature( this);
    selectionWidgetFeature = new JTabbedPaneSelectionWidgetFeature( this);
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
    if ( clss == ITextWidgetFeature.class) return (T)textFeature;
    if ( clss == ITitleFeature.class) return (T)titleFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IWidgetContainerFeature.class) return (T)containerFeature;
    if ( clss == ISelectionModelFeature.class) return (T)selectionModelFeature;
    if ( clss == ISelectionUpdateFeature.class) return (T)selectionUpdateFeature;
    if ( clss == ISelectionWidgetFeature.class) return (T)selectionWidgetFeature;
    
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
  private ITextWidgetFeature textFeature;
  private ITitleFeature titleFeature;
  private JTabbedPaneWidgetCreationFeature creationFeature;
  private IWidgetContainerFeature containerFeature;
  private ISelectionModelFeature selectionModelFeature;
  private ISelectionUpdateFeature selectionUpdateFeature;
  private ISelectionWidgetFeature selectionWidgetFeature;
  private IFeatured basicFeatureSet;
}
