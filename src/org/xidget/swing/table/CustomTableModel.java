/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.xidget.IXidget;
import org.xidget.ifeature.table.ITableModelFeature;
import org.xidget.table.Cell;
import org.xidget.table.Column;
import org.xidget.table.Row;

/**
 * A custom table model for table xidgets.
 * TODO: set cell editor to xidget?
 */
@SuppressWarnings("serial")
public class CustomTableModel extends AbstractTableModel
{
  public CustomTableModel( IXidget xidget)
  {
    this.xidget = xidget;
    this.editors = new ArrayList<IXidget>( 5);
  }
  
  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#getColumnName(int)
   */
  @Override
  public String getColumnName( int columnIndex)
  {
    ITableModelFeature feature = xidget.getFeature( ITableModelFeature.class);
    Column column = feature.getColumns().get( columnIndex);
    return column.title;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getColumnCount()
   */
  public int getColumnCount()
  {
    ITableModelFeature feature = xidget.getFeature( ITableModelFeature.class);
    return feature.getColumns().size();
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getRowCount()
   */
  public int getRowCount()
  {
    ITableModelFeature feature = xidget.getFeature( ITableModelFeature.class);
    return feature.getRows().size();
  }

  /* (non-Javadoc)
   * @see javax.swing.table.TableModel#getValueAt(int, int)
   */
  public Object getValueAt( int rowIndex, int columnIndex)
  {
    ITableModelFeature feature = xidget.getFeature( ITableModelFeature.class);
    Row row = feature.getRows().get( rowIndex);
    Cell cell = row.cells.get( columnIndex);
    return cell.text;
  }

  /* (non-Javadoc)
   * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
   */
  @Override
  public void setValueAt( Object value, int rowIndex, int columnIndex)
  {
    ITableModelFeature feature = xidget.getFeature( ITableModelFeature.class);
    Row row = feature.getRows().get( rowIndex);
    Cell cell = row.cells.get( columnIndex);
    cell.source.setValue( value);
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
    ITableModelFeature feature = xidget.getFeature( ITableModelFeature.class);
    Row row = feature.getRows().get( rowIndex);
    Cell cell = row.cells.get( columnIndex);
    return cell.icon;
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

  private IXidget xidget;
  private List<IXidget> editors;
}
