/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.table;

import javax.swing.JTable;
import org.xidget.IXidget;
import org.xidget.ifeature.table.ITableWidgetFeature;
import org.xidget.swing.table.CustomTableModel;

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
   * @see org.xidget.table.features.ITableWidgetFeature#insertRow(int)
   */
  public void insertRow( int row)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.insertRow( row);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#insertRows(int, int)
   */
  public void insertRows( int row, int count)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.insertRows( row, count);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#removeRow(int)
   */
  public void removeRow( int row)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.removeRow( row);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#removeRows(int, int)
   */
  public void removeRows( int row, int count)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.removeRows( row, count);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#setEditable(int, int, boolean)
   */
  public void setEditable( int row, int column, boolean editable)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.setEditable( row, column, editable);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#setIcon(int, int, java.lang.Object)
   */
  public void setIcon( int row, int column, Object icon)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.setIcon( row, column, icon);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#setText(int, int, java.lang.String)
   */
  public void setText( int row, int column, String text)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.setText( row, column, text);
  }

  private IXidget xidget;
}
