/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.xidget.IXidget;
import org.xidget.table.ifeatures.ITableModelFeature;
import org.xidget.table.ifeatures.ITableWidgetFeature;
import org.xidget.text.TextXidget;

/**
 * A custom table model for table xidgets.
 * TODO: set cell editor to xidget?
 */
@SuppressWarnings("serial")
public class CustomTableModel extends AbstractTableModel implements ITableWidgetFeature
{
  public CustomTableModel( IXidget xidget)
  {
    this.xidget = xidget;
    this.editors = new ArrayList<IXidget>( 5);
    this.headers = new ArrayList<Column>( 5);
    this.rows = new ArrayList<Column[]>( 5);
  }
  
  /**
   * Set the header of the specified column.
   * @param columnIndex The column index.
   * @param title The title.
   */
  public void setColumnTitle( int columnIndex, String title)
  {
    Column column = new Column();
    column.text = title;
    
    if ( columnIndex >= headers.size())
    {
      headers.add( column);
    }
    else
    {
      headers.set( columnIndex, column);
    }
    
    changeColumnCount( columnIndex);
    fireTableStructureChanged();
  }
  
  /**
   * Set the header of the specified column.
   * @param columnIndex The column index.
   * @param icon The icon.
   */
  public void setColumnIcon( int columnIndex, Object icon)
  {
    Column column = new Column();
    column.icon = icon;
    
    if ( columnIndex >= headers.size())
    {
      headers.add( column);
    }
    else
    {
      headers.set( columnIndex, column);
    }
    
    changeColumnCount( columnIndex);
    fireTableStructureChanged();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#insertRow(int)
   */
  public void insertRow( int row)
  {
    Column[] column = new Column[ getColumnCount()];
    rows.add( column);
    fireTableRowsInserted( row, row);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#removeRow(int)
   */
  public void removeRow( int row)
  {
    rows.remove( row);
    fireTableRowsDeleted( row, row);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#insertRows(int, int)
   */
  public void insertRows( int row, int count)
  {
    int columnCount = getColumnCount();
    for( int i=0; i<count; i++)
    {
      Column[] column = new Column[ columnCount];
      rows.add( column);
    }
    fireTableRowsInserted( row, row+count-1);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#removeRows(int, int)
   */
  public void removeRows( int row, int count)
  {
    for( int i=0; i<count; i++) rows.remove( row);
    fireTableRowsDeleted( row, row+count-1);
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#getColumnName(int)
   */
  @Override
  public String getColumnName( int column)
  {
    return headers.get( column).text;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnCount()
   */
  public int getColumnCount()
  {
    return headers.size();
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
    changeColumnCount( columnIndex+1);
    Column[] columns = rows.get( rowIndex);
    return columns[ columnIndex].text;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
   */
  @Override
  public void setValueAt( Object value, int rowIndex, int columnIndex)
  {
    changeColumnCount( columnIndex+1);
    
    // set text in table
    Column[] columns = rows.get( rowIndex);
    columns[ columnIndex].text = value.toString();
    
    // set text in model
    ITableModelFeature feature = xidget.getFeature( ITableModelFeature.class);
    feature.setText( rowIndex, columnIndex, TextXidget.allChannel, value.toString());
  }
  
  /**
   * Returns the icon at the specified cell.
   * @param rowIndex The row index.
   * @param columnIndex The column index.
   */
  public Object getIconAt( int rowIndex, int columnIndex)
  {
    changeColumnCount( columnIndex+1);
    Column[] columns = rows.get( rowIndex);
    return columns[ columnIndex].icon;
  }
  
  /**
   * Returns the header title of the specified column.
   * @param columnIndex The column index.
   */
  public String getHeaderTitleAt( int columnIndex)
  {
    changeColumnCount( columnIndex+1);
    Column header = headers.get( columnIndex);
    return header.text;
  }
  
  /**
   * Returns the header icon of the specified column.
   * @param columnIndex The column index.
   */
  public Object getHeaderIconAt( int columnIndex)
  {
    changeColumnCount( columnIndex+1);
    Column header = headers.get( columnIndex);
    return header.icon;
  }
    
  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#setEditor(int, org.xidget.IXidget)
   */
  public void setEditor( int column, IXidget xidget)
  {
    for( int i=editors.size(); i<=column; i++) editors.add( null);
    editors.set( column, xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#setIcon(int, int, java.lang.Object)
   */
  public void setIcon( int rowIndex, int columnIndex, Object icon)
  {
    changeColumnCount( columnIndex+1);
    Column[] columns = rows.get( rowIndex);
    columns[ columnIndex].icon = icon;
    fireTableCellUpdated( rowIndex, columnIndex);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#setText(int, int, java.lang.String)
   */
  public void setText( int rowIndex, int columnIndex, String text)
  {
    changeColumnCount( columnIndex+1);
    Column[] columns = rows.get( rowIndex);
    columns[ columnIndex].text = text;
    fireTableCellUpdated( rowIndex, columnIndex);
  }
  
  /**
   * Set whether the specified cell is editable.
   * @param rowIndex The row index of the cell.
   * @param columnIndex The column index of the cell.
   * @param editable True if the cell is editable.
   */
  public void setEditable( int rowIndex, int columnIndex, boolean editable)
  {
    changeColumnCount( columnIndex+1);
    rows.get( rowIndex)[ columnIndex].editable = editable;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
   */
  @Override
  public boolean isCellEditable( int rowIndex, int columnIndex)
  {
    changeColumnCount( columnIndex+1);
    if ( editors.get( rowIndex) == null) return false;
    return rows.get( rowIndex)[ columnIndex].editable;
  }

  /**
   * Change the column count for all rows.
   * @param count The new count.
   */
  private void changeColumnCount( int count)
  {
    if ( rows.size() == 0 || rows.get( 0).length > count) return;
    
    int current = rows.get( 0).length;
    int max = (current < count)? current: count;
    for( int i=0; i<rows.size(); i++)
    {
      Column[] columns = new Column[ count];
      for( int j=0; j<count; j++) columns[ j] = new Column();
      
      System.arraycopy( rows.get( i), 0, columns, 0, max);
      for( int j=max; j<count; j++) columns[ j].text = "";
      
      rows.set( i, columns);
    }
  }

  private class Column
  {
    Object icon;
    String text;
    boolean editable;
  }

  private IXidget xidget;
  private List<IXidget> editors;
  private List<Column> headers;
  private List<Column[]> rows;
}
