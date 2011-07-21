/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.tree;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.xidget.IXidget;
import org.xidget.ifeature.model.IPartialSelectionWidgetFeature;
import org.xidget.ifeature.model.ISelectionWidgetFeature;
import org.xidget.tree.Row;

/**
 * An implementation of ISelectionWidgetFeature for JTree.
 */
public class JTreeSelectionWidgetFeature implements ISelectionWidgetFeature, IPartialSelectionWidgetFeature
{
  @SuppressWarnings("serial")
  public JTreeSelectionWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    
    // performance caches
    rowCache = new LinkedHashMap<Object, Row>( 16, 0.75f, true) {
      protected boolean removeEldestEntry( Entry<Object, Row> eldest)
      {
        return size() > 100;
      }
    };
    pathCache = new LinkedHashMap<Row, TreePath>( 16, 0.75f, true) {
      protected boolean removeEldestEntry( Entry<Row, TreePath> eldest)
      {
        return size() > 100;
      }
    };
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#select(java.lang.Object)
   */
  @Override
  public void select( Object object)
  {
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel model = (CustomTreeModel)jtree.getModel();
    TreePath path = getPath( model, object);
    if ( path != null) jtree.addSelectionPath( path);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#deselect(java.lang.Object)
   */
  @Override
  public void deselect( Object object)
  {
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel model = (CustomTreeModel)jtree.getModel();
    TreePath path = getPath( model, object);
    if ( path != null) jtree.removeSelectionPath( path);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#setSelection(java.util.List)
   */
  @Override
  public void setSelection( List<? extends Object> list)
  {
    JTree jtree = xidget.getFeature( JTree.class);
    jtree.clearSelection();
    
    CustomTreeModel model = (CustomTreeModel)jtree.getModel();
    for( Object object: list)
    {
      TreePath path = getPath( model, object);
      if ( path != null) jtree.addSelectionPath( path);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#getSelection()
   */
  @Override
  public List<? extends Object> getSelection()
  {
    List<Object> list = new ArrayList<Object>();
    
    JTree jtree = xidget.getFeature( JTree.class);
    TreePath[] paths = jtree.getSelectionPaths();
    if ( paths != null)
    {
      for( TreePath path: paths)
      {
        Row row = (Row)path.getLastPathComponent();
        list.add( row.getContext().getObject());
      }
    }
    
    return list;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IPartialSelectionWidgetFeature#select(org.xidget.IXidget, java.lang.Object)
   */
  @Override
  public void select( IXidget origin, Object object)
  {
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel model = (CustomTreeModel)jtree.getModel();
    TreePath path = getPath( origin, model, object);
    if ( path != null) jtree.addSelectionPath( path);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IPartialSelectionWidgetFeature#deselect(org.xidget.IXidget, java.lang.Object)
   */
  @Override
  public void deselect( IXidget origin, Object object)
  {
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel model = (CustomTreeModel)jtree.getModel();
    TreePath path = getPath( origin, model, object);
    if ( path != null) jtree.removeSelectionPath( path);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IPartialSelectionWidgetFeature#setSelection(org.xidget.IXidget, java.util.List)
   */
  @Override
  public void setSelection( IXidget origin, List<? extends Object> list)
  {
    List<? extends Object> selection = getSelection( origin);
    for( Object object: selection) deselect( object);
    
    JTree jtree = xidget.getFeature( JTree.class);
    CustomTreeModel model = (CustomTreeModel)jtree.getModel();
    for( Object object: list)
    {
      TreePath path = getPath( origin, model, object);
      if ( path != null) jtree.addSelectionPath( path);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IPartialSelectionWidgetFeature#getSelection(org.xidget.IXidget)
   */
  @Override
  public List<? extends Object> getSelection( IXidget origin)
  {
    List<Object> list = new ArrayList<Object>();
    
    JTree jtree = xidget.getFeature( JTree.class);
    TreePath[] paths = jtree.getSelectionPaths();
    if ( paths != null)
    {
      for( TreePath path: paths)
      {
        Row row = (Row)path.getLastPathComponent();
        if ( row.getTable().getParent() == origin)
        {
          list.add( row.getContext().getObject());
        }
      }
    }
    
    return list;
  }

  /**
   * Constructs the TreePath for the specified object.
   * @param model The model.
   * @param object The object.
   * @return Returns the TreePath.
   */
  private TreePath getPath( CustomTreeModel model, Object object)
  {
    return getPath( null, model, object);
  }
  
  /**
   * Constructs the TreePath for the specified object.
   * @param xidget Null or the tree xidget to which the selection is localized.
   * @param model The model.
   * @param object The object.
   * @return Returns null or the TreePath.
   */
  private TreePath getPath( IXidget xidget, CustomTreeModel model, Object object)
  {
    //
    // Tree may contain duplicate objects, so make sure the cached row 
    // belongs to the correct tree xidget if specified.
    //
    Row row = rowCache.get( object);
    if ( row == null || (xidget != null && row.getTable().getParent() != xidget))
    {
      row = model.findObject( xidget, object);
      if ( row == null) return null;
      rowCache.put( object, row);
    }

    Row parent = row.getParent();
    if ( parent == null)
    {
      return new TreePath( model.createPath( row));
    }
    else
    {
      TreePath path = pathCache.get( parent);
      if ( path == null)
      {
        path = new TreePath( model.createPath( parent));
        pathCache.put( parent, path);
      }
      
      return path.pathByAddingChild( row);
    }
  }
  
  private IXidget xidget;
  private Map<Row, TreePath> pathCache;
  private Map<Object, Row> rowCache;
}
