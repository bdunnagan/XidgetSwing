/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table.renderer;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.xidget.swing.table.CustomTableModel;

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
  public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    // allow super-class to do its thing
    super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column);
    
    // consult table model for header title and icon
    CustomTableModel model = (CustomTableModel)table.getModel();
    setIcon( (Icon)model.getIconAt( row, column));
    
    Object object = model.getValueAt( row, column);
    String text = (object != null)? object.toString(): "";
    setName( text);
    setText( text);
        
    return this;
  }
}
