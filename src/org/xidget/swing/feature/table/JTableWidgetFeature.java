/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.table;

import java.util.List;
import javax.swing.JTable;
import org.xidget.IXidget;
import org.xidget.ifeature.table.ITableWidgetFeature;
import org.xidget.swing.table.CustomTableModel;
import org.xidget.table.Row;
import org.xmodel.xpath.expression.StatefulContext;

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
   * @see org.xidget.ifeature.table.ITableWidgetFeature#insertRows(org.xmodel.xpath.expression.StatefulContext, int, org.xidget.table.Row[])
   */
  public void insertRows( StatefulContext parent, int rowIndex, Row[] rows)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.insertRows( rowIndex, rows);
    tableModel.fireTableRowsInserted( rowIndex, rowIndex + rows.length - 1);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#removeRows(org.xmodel.xpath.expression.StatefulContext, int, org.xidget.table.Row[])
   */
  public void removeRows( StatefulContext parent, int rowIndex, Row[] rows)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.removeRows( rowIndex, rows.length);
    tableModel.fireTableRowsDeleted( rowIndex, rowIndex + rows.length - 1);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#getRows(org.xmodel.xpath.expression.StatefulContext)
   */
  public List<Row> getRows( StatefulContext parent)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    return tableModel.getRows();
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
    tableModel.setColumnName( columnIndex, title);
    tableModel.fireTableStructureChanged();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#updateCell(org.xidget.table.Row, int)
   */
  public void updateCell( Row row, int columnIndex)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.fireTableCellUpdated( tableModel.getRows().indexOf( row), columnIndex);
  }

  private IXidget xidget;
}
