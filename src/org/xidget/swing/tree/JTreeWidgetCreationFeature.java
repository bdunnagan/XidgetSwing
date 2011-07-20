/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JTreeWidgetCreationFeature.java
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

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.xidget.IXidget;
import org.xidget.ifeature.IDragAndDropFeature;
import org.xidget.ifeature.model.ISelectionUpdateFeature;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xidget.tree.Cell;
import org.xidget.tree.Row;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IWidgetCreationFeature for creating a Swing JTree widget.
 */
public class JTreeWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public JTreeWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    jtree = new JTree( new CustomTreeModel( xidget));
    jtree.setDragEnabled( true);
    try
    {
      jtree.setDropTarget( new DropTarget());
      jtree.getDropTarget().addDropTargetListener( dndListener);
    }
    catch( Exception e)
    {
      e.printStackTrace( System.err);
    }
    
    jtree.setCellRenderer( new CustomTreeCellRenderer());
    jtree.setShowsRootHandles( true);
    jtree.setRootVisible( false);
    jtree.putClientProperty( "JTree.lineStyle", "Angled");
    jtree.addTreeExpansionListener( expandListener);
    jtree.addTreeSelectionListener( selectionListener);
    
    jscrollPane = new JScrollPane( jtree);
    jscrollPane.setBorder( BorderFactory.createEmptyBorder());
    return jscrollPane;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jscrollPane, jtree};
  }

  /**
   * Returns the scroll pane that holds the table.
   * @return Returns the scroll pane that holds the table.
   */
  public JScrollPane getJScrollPane()
  {
    return jscrollPane;
  }

  /**
   * Returns the JTree widget.
   * @return Returns the JTree widget.
   */
  public JTree getJTree()
  {
    return jtree;
  }

  private TreeExpansionListener expandListener = new TreeExpansionListener() {
    public void treeExpanded( TreeExpansionEvent event)
    {
      TreePath path = event.getPath();
      Row row = (Row)path.getLastPathComponent();

      ITreeExpandFeature feature = xidget.getFeature( ITreeExpandFeature.class);
      feature.expand( row);
    }
    
    public void treeCollapsed( TreeExpansionEvent event)
    {
      TreePath path = event.getPath();
      Row row = (Row)path.getLastPathComponent();

      ITreeExpandFeature feature = xidget.getFeature( ITreeExpandFeature.class);
      feature.collapse( row);
    }
  };
  
  /**
   * Returns the next consecutive selection paths belonging to the same tree xidget. 
   * @param event The selection event.
   * @param paths The selected paths.
   * @param start The starting index.
   * @return Returns the list of selected objects.
   */
  private List<Object> getNextSelectionGroup( TreeSelectionEvent event, List<TreePath> paths, int start)
  {
    List<Object> list = new ArrayList<Object>();
    Row row = (Row)paths.get( start).getLastPathComponent();
    IXidget tree = row.getTree( xidget);
    for( int i=start; i<paths.size(); i++)
    {
      row = (Row)paths.get( i).getLastPathComponent();
      if ( tree != row.getTree( xidget)) break;
      list.add( row.getContext().getObject());
    }
    return list;
  }
  
  private TreeSelectionListener selectionListener = new TreeSelectionListener() {
    public void valueChanged( TreeSelectionEvent event)
    {
      if ( updating) return;
      updating = true;
      
      try 
      { 
        ISelectionUpdateFeature feature = xidget.getFeature( ISelectionUpdateFeature.class);
        
        TreePath[] paths = event.getPaths();
        List<TreePath> inserted = new ArrayList<TreePath>();
        List<TreePath> deleted = new ArrayList<TreePath>();
        for( int i=0; i<paths.length; i++)
        {
          if ( event.isAddedPath( i))
          {
            inserted.add( paths[ i]);
          }
          else
          {
            deleted.add( paths[ i]);
          }
        }
        
        for( int index = 0; index < deleted.size(); )
        {
          Row first = (Row)deleted.get( index).getLastPathComponent();
          IXidget tree = first.getTree( xidget);

          // next block of paths belonging to the same tree
          List<Object> list = getNextSelectionGroup( event, deleted, index);
          
          // notify global selection
          feature.modelDeselect( list);
          
          // notify local selection
          if ( tree != xidget)
          {
            ISelectionUpdateFeature nestedFeature = tree.getFeature( ISelectionUpdateFeature.class);
            nestedFeature.modelDeselect( list);
          }
          
          index += list.size();
        }
        
        for( int index = 0; index < inserted.size(); )
        {
          Row first = (Row)inserted.get( index).getLastPathComponent();
          IXidget tree = first.getTree( xidget);

          // next block of paths belonging to the same tree
          List<Object> list = getNextSelectionGroup( event, inserted, index);
          
          // notify global selection
          feature.modelSelect( list);
          
          // notify local selection
          if ( tree != xidget)
          {
            ISelectionUpdateFeature nestedFeature = tree.getFeature( ISelectionUpdateFeature.class);
            nestedFeature.modelSelect( list);
          }
          
          index += list.size();
        }
      } 
      finally 
      { 
        updating = false;
      }
    }
    
    private boolean updating;
  };
  
  /**
   * Create a drop context and populate the appropriate variables.
   * @param location The current drag or drop location.
   * @return Returns the new drop context.
   */
  private StatefulContext createDropContext( Point location)
  {
    TreePath path = jtree.getClosestPathForLocation( location.x, location.y);
    if ( path == null) return null;
    
    Row row = (Row)path.getLastPathComponent();
    int rIndex = row.getParent().getChildren().indexOf( row);
    int cIndex = 0; // limitation of Swing JTree
    
    StatefulContext rowContext = (row != null)? row.getContext(): null;
    StatefulContext dropContext = new StatefulContext( rowContext, rowContext.getObject().getParent());
    
    dropContext.set( "row", rowContext.getObject());
    dropContext.set( "rowIndex", rIndex);
    dropContext.set( "columnIndex", cIndex);
    
    Rectangle cellBounds = jtree.getPathBounds( path);
    int middle = cellBounds.y + cellBounds.height / 2;
    int insert = (location.y < middle)? rIndex: rIndex+1;
    dropContext.set( "insert", insert);
    
    Cell cell = row.getCell( cIndex);
    dropContext.set( "cell", (cell != null)? cell.source: null);
    
    return dropContext;
  }
  
  /**
   * Returns true if a drop at the specified location is allowed.
   * @param location The location.
   * @return Returns true if a drop at the specified location is allowed.
   */
  private boolean canDrop( Point location)
  {
    StatefulContext dropContext = createDropContext( location);
    
    // local definition takes precedence
    TreePath path = jtree.getClosestPathForLocation( location.x, location.y);
    if ( path == null) return false;
    
    Row row = (Row)path.getLastPathComponent();
    if ( row != null)
    {
      IDragAndDropFeature dndFeature = row.getTable().getFeature( IDragAndDropFeature.class);
      if ( dndFeature != null && dndFeature.isDropEnabled()) return dndFeature.canDrop( dropContext); 
    }
    
    // global definition
    IDragAndDropFeature dndFeature = xidget.getFeature( IDragAndDropFeature.class);
    if ( dndFeature != null) return dndFeature.canDrop( dropContext);
    
    return false;
  }
  
  private DropTargetListener dndListener = new DropTargetAdapter() {
    public void dragEnter( DropTargetDragEvent event)
    {
      if ( canDrop( event.getLocation())) 
        event.acceptDrag( DnDConstants.ACTION_COPY); 
      else 
        event.rejectDrag();
    }
    public void dragOver( DropTargetDragEvent event)
    {
      if ( canDrop( event.getLocation())) 
        event.acceptDrag( DnDConstants.ACTION_COPY); 
      else 
        event.rejectDrag();
    }
    public void drop( DropTargetDropEvent event)
    {
      StatefulContext dropContext = createDropContext( event.getLocation());
      
      // local definition goes first
      Point location = event.getLocation();
      TreePath path = jtree.getClosestPathForLocation( location.x, location.y);
      Row row = (Row)path.getLastPathComponent();
      if ( row != null)
      {
        IDragAndDropFeature dndFeature = row.getTable().getFeature( IDragAndDropFeature.class);
        if ( dndFeature != null && dndFeature.isDropEnabled())
        {
          dndFeature.drop( dropContext);
          return;
        }
      }
      
      // global definition
      IDragAndDropFeature dndFeature = xidget.getFeature( IDragAndDropFeature.class);
      if ( dndFeature != null && dndFeature.isDropEnabled()) dndFeature.drop( dropContext);
    }
  };
    
  private JScrollPane jscrollPane;
  private JTree jtree;
}
