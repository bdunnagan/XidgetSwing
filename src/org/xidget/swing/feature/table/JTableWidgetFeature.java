/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.table;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTable;
import org.xidget.IXidget;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.swing.table.CustomTableModel;
import org.xidget.tree.Row;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITableWidgetFeature for use with a Swing JTable.
 */
public class JTableWidgetFeature implements ITreeWidgetFeature
{
  public JTableWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.map = new HashMap<StatefulContext, Row>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#insertRows(org.xidget.table.Row, int, org.xidget.table.Row[])
   */
  public void insertRows( Row parent, int rowIndex, Row[] rows)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.insertRows( rowIndex, rows);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#removeRows(org.xidget.table.Row, int, org.xidget.table.Row[])
   */
  public void removeRows( Row parent, int rowIndex, Row[] rows)
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
   * @see org.xidget.table.features.ITableWidgetFeature#setEditable(int, int, boolean)
   */
//  public void setEditable( int rowIndex, int columnIndex, boolean editable)
//  {
//    JTable table = xidget.getFeature( JTable.class);
//    CustomTableModel tableModel = (CustomTableModel)table.getModel();
//    tableModel.setEditable( rowIndex, columnIndex, editable);
//  }

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
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#updateCell(org.xidget.table.Row, int)
   */
  public void updateCell( Row row, int columnIndex)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.fireTableCellUpdated( tableModel.getRows().indexOf( row), columnIndex);
  }

  private IXidget xidget;
  private Map<StatefulContext, Row> map;
}
