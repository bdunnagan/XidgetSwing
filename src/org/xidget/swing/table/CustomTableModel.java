/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.xidget.IXidget;
import org.xidget.table.features.ITableModelFeature;
import org.xidget.table.features.ITableWidgetFeature;
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
    Column[] columns = rows.get( rowIndex);
    return columns[ columnIndex].text;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
   */
  @Override
  public void setValueAt( Object value, int rowIndex, int columnIndex)
  {
    // set text in table
    Column[] columns = rows.get( rowIndex);
    columns[ columnIndex].text = value.toString();
    
    // set text in model
    ITableModelFeature feature = xidget.getFeature( ITableModelFeature.class);
    feature.setText( rowIndex, columnIndex, TextXidget.allChannel, value.toString());
  }
  
  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#setEditor(int, org.xidget.IXidget)
   */
  public void setEditor( int column, IXidget xidget)
  {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#setIcon(int, int, java.lang.Object)
   */
  public void setIcon( int rowIndex, int columnIndex, Object icon)
  {
    Column[] columns = rows.get( rowIndex);
    columns[ columnIndex].icon = icon;
    fireTableCellUpdated( rowIndex, columnIndex);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableWidgetFeature#setText(int, int, java.lang.String)
   */
  public void setText( int rowIndex, int columnIndex, String text)
  {
    Column[] columns = rows.get( rowIndex);
    columns[ columnIndex].text = text;
    fireTableCellUpdated( rowIndex, columnIndex);
  }
  
  /**
   * Change the column count for all rows.
   * @param count The new count.
   */
  private void changeColumnCount( int count)
  {
    if ( rows.size() == 0) return;
    
    int current = rows.get( 0).length;
    int max = (current < count)? current: count;
    for( int i=0; i<rows.size(); i++)
    {
      Column[] columns = new Column[ count];
      for( int j=0; j<count; j++) columns[ j] = new Column();
      
      System.arraycopy( rows.get( i), 0, columns, 0, max);
      for( int j=max; j<=count; j++) columns[ j].text = "";
      
      rows.set( i, columns);
    }
  }

  private class Column
  {
    Object icon;
    String text;
  }

  private IXidget xidget;
  private List<Column> headers;
  private List<Column[]> rows;
}
