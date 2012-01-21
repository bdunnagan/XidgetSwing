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
package org.xidget.swing.applet;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JApplet;
import javax.swing.JComponent;

import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ITextWidgetFeature;
import org.xidget.ifeature.ITitleFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingContainerTextWidgetFeature;
import org.xmodel.ModelObject;

/**
 * An application xidget implemented with the Swing JFrame widget.
 */
public class JAppletXidget extends Xidget
{
  public JAppletXidget( JApplet applet)
  {
    creationFeature = new JAppletWidgetCreationFeature( applet);
    try { startConfig( null, null, new ModelObject( "dummy"));} catch( Exception e) {}
  }
  
  public void createFeatures()
  {
    bindFeature = new BindFeature( this);
    widgetFeature = new JAppletWidgetFeature( this);
    textFeature = new SwingContainerTextWidgetFeature( this);
    containerFeature = new JAppletContainerFeature( this);
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
    if ( clss == ITitleFeature.class) return (T)widgetFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IWidgetContainerFeature.class) return (T)containerFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getJApplet();
    if ( clss == JComponent.class) return (T)creationFeature.getJApplet();
    if ( clss == Container.class) return (T)creationFeature.getJApplet();
    if ( clss == Window.class) return (T)creationFeature.getJApplet();
    if ( clss == JApplet.class) return (T)creationFeature.getJApplet();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private ITextWidgetFeature textFeature;
  private JAppletWidgetCreationFeature creationFeature;
  private IWidgetContainerFeature containerFeature;
  private IFeatured basicFeatureSet;
}
