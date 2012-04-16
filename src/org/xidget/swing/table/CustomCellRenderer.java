/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * CustomCellRenderer.java
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

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
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
      Image image = (Image)model.getImageAt( rowIndex, columnIndex);
      setIcon( (image != null)? new ImageIcon( image): null);
      
      Object object = model.getValueAt( rowIndex, columnIndex);
      String text = (object != null)? object.toString(): "";
      if ( text.trim().length() > 0)
      {
        setIconTextGap( 2);
        setHorizontalAlignment( SwingConstants.LEFT);
        
        //setName( text);
        setText( text);
      }
      else
      {
        setIconTextGap( 0);
        setHorizontalAlignment( SwingConstants.CENTER);
      }
    }
        
    return this;
  }
}