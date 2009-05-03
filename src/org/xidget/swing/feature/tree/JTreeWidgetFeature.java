/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTree;
import org.xidget.IXidget;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.swing.tree.CustomTreeModel;
import org.xidget.table.Row;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITreeWidgetFeature for a Netbeans Outline widget.
 */
public class JTreeWidgetFeature implements ITreeWidgetFeature
{
  public JTreeWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.map = new HashMap<StatefulContext, Row>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#insertRows(org.xidget.table.Row, int, org.xidget.table.Row[])
   */
  public void insertRows( Row parent, int rowIndex, Row[] rows)
  {
    // update map
    for( int i=0; i<rows.length; i++) 
      map.put( rows[ i].getContext(), rows[ i]);
    
    // notify widget
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel treeModel = (CustomTreeModel)jtree.getModel();
    treeModel.insertRows( parent, rowIndex, rows);
    
    // expand content of row according to policy
    ITreeExpandFeature expandFeature = xidget.getFeature( ITreeExpandFeature.class);
    for( int i=0; i<rows.length; i++)
      expandFeature.rowAdded( rows[ i]);    
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#removeRows(org.xidget.table.Row, int, org.xidget.table.Row[])
   */
  public void removeRows( Row parent, int rowIndex, Row[] rows)
  {
    // let expansion policy cleanup listeners
    ITreeExpandFeature expandFeature = xidget.getFeature( ITreeExpandFeature.class);
    for( int i=0; i<rows.length; i++)
      expandFeature.rowRemoved( rows[ i]);
    
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel treeModel = (CustomTreeModel)jtree.getModel();
    
    // update model
    for( int i=0; i<rows.length; i++)
      map.remove( rows[ i].getContext());
    
    // notify widget
    treeModel.removeRows( parent, rowIndex, rows);    
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#getChildren(org.xidget.table.Row)
   */
  public List<Row> getChildren( Row parent)
  {
    return parent.getChildren();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#findRow(org.xmodel.xpath.expression.StatefulContext)
   */
  public Row findRow( StatefulContext context)
  {
    if ( !map.containsKey( context))
    {
      JTree jtree = xidget.getFeature( JTree.class);
      CustomTreeModel treeModel = (CustomTreeModel)jtree.getModel();
      Row root = (Row)treeModel.getRoot();
      root.setContext( context);
      map.put( context, root);
    }
    
    return map.get( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#setTitle(int, java.lang.String)
   */
  public void setTitle( int columnIndex, String title)
  {
//    JTree jtree = xidget.getFeature( JTree.class);
//    CustomTreeModel treeModel = (CustomTreeModel)jtree.getModel();
//    CustomTableModel tableModel = (CustomTableModel)outlineModel.getTableModel();
//    tableModel.setColumnName( columnIndex, title);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#updateCell(org.xidget.table.Row, int)
   */
  public void updateCell( Row row, int columnIndex)
  {
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel treeModel = (CustomTreeModel)jtree.getModel();
    treeModel.updateCells( row);
  }
  
  private IXidget xidget;
  private Map<StatefulContext, Row> map;
}
