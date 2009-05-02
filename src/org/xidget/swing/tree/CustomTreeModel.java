/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.tree;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.xidget.IXidget;
import org.xidget.table.Row;

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
  private Object[] createPath( Row row)
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
    Object[] path = createPath( parent);
    int[] indices = new int[ rows.length];
    for( int i=0; i<rows.length; i++) indices[ i] = rowIndex + i;
    
    if ( parent == root)
      fireTreeStructureChanged( this, path, indices, rows);
    else
      fireTreeNodesInserted( this, path, indices, rows);
  }
  
  /**
   * Remove frows from the tree.
   * @param parent The parent row.
   * @param rowIndex The index where the rows will be removed.
   * @param rows The rows that were removed.
   */
  public void removeRows( Row parent, int rowIndex, Row[] rows)
  {
    List<Row> children = (parent == null)? root.getChildren(): parent.getChildren();
    
    Object[] path = createPath( parent);
    int[] indices = new int[ rows.length];
    for( int i=0, j=rowIndex; i<rows.length; i++, j++) 
      indices[ i] = j;

    if ( parent == root)
      fireTreeStructureChanged( this, path, indices, rows);
    else
      fireTreeNodesRemoved( this, path, indices, rows);
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
    List<Row> children = (row.getParent() == null)? root.getChildren(): row.getParent().getChildren();
    int rowIndex = children.indexOf( row);
    
    Object[] path = createPath( row);
    fireTreeNodesChanged( this, path, new int[] { rowIndex}, new Row[] { row});
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
   * @param childIndices the indices of the affected elements
   * @param children the affected elements
   */
  protected void fireTreeStructureChanged( Object source, Object[] path, int[] childIndices, Object[] children) 
  {
    if ( listeners.size() == 0) return;
    
    TreeModelEvent event = new TreeModelEvent( source, path, childIndices, children);
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

  private Row root;
  private List<TreeModelListener> listeners; 
}