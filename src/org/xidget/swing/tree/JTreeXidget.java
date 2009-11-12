/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JTreeXidget.java
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
package org.xidget.swing.tree;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTree;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.SelectionModelFeature;
import org.xidget.feature.tree.TreeExpandFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.ISelectionWidgetFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingWidgetFeature;

/**
 * A tree xidget which uses the Swing JTree class.
 */
public class JTreeXidget extends Xidget
{
  /* (non-Javadoc)
   * @see org.xidget.Xidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    bindFeature = new BindFeature( this, new String[] { "tree"});
    creationFeature = new JTreeWidgetCreationFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    expandFeature = new TreeExpandFeature( this);
    treeWidgetFeature = new JTreeWidgetFeature( this);
    selectionModelFeature = new SelectionModelFeature( this);
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
    if ( clss == ITreeExpandFeature.class) return (T)expandFeature;
    if ( clss == ITreeWidgetFeature.class) return (T)treeWidgetFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == ISelectionWidgetFeature.class) return (T)treeWidgetFeature;
    if ( clss == ISelectionModelFeature.class) return (T)selectionModelFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getJScrollPane();
    if ( clss == JComponent.class) return (T)creationFeature.getJScrollPane();
    if ( clss == JTree.class) return (T)creationFeature.getJTree();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }  
    
  private IBindFeature bindFeature;
  private ITreeExpandFeature expandFeature;
  private JTreeWidgetFeature treeWidgetFeature;
  private JTreeWidgetCreationFeature creationFeature;
  private ISelectionModelFeature selectionModelFeature;
  private IWidgetFeature widgetFeature;
  private IFeatured basicFeatureSet;
}
