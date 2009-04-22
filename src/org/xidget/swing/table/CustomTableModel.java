/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.xidget.IXidget;
import org.xidget.table.Cell;
import org.xidget.table.Row;

/**
 * A custom table model for table xidgets.
 * TODO: set cell editor to xidget?
 */
@SuppressWarnings("serial")
public class CustomTableModel extends AbstractTableModel
{
  public CustomTableModel()
  {
    this.rows = new ArrayList<Row>();
    this.columns = new ArrayList<Column>( 5);
    this.editors = new ArrayList<IXidget>( 5);
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
    for( int i=0; i<newRows.length; i++)
      rows.add( rowIndex+i, newRows[ i]);
  }
  
  /**
   * Remove rows.
   * @param rowIndex The index of the first row.
   * @param count The number of rows to remove.
   */
  public void removeRows( int rowIndex, int count)
  {
    for( int i=0; i<count; i++)
      rows.remove( rowIndex);
  }
  
  /**
   * Returns the rows of the table.
   * @return Returns the rows of the table.
   */
  public List<Row> getRows()
  {
    return rows;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getRowCount()
   */
  public int getRowCount()
  {
    return rows.size();
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  public Object getValueAt( int rowIndex, int columnIndex)
  {
    Cell cell = rows.get( rowIndex).cells.get( columnIndex);
    return (cell != null)? cell.text: "";
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
   */
  @Override
  public void setValueAt( Object value, int rowIndex, int columnIndex)
  {
    if ( rowIndex >= rows.size())
    {
      for( int i = rows.size(); i <= rowIndex; i++)
        rows.add( new Row());
    }
    
    Row row = rows.get( rowIndex);
    if ( columnIndex >= row.cells.size())
    {
      for( int i = row.cells.size(); i <= columnIndex; i++)
        row.cells.add( new Cell());
    }
    
    Cell cell = row.cells.get( columnIndex);
    cell.text = (value != null)? value.toString(): "";
    fireTableCellUpdated( rowIndex, columnIndex);
  }
  
  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
   */
  @Override
  public boolean isCellEditable( int rowIndex, int columnIndex)
  {
    return false;
  }
  
  /**
   * Returns the icon at the specified cell.
   * @param rowIndex The row index.
   * @param columnIndex The column index.
   */
  public Object getIconAt( int rowIndex, int columnIndex)
  {
    Cell cell = rows.get( rowIndex).cells.get( columnIndex);
    return (cell != null)? cell.icon: null;
  }
  
  /**
   * Set the editor for the specified column.
   * @param columnIndex The column index.
   * @param xidget The xidget to use for editing.
   */
  public void setEditor( int columnIndex, IXidget xidget)
  {
    for( int i=editors.size(); i<=columnIndex; i++) editors.add( null);
    editors.set( columnIndex, xidget);
  }

  /**
   * Set whether the specified cell is editable.
   * @param rowIndex The row index of the cell.
   * @param columnIndex The column index of the cell.
   * @param editable True if the cell is editable.
   */
  public void setEditable( int rowIndex, int columnIndex, boolean editable)
  {
  }

  private class Column
  {
    public String title;
    public Object image;
  }
  
  private List<Column> columns;
  private List<Row> rows; 
  private List<IXidget> editors;
}
