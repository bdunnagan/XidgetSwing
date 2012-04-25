/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JPanelXidget.java
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
package org.xidget.swing.form;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.AnchorLayoutFeature;
import org.xidget.feature.BindFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IImageFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.ITextWidgetFeature;
import org.xidget.ifeature.ITitleFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.canvas.ICanvasFeature;
import org.xidget.swing.canvas.CanvasFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingContainerTextWidgetFeature;
import org.xidget.swing.feature.SwingTitleFeature;

/**
 * A form xidget implemented with the Swing JPanel widget.
 */
public class JPanelXidget extends Xidget
{
  public void createFeatures()
  {
    bindFeature = new BindFeature( this);
    layoutFeature = new AnchorLayoutFeature( this);
    widgetFeature = new CanvasWidgetFeature( this);
    textFeature = new SwingContainerTextWidgetFeature( this);
    titleFeature = new SwingTitleFeature( this);
    iconFeature = new TabImageFeature( this);
    creationFeature = new JPanelWidgetCreationFeature( this);
    containerFeature = new JPanelContainerFeature( this);
    canvasFeature = new CanvasFeature( this);
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
    if ( clss == ITextWidgetFeature.class) return (T)textFeature;
    if ( clss == ITitleFeature.class) return (T)titleFeature;
    if ( clss == IImageFeature.class) return (T)iconFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IWidgetContainerFeature.class) return (T)containerFeature;
    if ( clss == ICanvasFeature.class) return (T)canvasFeature;
    
    if ( clss == JComponent.class) return (T)creationFeature.getOuterWidget();
    if ( clss == JPanel.class) return (T)creationFeature.getInnerWidget();
    if ( clss == Container.class) return (T)creationFeature.getInnerWidget();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private ITextWidgetFeature textFeature;
  private ILayoutFeature layoutFeature;
  private ITitleFeature titleFeature;
  private IImageFeature iconFeature;
  private JPanelWidgetCreationFeature creationFeature;
  private IWidgetContainerFeature containerFeature;
  private ICanvasFeature canvasFeature;
  private IFeatured basicFeatureSet;
}
