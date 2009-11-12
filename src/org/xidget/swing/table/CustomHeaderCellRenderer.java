/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * CustomHeaderCellRenderer.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.swing.table;

import java.awt.Color;
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

    setBackground( Color.lightGray);
    setIcon( (Icon)tableModel.getColumnImage( column));
    setText( tableModel.getColumnName( column));
        
    return this;
  }
}
