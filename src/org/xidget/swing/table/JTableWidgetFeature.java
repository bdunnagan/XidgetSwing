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
import org.xidget.feature.tree.ColumnWidthFeature;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.ISelectionWidgetFeature;
import org.xidget.ifeature.table.ITableWidgetFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.tree.Row;
import org.xmodel.IModelObject;
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
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#removeRows(org.xidget.tree.Row, int, org.xidget.tree.Row[], boolean)
   */
  public void removeRows( Row parent, int rowIndex, Row[] rows, boolean dummy)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.removeRows( rowIndex, rows.length);
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
    tableModel.fireTableStructureChanged();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#setColumnWidth(int, int)
   */
  @Override
  public void setColumnWidth( int columnIndex, int width)
  {
    JTable table = xidget.getFeature( JTable.class);
    TableColumnModel model = table.getColumnModel();
    TableColumn column = model.getColumn( columnIndex);
    column.setMaxWidth( width);
    column.setPreferredWidth( width);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#updateCell(org.xidget.table.Row, int)
   */
  public void updateCell( Row row, int columnIndex)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.fireTableCellUpdated( tableModel.getRows().indexOf( row), columnIndex);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#setSelection(java.util.List)
   */
  public void setSelection( List<IModelObject> nodes)
  {
    JTable jtable = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)jtable.getModel();
    List<Row> rows = tableModel.getRows();
    for( IModelObject node: nodes)
    {
      int index = findNode( rows, node);
      if ( index >= 0) jtable.addRowSelectionInterval( index, index);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#getSelection()
   */
  public List<IModelObject> getSelection()
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
   * @see org.xidget.ifeature.ISelectionWidgetFeature#insertSelected(int, org.xmodel.IModelObject)
   */
  public void insertSelected( int index, IModelObject element)
  {
    System.out.println( "insert selected");
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#removeSelected(int, org.xmodel.IModelObject)
   */
  public void removeSelected( int index, IModelObject element)
  {
    System.out.println( "remove selected");
  }

  /**
   * Returns the first index of the row containing the specified node.
   * @param rows The rows.
   * @param node The node.
   * @return Returns -1 or the first index.
   */
  private int findNode( List<Row> rows, IModelObject node)
  {
    ISelectionModelFeature selectionModelFeature = xidget.getFeature( ISelectionModelFeature.class);
    Object identity = selectionModelFeature.getIdentity( node);
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
    int tableColumnCount = table.getColumnCount();
    if ( tableColumnCount > 0) return;
    
    IModelObject config = xidget.getConfig();
    if ( config.getNumberOfChildren( "column") == 0)
    {
      table.getTableHeader().setVisible( false);
    }

    CustomTableModel model = (CustomTableModel)table.getModel();
    int configColumnCount = config.getNumberOfChildren( "cell");
    for( int i=tableColumnCount; i<configColumnCount; i++)
    {
      model.setColumnName( i, "");
      table.addColumn( new TableColumn( i));
    }
    
    model.fireTableStructureChanged();
  }

  private IXidget xidget;
  private Map<StatefulContext, Row> map;
  private ColumnWidthFeature columnSizeCalculator;
}
