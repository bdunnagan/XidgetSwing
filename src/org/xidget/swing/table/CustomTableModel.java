/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
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
  }
  
  /**
   * Remove rows.
   * @param rowIndex The index of the first row.
   * @param count The number of rows to remove.
   */
  public void removeRows( int rowIndex, int count)
  {
  }
  
  /**
   * Commit row-set changes to the specified parent.
   * @param parent The parent.
   */
  public void commit( Row parent)
  {
    // TODO: need better record keeping for finer grained events
    fireTableStructureChanged();
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
    return (text != null)? text: "";
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
    return row.getCell( columnIndex).source != null;
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
