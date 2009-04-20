/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * A TableCellRenderer to be used as the table column header renderer.
 */
@SuppressWarnings("serial")
public class CustomHeaderCellRenderer extends DefaultTableCellRenderer
{
  /* (non-Javadoc)
   * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(
   * javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
   */
  @Override
  public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    // allow super-class to do its thing
    super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column);

    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    
    setIcon( (Icon)tableModel.getColumnImage( column));
    setName( tableModel.getColumnName( column));
    setText( tableModel.getColumnName( column));
        
    return this;
  }
}
