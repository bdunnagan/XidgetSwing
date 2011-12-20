/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * CustomTableModel.java
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

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.xidget.IXidget;
import org.xidget.tree.Row;
import org.xmodel.IModelObject;

/**
 * A custom table model for table xidgets.
 */
@SuppressWarnings("serial")
public class CustomTableModel extends AbstractTableModel
{
  public CustomTableModel( IXidget xidget)
  {
    this.root = new Row( xidget);
    this.columns = new ArrayList<Column>( 5);
  }
  
  /**
   * Set the name of the specified column.
   * @param columnIndex The column index.
   * @param name The name.
   */
  public void setColumnName( int columnIndex, String name)
  {
    if ( columns.size() <= columnIndex)
    {
      for( int i = columns.size(); i <= columnIndex; i++)
        columns.add( new Column());
    }
    columns.get( columnIndex).title = name;
  }
  
  /**
   * Set the image of the specified column.
   * @param columnIndex The column index.
   * @param image The image.
   */
  public void setColumnImage( int columnIndex, Object image)
  {
    if ( columns.size() <= columnIndex)
    {
      for( int i = columns.size(); i <= columnIndex; i++)
        columns.add( new Column());
    }
    columns.get( columnIndex).image = image;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#getColumnName(int)
   */
  @Override
  public String getColumnName( int columnIndex)
  {
    if ( columns.size() <= columnIndex) return "";
    return columns.get( columnIndex).title;
  }

  /**
   * Returns the image for the specified column.
   * @param columnIndex The column index.
   * @return Returns the image for the specified column.
   */
  public Object getColumnImage( int columnIndex)
  {
    return columns.get( columnIndex).image;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnCount()
   */
  public int getColumnCount()
  {
    return columns.size();
  }

  /**
   * Insert rows.
   * @param rowIndex The index of the first row.
   * @param newRows The rows to be inserted. 
   */
  public void insertRows( int rowIndex, Row[] newRows)
  {
    fireTableRowsInserted( rowIndex, rowIndex + newRows.length - 1);
  }
  
  /**
   * Remove rows.
   * @param rowIndex The index of the first row.
   * @param count The number of rows to remove.
   */
  public void removeRows( int rowIndex, int count)
  {
    fireTableRowsDeleted( rowIndex, rowIndex + count - 1);
  }
  
  /**
   * Commit row-set changes to the specified parent.
   * @param parent The parent.
   */
  public void commit( Row parent)
  {
    // this causes the selection to be cleared
    //fireTableStructureChanged();
  }
  
  /**
   * Returns the rows of the table.
   * @return Returns the rows of the table.
   */
  public List<Row> getRows()
  {
    return root.getChildren();
  }
  
  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getRowCount()
   */
  public int getRowCount()
  {
    return root.getChildren().size();
  }

  /**
   * Returns the root of the table.
   * @return Returns the root of the table.
   */
  public Row getRoot()
  {
    return root;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  public Object getValueAt( int rowIndex, int columnIndex)
  {
    String text = root.getChildren().get( rowIndex).getCell( columnIndex).text;
    if ( text == null) return "";
    if ( text.equalsIgnoreCase( "true")) return Boolean.TRUE;
    if ( text.equalsIgnoreCase( "false")) return Boolean.FALSE;
    return text;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
   */
  @Override
  public void setValueAt( Object value, int rowIndex, int columnIndex)
  {
    Row row = root.getChildren().get( rowIndex);
    row.getCell( columnIndex).text = (value != null)? value.toString(): "";
    fireTableCellUpdated( rowIndex, columnIndex);
  }
  
  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
   */
  @Override
  public boolean isCellEditable( int rowIndex, int columnIndex)
  {
    Row row = root.getChildren().get( rowIndex);
    IXidget xidget = CustomCellEditor.findEditor( row, columnIndex);
    return xidget != null;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
   */
  @Override
  public Class<?> getColumnClass( int columnIndex)
  {
    return IModelObject.class;
  }

  /**
   * Returns the icon at the specified cell.
   * @param rowIndex The row index.
   * @param columnIndex The column index.
   */
  public Object getIconAt( int rowIndex, int columnIndex)
  {
    return root.getChildren().get( rowIndex).getCell( columnIndex).icon;
  }

  private class Column
  {
    public String title;
    public Object image;
  }
  
  private Row root;
  private List<Column> columns;
}
