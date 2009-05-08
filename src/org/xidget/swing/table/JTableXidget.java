/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import javax.swing.JComponent;
import javax.swing.JTable;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.ComputeNodeFeature;
import org.xidget.feature.tree.ColumnSetFeature;
import org.xidget.feature.tree.RowSetFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.tree.IColumnSetFeature;
import org.xidget.ifeature.tree.IRowSetFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingWidgetFeature;
import org.xidget.swing.feature.table.JTableWidgetCreationFeature;
import org.xidget.swing.feature.table.JTableWidgetFeature;

/**
 * A table xidget implemented with the Swing JTable widget.
 */
public class JTableXidget extends Xidget
{
  public void createFeatures()
  {
    rowSetFeature = new RowSetFeature( this);
    columnSetFeature = new ColumnSetFeature( this);
    bindFeature = new BindFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    treeWidgetFeature = new JTableWidgetFeature( this);
    computeNodeFeature = new ComputeNodeFeature( this);
    creationFeature = new JTableWidgetCreationFeature( this);
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
    if ( clss == ITreeWidgetFeature.class) return (T)treeWidgetFeature;
    if ( clss == IComputeNodeFeature.class) return (T)computeNodeFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IBindFeature.class) return (T)bindFeature;
    
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
  private ITreeWidgetFeature treeWidgetFeature;
  private IComputeNodeFeature computeNodeFeature;
  private JTableWidgetCreationFeature creationFeature;  
  private IFeatured basicFeatureSet;
}
