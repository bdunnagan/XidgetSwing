/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.table;

import javax.swing.JTable;
import org.xidget.IXidget;
import org.xidget.ifeature.table.ITableModelFeature;
import org.xidget.ifeature.table.ITableWidgetFeature;
import org.xidget.swing.table.CustomTableModel;
import org.xidget.table.Row;

/**
 * An implementation of ITableWidgetFeature for use with a Swing JTable.
 */
public class JTableWidgetFeature implements ITableWidgetFeature
{
  public JTableWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#insertRows(int, int)
   */
  public void insertRows( int rowIndex, int count)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.fireTableRowsInserted( rowIndex, rowIndex + count - 1);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#removeRows(int, int)
   */
  public void removeRows( int rowIndex, int count)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.fireTableRowsDeleted( rowIndex, rowIndex + count - 1);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#setEditable(int, int, boolean)
   */
  public void setEditable( int rowIndex, int columnIndex, boolean editable)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.setEditable( rowIndex, columnIndex, editable);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#setTitle(int, java.lang.String)
   */
  public void setTitle( int columnIndex, String title)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.fireTableStructureChanged();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#setIcon(org.xidget.table.Row, int, java.lang.Object)
   */
  public void setIcon( Row row, int columnIndex, Object icon)
  {
    ITableModelFeature feature = xidget.getFeature( ITableModelFeature.class);
    int rowIndex = feature.getRows().indexOf( row);
    
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.fireTableCellUpdated( rowIndex, columnIndex);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#setText(org.xidget.table.Row, int, java.lang.String)
   */
  public void setText( Row row, int columnIndex, String text)
  {
    ITableModelFeature feature = xidget.getFeature( ITableModelFeature.class);
    int rowIndex = feature.getRows().indexOf( row);
    
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.fireTableCellUpdated( rowIndex, columnIndex);
  }

  private IXidget xidget;
}
