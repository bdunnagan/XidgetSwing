/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table.feature;

import javax.swing.JTable;
import org.xidget.IXidget;
import org.xidget.swing.feature.ISwingWidgetFeature;
import org.xidget.swing.table.CustomTableModel;
import org.xidget.table.ifeatures.ITableWidgetFeature;

/**
 * An implementation of ITableWidgetFeature for use with a Swing JTable.
 */
public class JTableWidgetFeature implements ITableWidgetFeature
{
  public JTableWidgetFeature( IXidget xidget)
  {
    ISwingWidgetFeature feature = xidget.getFeature( ISwingWidgetFeature.class);
    JTable table = (JTable)feature.getWidget();
    this.tableModel = (CustomTableModel)table.getModel();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#insertRow(int)
   */
  public void insertRow( int row)
  {
    tableModel.insertRow( row);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#insertRows(int, int)
   */
  public void insertRows( int row, int count)
  {
    tableModel.insertRows( row, count);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#removeRow(int)
   */
  public void removeRow( int row)
  {
    tableModel.removeRow( row);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#removeRows(int, int)
   */
  public void removeRows( int row, int count)
  {
    tableModel.removeRows( row, count);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#setEditable(int, int, boolean)
   */
  public void setEditable( int row, int column, boolean editable)
  {
    tableModel.setEditable( row, column, editable);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#setIcon(int, int, java.lang.Object)
   */
  public void setIcon( int row, int column, Object icon)
  {
    tableModel.setIcon( row, column, icon);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#setText(int, int, java.lang.String)
   */
  public void setText( int row, int column, String text)
  {
    tableModel.setText( row, column, text);
  }
  
  private CustomTableModel tableModel;
}
