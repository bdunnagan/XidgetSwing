/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JTreeWidgetFeature.java
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
package org.xidget.swing.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.xidget.IXidget;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.tree.Row;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITreeWidgetFeature for a Swing JTree widget.
 */
public class JTreeWidgetFeature implements ITreeWidgetFeature
{
  public JTreeWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.contexts = new HashMap<StatefulContext, Row>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#insertRows(org.xidget.table.Row, int, org.xidget.table.Row[])
   */
  public void insertRows( Row parent, int rowIndex, Row[] rows)
  {
    // update context map
    for( int i=0; i<rows.length; i++) 
      contexts.put( rows[ i].getContext(), rows[ i]);
    
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
  public void removeRows( Row parent, int rowIndex, Row[] rows, boolean dummy)
  {
    // let expansion policy cleanup listeners
    ITreeExpandFeature expandFeature = xidget.getFeature( ITreeExpandFeature.class);
    for( int i=0; i<rows.length; i++)
      expandFeature.rowRemoved( rows[ i]);
    
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel treeModel = (CustomTreeModel)jtree.getModel();
    
    // update context map
    for( int i=0; i<rows.length; i++)
      contexts.remove( rows[ i].getContext());
        
    // notify widget
    treeModel.removeRows( parent, rowIndex, rows, dummy);    
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#commit(org.xidget.table.Row)
   */
  public void commit( Row parent)
  {
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel treeModel = (CustomTreeModel)jtree.getModel();
    treeModel.commit( parent);
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
    if ( !contexts.containsKey( context))
    {
      JTree jtree = xidget.getFeature( JTree.class);
      CustomTreeModel treeModel = (CustomTreeModel)jtree.getModel();
      Row root = (Row)treeModel.getRoot();
      root.setContext( context);
      contexts.put( context, root);
    }
    
    return contexts.get( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#isVisible(org.xidget.table.Row)
   */
  public boolean isVisible( Row row)
  {
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel treeModel = (CustomTreeModel)jtree.getModel();
    Object[] path = treeModel.createPath( row);
    return jtree.isVisible( new TreePath( path));
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
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#setColumnWidth(int, int)
   */
  @Override
  public void setColumnWidth( int columnIndex, int width)
  {
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
  private Map<StatefulContext, Row> contexts;
}
