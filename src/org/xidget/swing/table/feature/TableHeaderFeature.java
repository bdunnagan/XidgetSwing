/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table.feature;

import javax.swing.JTable;
import org.xidget.swing.feature.ITableHeaderFeature;
import org.xidget.swing.table.CustomTableModel;

/**
 * An implementation of ITableHeaderFeature for JTable.
 */
public class TableHeaderFeature implements ITableHeaderFeature
{
  public TableHeaderFeature( JTable table)
  {
    this.table = table;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.table.feature.ITableHeaderFeature#setIcon(int, java.lang.Object)
   */
  public void setIcon( int column, Object icon)
  {
    CustomTableModel model = (CustomTableModel)table.getModel();
    model.setColumnIcon( column, icon);
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.table.feature.ITableHeaderFeature#setTitle(int, java.lang.String)
   */
  public void setTitle( int column, String title)
  {
    CustomTableModel model = (CustomTableModel)table.getModel();
    model.setColumnTitle( column, title);
  }
  
  private JTable table;
}
