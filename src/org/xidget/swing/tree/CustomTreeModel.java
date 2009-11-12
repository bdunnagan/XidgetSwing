/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * CustomTreeModel.java
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

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.xidget.IXidget;
import org.xidget.tree.Row;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A custom Swing TreeModel which is backed by the table/tree data-structures.
 */
@SuppressWarnings("unused")
public class CustomTreeModel implements TreeModel
{
  public CustomTreeModel( IXidget xidget)
  {
    listeners = new ArrayList<TreeModelListener>( 3);
    root = new Row( xidget);
    root.setExpanded( true);
  }
  
  /* (non-Javadoc)
   * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
   */
  public void addTreeModelListener( TreeModelListener listener)
  {
    listeners.add( listener);
  }

  /* (non-Javadoc)
   * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
   */
  public void removeTreeModelListener( TreeModelListener listener)
  {
    listeners.remove( listener);
  }

  /**
   * Returns the path for the specified row.
   * @param row The row.
   * @return Returns the path for the specified row.
   */
  public Object[] createPath( Row row)
  {
    List<Object> path = new ArrayList<Object>();
    if ( row != null)
    {
      for( ; row != null; row = row.getParent()) path.add( 0, row);
    }
    else
    {
      path.add( 0, root);
    }
    return path.toArray();
  }
  
  /**
   * Insert rows into the tree.
   * @param parent The parent row.
   * @param rowIndex The index where the rows will be inserted.
   * @param rows The rows to be inserted.
   */
  public void insertRows( Row parent, int rowIndex, Row[] rows)
  {
    if ( swingWillHaveCollapsed) expandOnCommit = true;
    swingWillHaveCollapsed = false;
    
    int[] indices = new int[ rows.length];
    for( int i=0; i<rows.length; i++) 
      indices[ i] = rowIndex + i;
    
    Object[] path = createPath( parent);
    fireTreeNodesInserted( this, path, indices, rows);
  }
  
  /**
   * Remove frows from the tree.
   * @param parent The parent row.
   * @param rowIndex The index where the rows will be removed.
   * @param rows The rows that were removed.
   * @param dummy True if the dummy/temporary node is being removed.
   */
  public void removeRows( Row parent, int rowIndex, Row[] rows, boolean dummy)
  {
    if ( dummy) swingWillHaveCollapsed = true;
    
    int[] indices = new int[ rows.length];
    for( int i=0; i<rows.length; i++) 
      indices[ i] = rowIndex + i;
    
    Object[] path = createPath( parent);
    fireTreeNodesRemoved( this, path, indices, rows);
  }

  /**
   * Fire events to commit previous changes to the row-set of the specified parent.
   * @param parent The parent row.
   */
  public void commit( Row parent)
  {
    //
    // Swing collapses the node if all children are removed even if some children are added afterward.
    // So we need to make sure the row is expanded in this case.  In addition, Swing does not expand
    // the root node, so we need to make sure it is expanded.
    //
    if ( parent.isExpanded() && (parent.getParent() == null || expandOnCommit))
    {
      ExpandRunnable runnable = new ExpandRunnable();
      runnable.row = parent;
      SwingUtilities.invokeLater( runnable);
      swingWillHaveCollapsed = false;
      expandOnCommit = false;
    }
  }
  
  /* (non-Javadoc)
   * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
   */
  public Object getChild( Object parent, int index)
  {
    List<Row> children = (parent == null)? root.getChildren(): ((Row)parent).getChildren();
    return children.get( index);
  }

  /* (non-Javadoc)
   * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
   */
  public int getChildCount( Object parent)
  {
    List<Row> children = (parent == null)? root.getChildren(): ((Row)parent).getChildren();
    if ( children == null) return 0;
    return children.size();
  }

  /* (non-Javadoc)
   * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
   */
  public int getIndexOfChild( Object parent, Object child)
  {
    List<Row> children = (parent == null)? root.getChildren(): ((Row)parent).getChildren();
    if ( children == null) return -1;
    return children.indexOf( (Row)child);
  }

  /* (non-Javadoc)
   * @see javax.swing.tree.TreeModel#getRoot()
   */
  public Object getRoot()
  {
    return root;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
   */
  public boolean isLeaf( Object parent)
  {
    StatefulContext context = ((Row)parent).getContext();
    if ( context != null && context.getObject().isDirty()) return false;
    
    List<Row> children = (parent == null)? root.getChildren(): ((Row)parent).getChildren();
    return children == null || children.size() == 0;
  }

  /* (non-Javadoc)
   * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
   */
  public void valueForPathChanged( TreePath path, Object newValue)
  {
    // xidget uses remove/insert combination
  }

  /**
   * Update the cells of the specified row.
   * @param row The row.
   */
  public void updateCells( Row row)
  {
//    List<Row> children = (row.getParent() == null)? root.getChildren(): row.getParent().getChildren();
//    int rowIndex = children.indexOf( row);
//    
//    Object[] path = createPath( row);
//    fireTreeNodesChanged( this, path, new int[] { rowIndex}, new Row[] { row});
    
    Object[] path = createPath( row);
    fireTreeNodesChanged( this, path, new int[] {}, new Row[] {});
  }
  
  /**
   * Notifies all listeners that have registered interest for notification on this event type.
   * @param source the node being changed
   * @param path the path to the root node
   * @param childIndices the indices of the changed elements
   * @param children the changed elements
   */
  protected void fireTreeNodesChanged( Object source, Object[] path, int[] childIndices, Object[] children) 
  {
    if ( listeners.size() == 0) return;
    
    TreeModelEvent event = new TreeModelEvent( source, path, childIndices, children);
    TreeModelListener[] array = listeners.toArray( new TreeModelListener[ 0]);
    for( TreeModelListener listener: array) listener.treeNodesChanged( event);
  }

  /**
   * Notifies all listeners that have registered interest for notification on this event type.
   * @param source the node where new elements are being inserted
   * @param path the path to the root node
   * @param childIndices the indices of the new elements
   * @param children the new elements
   */
  protected void fireTreeNodesInserted( Object source, Object[] path, int[] childIndices, Object[] children) 
  {
    if ( listeners.size() == 0) return;
    
    TreeModelEvent event = new TreeModelEvent( source, path, childIndices, children);
    TreeModelListener[] array = listeners.toArray( new TreeModelListener[ 0]);
    for( TreeModelListener listener: array) listener.treeNodesInserted( event);
  }

  /**
   * Notifies all listeners that have registered interest for notification on this event type.
   * @param source the node where elements are being removed
   * @param path the path to the root node
   * @param childIndices the indices of the removed elements
   * @param children the removed elements
   */
  protected void fireTreeNodesRemoved( Object source, Object[] path, int[] childIndices, Object[] children) 
  {
    if ( listeners.size() == 0) return;
    
    TreeModelEvent event = new TreeModelEvent( source, path, childIndices, children);
    TreeModelListener[] array = listeners.toArray( new TreeModelListener[ 0]);
    for( TreeModelListener listener: array) listener.treeNodesRemoved( event);
  }

  /**
   * Notifies all listeners that have registered interest for notification on this event type.
   * @param source the node where the tree model has changed
   * @param path the path to the root node
   */
  protected void fireTreeStructureChanged( Object source, Object[] path) 
  {
    if ( listeners.size() == 0) return;
    
    TreeModelEvent event = new TreeModelEvent( source, path);
    TreeModelListener[] array = listeners.toArray( new TreeModelListener[ 0]);
    for( TreeModelListener listener: array) listener.treeStructureChanged( event);
  }

  /**
   * Notifies all listeners that have registered interest for notification on this event type.
   * @param source the node where the tree model has changed
   * @param path the path to the root node
   */
  private void fireTreeStructureChanged( Object source, TreePath path) 
  {
    if ( listeners.size() == 0) return;
    
    TreeModelEvent event = new TreeModelEvent( source, path);
    TreeModelListener[] array = listeners.toArray( new TreeModelListener[ 0]);
    for( TreeModelListener listener: array) listener.treeStructureChanged( event);
  }

  private class ExpandRunnable implements Runnable
  {
    public Row row;
    
    public void run()
    {
      Object[] path = createPath( row);
      JTree jTree = root.getTable().getFeature( JTree.class);
      jTree.expandPath( new TreePath( path));
    }
  }
  
  private Row root;
  private List<TreeModelListener> listeners;
  private boolean swingWillHaveCollapsed;
  private boolean expandOnCommit;
}