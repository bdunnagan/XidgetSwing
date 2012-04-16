/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JTableXidget.java
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
package org.xidget.swing.table;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.model.SelectionModelFeature;
import org.xidget.feature.model.SelectionUpdateFeature;
import org.xidget.feature.tree.ColumnSetFeature;
import org.xidget.feature.tree.RowSetFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ITextWidgetFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.model.IPartialSelectionWidgetFeature;
import org.xidget.ifeature.model.ISelectionModelFeature;
import org.xidget.ifeature.model.ISelectionUpdateFeature;
import org.xidget.ifeature.model.ISelectionWidgetFeature;
import org.xidget.ifeature.tree.IColumnSetFeature;
import org.xidget.ifeature.tree.IColumnWidthFeature;
import org.xidget.ifeature.tree.IRowSetFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingTextWidgetFeature;
import org.xidget.swing.feature.SwingWidgetFeature;

/**
 * A table xidget implemented with the Swing JTable widget.
 */
public class JTableXidget extends Xidget
{
  public void createFeatures()
  {
    rowSetFeature = new RowSetFeature( this);
    columnSetFeature = new ColumnSetFeature( this);
    bindFeature = new BindFeature( this, new String[] { "text", "combo", "button"});
    widgetFeature = new SwingWidgetFeature( this);
    textFeature = new SwingTextWidgetFeature( this);
    treeWidgetFeature = new JTableWidgetFeature( this);
    columnWidthFeature = new JTableColumnWidthFeature( this);
    creationFeature = new JTableWidgetCreationFeature( this);
    selectionModelFeature = new SelectionModelFeature( this);
    selectionUpdateFeature = new SelectionUpdateFeature( this);
    basicFeatureSet = new BasicFeatureSet( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IRowSetFeature.class) return (T)rowSetFeature;
    if ( clss == IColumnSetFeature.class) return (T)columnSetFeature;
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == ITextWidgetFeature.class) return (T)textFeature;
    if ( clss == ITreeWidgetFeature.class) return (T)treeWidgetFeature;
    if ( clss == IColumnWidthFeature.class) return (T)columnWidthFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == ISelectionModelFeature.class) return (T)selectionModelFeature;
    if ( clss == ISelectionUpdateFeature.class) return (T)selectionUpdateFeature;
    if ( clss == ISelectionWidgetFeature.class) return (T)treeWidgetFeature;
    if ( clss == IPartialSelectionWidgetFeature.class) return (T)treeWidgetFeature;
    if ( clss == IBindFeature.class) return (T)bindFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getJScrollPane();
    if ( clss == JComponent.class) return (T)creationFeature.getJScrollPane();
    if ( clss == JTable.class) return (T)creationFeature.getJTable();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }
  
  private IRowSetFeature rowSetFeature;
  private IColumnSetFeature columnSetFeature;
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private ITextWidgetFeature textFeature;
  private ITreeWidgetFeature treeWidgetFeature;
  private IColumnWidthFeature columnWidthFeature;
  private JTableWidgetCreationFeature creationFeature;  
  private ISelectionModelFeature selectionModelFeature;
  private ISelectionUpdateFeature selectionUpdateFeature;
  private IFeatured basicFeatureSet;
}
