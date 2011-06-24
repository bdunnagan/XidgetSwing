/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JTableWidgetFeature.java
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.xidget.IXidget;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.ISelectionWidgetFeature;
import org.xidget.ifeature.table.ITableWidgetFeature;
import org.xidget.ifeature.tree.IColumnWidthFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.tree.Row;
import org.xmodel.IModelObject;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITableWidgetFeature for use with a Swing JTable.
 */
public class JTableWidgetFeature implements ITableWidgetFeature, ITreeWidgetFeature, ISelectionWidgetFeature
{
  public JTableWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.map = new HashMap<StatefulContext, Row>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#setShowGrid(boolean)
   */
  public void setShowGrid( boolean show)
  {
    JTable table = xidget.getFeature( JTable.class);
    if ( table != null) table.setShowGrid( show);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#insertRows(org.xidget.table.Row, int, org.xidget.table.Row[])
   */
  public void insertRows( Row parent, int rowIndex, Row[] rows)
  {
    JTable table = xidget.getFeature( JTable.class);
    createColumns( table);
    
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.insertRows( rowIndex, rows);
    
    IColumnWidthFeature widthFeature = xidget.getFeature( IColumnWidthFeature.class);
    for( int i=0; i<rows.length; i++) widthFeature.insertRow( rowIndex + i);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#removeRows(org.xidget.tree.Row, int, org.xidget.tree.Row[], boolean)
   */
  public void removeRows( Row parent, int rowIndex, Row[] rows, boolean dummy)
  {
    JTable table = xidget.getFeature( JTable.class);
    
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.removeRows( rowIndex, rows.length);
    
    if ( !dummy)
    {
      IColumnWidthFeature widthFeature = xidget.getFeature( IColumnWidthFeature.class);
      for( int i=0; i<rows.length; i++) widthFeature.removeRow( rowIndex);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#commit(org.xidget.table.Row)
   */
  public void commit( Row parent)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.commit( parent);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#isVisible(org.xidget.table.Row)
   */
  public boolean isVisible( Row row)
  {
    return true;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#findRow(org.xmodel.xpath.expression.StatefulContext)
   */
  public Row findRow( StatefulContext context)
  {
    if ( !map.containsKey( context)) 
    {
      JTable table = xidget.getFeature( JTable.class);
      CustomTableModel tableModel = (CustomTableModel)table.getModel();
      Row root = tableModel.getRoot();
      root.setContext( context);
      map.put( context, root);
      return root;
    }
    return map.get( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#getChildren(org.xidget.table.Row)
   */
  public List<Row> getChildren( Row parent)
  {
    return parent.getChildren();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#setTitle(int, java.lang.String)
   */
  public void setTitle( int columnIndex, String title)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.setColumnName( columnIndex, title);
    if ( title.length() > 0) table.getTableHeader().setVisible( true);
    tableModel.fireTableStructureChanged();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#setColumnWidth(int, int)
   */
  @Override
  public void setColumnWidth( int columnIndex, int width)
  {
    JTable table = xidget.getFeature( JTable.class);
    TableColumnModel model = (TableColumnModel)table.getColumnModel();
    if ( columnIndex < model.getColumnCount())
    {
      TableColumn column = model.getColumn( columnIndex);
      column.setMinWidth( 0);
      column.setMaxWidth( Integer.MAX_VALUE);
      column.setPreferredWidth( width);
      column.setWidth( width);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#updateCell(org.xidget.table.Row, int)
   */
  public void updateCell( Row row, int columnIndex)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    
    int rowIndex = findRowIndex( tableModel, row);
    tableModel.fireTableCellUpdated( rowIndex, columnIndex);
    
    IColumnWidthFeature widthFeature = xidget.getFeature( IColumnWidthFeature.class);
    widthFeature.setColumnText( rowIndex, columnIndex, row.getCell( columnIndex).text);
  }
  
  /**
   * Returns the index of the specified row.
   * @param model The table model.
   * @param row The row.
   * @return Returns -1 or the index of the specified row.
   */
  private int findRowIndex( CustomTableModel model, Row row)
  {
    List<Row> rows = model.getRows();
    if ( rows.size() == 0) return -1;
    
    // check end of list first for fast initial population
    if ( rows.get( rows.size() - 1) == row) return rows.size() - 1;
    
    // check list
    return model.getRows().indexOf( row);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#setSelection(java.util.List)
   */
  public void setSelection( List<? extends Object> objects)
  {
    JTable jtable = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)jtable.getModel();
    List<Row> rows = tableModel.getRows();
    for( Object object: objects)
    {
      int index = findNode( rows, object);
      if ( index >= 0) jtable.addRowSelectionInterval( index, index);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#getSelection()
   */
  public List<? extends Object> getSelection()
  {
    JTable jtable = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)jtable.getModel();
    List<Row> rows = tableModel.getRows();
    
    List<IModelObject> selection = new ArrayList<IModelObject>();
    for( int index: jtable.getSelectedRows())
    {
      selection.add( rows.get( index).getContext().getObject());
    }
    
    return selection;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#insertSelected(int, java.lang.Object)
   */
  public void insertSelected( int at, Object object)
  {
    JTable jtable = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)jtable.getModel();
    List<Row> rows = tableModel.getRows();
    int index = findNode( rows, object);
    if ( index >= 0) jtable.addRowSelectionInterval( index, index);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#removeSelected(int, java.lang.Object)
   */
  public void removeSelected( Object object)
  {
    JTable jtable = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)jtable.getModel();
    List<Row> rows = tableModel.getRows();
    int index = findNode( rows, object);
    if ( index >= 0) jtable.removeRowSelectionInterval( index, index);
  }

  /**
   * Returns the first index of the row containing the specified object.
   * @param rows The rows.
   * @param object The object.
   * @return Returns -1 or the first index.
   */
  private int findNode( List<Row> rows, Object object)
  {
    ISelectionModelFeature selectionModelFeature = xidget.getFeature( ISelectionModelFeature.class);
    Object identity = selectionModelFeature.getIdentity( object);
    for( int i=0; i<rows.size(); i++)
    {
      if ( selectionModelFeature.getIdentity( rows.get( i).getContext().getObject()).equals( identity))
        return i;
    }
    return -1;
  }
  
  /**
   * Create columns of table if necessary.
   * @param table The table.
   */
  private void createColumns( JTable table)
  {
    IModelObject config = xidget.getConfig();
    CustomTableModel model = (CustomTableModel)table.getModel();
    int configColumnCount = getConfiguredColumnCount( config);
    int tableColumnCount = table.getColumnCount();
    
    for( int i=tableColumnCount; i<configColumnCount; i++)
    {
      model.setColumnName( i, "");
      TableColumn column = new TableColumn( i);
      table.addColumn( column);
    }
    
    model.fireTableStructureChanged();
  }
  
  /**
   * Returns the maximum number of columns configured by column or cell declarations.
   * @param config The table configuration.
   * @return Returns the maximum number of configured columns.
   */
  private static int getConfiguredColumnCount( IModelObject config)
  {
    int max = 0;
    IExpression tableExpr = XPath.createExpression( "nested-or-self::table");
    List<IModelObject> tables = tableExpr.query( config, null);
    for( IModelObject table: tables)
    {
      int columns = table.getNumberOfChildren( "column");
      int cells = table.getNumberOfChildren( "cell");
      if ( max < columns) max = columns;
      if ( max < cells) max = cells;
    }
    return max;
  }
  
  private IXidget xidget;
  private Map<StatefulContext, Row> map;
}
