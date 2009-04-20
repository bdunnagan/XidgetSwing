/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import javax.swing.JComponent;
import javax.swing.JTable;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.ComputeNodeFeature;
import org.xidget.feature.table.ColumnSetFeature;
import org.xidget.feature.table.DelegateRowSetFeature;
import org.xidget.feature.table.HeaderFeature;
import org.xidget.feature.table.RowSetFeature;
import org.xidget.feature.table.TableModelFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IErrorFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.table.IColumnSetFeature;
import org.xidget.ifeature.table.IHeaderFeature;
import org.xidget.ifeature.table.IRowSetFeature;
import org.xidget.ifeature.table.ITableModelFeature;
import org.xidget.ifeature.table.ITableWidgetFeature;
import org.xidget.swing.feature.SwingWidgetFeature;
import org.xidget.swing.feature.TooltipErrorFeature;
import org.xidget.swing.feature.table.JTableWidgetCreationFeature;
import org.xidget.swing.feature.table.JTableWidgetFeature;

/**
 * A table xidget implemented with the Swing JTable widget.
 */
public class JTableXidget extends Xidget
{
  public void createFeatures()
  {
    headerFeature = new HeaderFeature( this);
    columnSetFeature = new ColumnSetFeature( this);
    bindFeature = new BindFeature( this);
    errorFeature = new TooltipErrorFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    tableModelFeature = new TableModelFeature( this);
    tableWidgetFeature = new JTableWidgetFeature( this);
    computeNodeFeature = new ComputeNodeFeature( this);
    creationFeature = new JTableWidgetCreationFeature( this);
    
    // pick the right IRowSetFeature
    boolean delegate = getConfig().getFirstChild( "group") != null;
    rowSetFeature = delegate? new DelegateRowSetFeature( this): new RowSetFeature( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IHeaderFeature.class) return (T)headerFeature;
    if ( clss == IRowSetFeature.class) return (T)rowSetFeature;
    if ( clss == IColumnSetFeature.class) return (T)columnSetFeature;
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == IErrorFeature.class) return (T)errorFeature;
    if ( clss == ITableModelFeature.class) return (T)tableModelFeature;
    if ( clss == ITableWidgetFeature.class) return (T)tableWidgetFeature;
    if ( clss == IComputeNodeFeature.class) return (T)computeNodeFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IBindFeature.class) return (T)bindFeature;
    
    if ( clss == JComponent.class) return (T)creationFeature.getWidget();
    if ( clss == JTable.class) return (T)creationFeature.getWidget();
    
    return super.getFeature( clss);
  }
  
  private IHeaderFeature headerFeature;
  private IRowSetFeature rowSetFeature;
  private IColumnSetFeature columnSetFeature;
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private IErrorFeature errorFeature;
  private ITableModelFeature tableModelFeature;
  private ITableWidgetFeature tableWidgetFeature;
  private IComputeNodeFeature computeNodeFeature;
  private JTableWidgetCreationFeature creationFeature;  
}