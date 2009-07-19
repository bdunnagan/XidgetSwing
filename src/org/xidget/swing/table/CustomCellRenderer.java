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
 * A TableCellRenderer to be used as the table column row renderer. 
 */
@SuppressWarnings("serial")
public class CustomCellRenderer extends DefaultTableCellRenderer
{
  /* (non-Javadoc)
   * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(
   * javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
   */
  @Override
  public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex)
  {
    if ( value instanceof Boolean)
    {
      return table.getDefaultRenderer( Boolean.class).getTableCellRendererComponent( table, value, isSelected, hasFocus, rowIndex, columnIndex);
    }
    else
    {
      // allow super-class to do its thing
      super.getTableCellRendererComponent( table, value, isSelected, hasFocus, rowIndex, columnIndex);
      
      // consult table model for header title and icon
      CustomTableModel model = (CustomTableModel)table.getModel();
      setIcon( (Icon)model.getIconAt( rowIndex, columnIndex));
      
      Object object = model.getValueAt( rowIndex, columnIndex);
      String text = (object != null)? object.toString(): "";
      setName( text);
      setText( text);
    }
        
    return this;
  }
}
