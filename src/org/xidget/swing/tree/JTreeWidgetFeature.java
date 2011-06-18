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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.xidget.IXidget;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.ISelectionWidgetFeature;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.tree.Row;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITreeWidgetFeature for a Swing JTree widget.
 */
public class JTreeWidgetFeature implements ITreeWidgetFeature, ISelectionWidgetFeature
{
  public JTreeWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.contexts = new HashMap<StatefulContext, Row>();
    
    // only create index if selection element is present
    if ( xidget.getConfig().getFirstChild( "selection") != null)
    {
      index = new HashMap<Object, Row>();
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeWidgetFeature#insertRows(org.xidget.table.Row, int, org.xidget.table.Row[])
   */
  public void insertRows( Row parent, int rowIndex, Row[] rows)
  {
    // update context map
    for( int i=0; i<rows.length; i++) 
      contexts.put( rows[ i].getContext(), rows[ i]);

    // update index
    if ( index != null)
    {
      ISelectionModelFeature selectionModelFeature = xidget.getFeature( ISelectionModelFeature.class);
      if ( selectionModelFeature != null)
      {
        for( Row row: rows)
        {
          Object identity = selectionModelFeature.getIdentity( row.getContext().getObject());
          index.put( identity, row);
        }
      }
    }
    
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
    
    // update index
    if ( index != null)
    {
      ISelectionModelFeature selectionModelFeature = xidget.getFeature( ISelectionModelFeature.class);
      if ( selectionModelFeature != null)
      {
        for( Row row: rows)
        {
          Object identity = selectionModelFeature.getIdentity( row.getContext().getObject());
          index.remove( identity);
        }
      }
    }
    
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
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#insertSelected(int, java.lang.Object)
   */
  public void insertSelected( int index, Object object)
  {
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel model = (CustomTreeModel)jtree.getModel();
    
    ISelectionModelFeature selectionModelFeature = xidget.getFeature( ISelectionModelFeature.class);
    Object identity = selectionModelFeature.getIdentity( object);
    Row row = this.index.get( identity);
    
    TreePath path = new TreePath( model.createPath( row));
    jtree.addSelectionPath( path);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#removeSelected(int, java.lang.Object)
   */
  public void removeSelected( int index, Object object)
  {
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel model = (CustomTreeModel)jtree.getModel();
    
    ISelectionModelFeature selectionModelFeature = xidget.getFeature( ISelectionModelFeature.class);
    Object identity = selectionModelFeature.getIdentity( object);
    Row row = this.index.get( identity);
    
    TreePath path = new TreePath( model.createPath( row));
    jtree.removeSelectionPath( path);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#setSelection(java.util.List)
   */
  public void setSelection( List<? extends Object> objects)
  {
    if ( index == null) return;
    
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel model = (CustomTreeModel)jtree.getModel();

    TreePath[] paths = new TreePath[ objects.size()];
    ISelectionModelFeature selectionModelFeature = xidget.getFeature( ISelectionModelFeature.class);
    for( int i=0; i<objects.size(); i++)
    {
      Object identity = selectionModelFeature.getIdentity( objects.get( i));
      Row row = index.get( identity);
      if ( row == null) row = searchModel( identity);
      paths[ i] = new TreePath( model.createPath( row));
    }

    for( TreePath path: paths) jtree.expandPath( path);
    jtree.setSelectionPaths( paths);
  }
  
  /**
   * Search the model for the specified key.
   * @param identity The key.
   * @return Returns null or the row for that key.
   */
  private Row searchModel( Object identity)
  {
    ISelectionModelFeature selectionModelFeature = xidget.getFeature( ISelectionModelFeature.class);
    ITreeExpandFeature expandFeature = xidget.getFeature( ITreeExpandFeature.class);
    
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel model = (CustomTreeModel)jtree.getModel();
    Row root = (Row)model.getRoot();
    
    Stack<Row> stack = new Stack<Row>();
    stack.push( root);
    while( !stack.empty())
    {
      Row row = stack.pop();
      
      if ( selectionModelFeature.getIdentity( row.getContext().getObject()) == identity)
        return row;
      
      expandFeature.expand( row);
      for( Row child: row.getChildren())
        stack.push( child);
    }
    
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#getSelection()
   */
  public List<? extends Object> getSelection()
  {
    JTree jtree = xidget.getFeature( JTree.class);
    TreePath[] paths = jtree.getSelectionPaths();
    List<IModelObject> nodes = new ArrayList<IModelObject>( paths.length);
    for( int i=0; i<paths.length; i++)
    {
      Row row = (Row)paths[ i].getLastPathComponent();
      nodes.add( row.getContext().getObject());
    }
    return nodes;
  }
  
  private IXidget xidget;
  private Map<StatefulContext, Row> contexts;
  private Map<Object, Row> index;
}
