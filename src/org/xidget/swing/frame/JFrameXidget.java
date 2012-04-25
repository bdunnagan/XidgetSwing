/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JFrameXidget.java
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
package org.xidget.swing.frame;

import java.awt.Container;
import java.awt.Window;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ITitleFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.ToplevelWidgetFeature;

/**
 * An application xidget implemented with the Swing JFrame widget.
 */
public class JFrameXidget extends Xidget
{
  public void createFeatures()
  {
    bindFeature = new BindFeature( this);
    widgetFeature = new ToplevelWidgetFeature( this);
    creationFeature = new JFrameWidgetCreationFeature( this);
    containerFeature = new JFrameContainerFeature( this);
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
    if ( clss == ITitleFeature.class) return (T)widgetFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IWidgetContainerFeature.class) return (T)containerFeature;
    
    if ( clss == JComponent.class) return (T)creationFeature.getFrame();
    if ( clss == Container.class) return (T)creationFeature.getFrame();
    if ( clss == Window.class) return (T)creationFeature.getFrame();
    if ( clss == JFrame.class) return (T)creationFeature.getFrame();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private JFrameWidgetCreationFeature creationFeature;
  private IWidgetContainerFeature containerFeature;
  private IFeatured basicFeatureSet;
}
