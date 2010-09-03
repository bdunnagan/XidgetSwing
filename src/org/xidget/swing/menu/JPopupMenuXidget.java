/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JPopupMenuXidget.java
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
package org.xidget.swing.menu;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.GenericContainerFeature;
import org.xidget.swing.feature.SwingContainerWidgetFeature;

/**
 * A xidget implementation for Swing JMenu widgets.
 */
public class JPopupMenuXidget extends Xidget
{
  /* (non-Javadoc)
   * @see org.xidget.Xidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    bindFeature = new BindFeature( this);
    widgetFeature = new SwingContainerWidgetFeature( this);
    containerFeature = new GenericContainerFeature( this);
    creationFeature = new JPopupMenuWidgetCreationFeature( this);
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
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IBindFeature.class) return (T)bindFeature;
    if ( clss == IWidgetContainerFeature.class) return (T)containerFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getJPopupMenu();
    if ( clss == Container.class) return (T)creationFeature.getJPopupMenu();
    if ( clss == JComponent.class) return (T)creationFeature.getJPopupMenu();
    if ( clss == JPopupMenu.class) return (T)creationFeature.getJPopupMenu();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }

  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private IWidgetContainerFeature containerFeature;
  private JPopupMenuWidgetCreationFeature creationFeature;  
  private IFeatured basicFeatureSet;
}
