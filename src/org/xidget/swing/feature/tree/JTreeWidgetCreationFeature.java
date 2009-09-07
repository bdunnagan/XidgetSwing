/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.tree;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import java.util.List;
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
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xidget.swing.tree.CustomTreeCellRenderer;
import org.xidget.swing.tree.CustomTreeModel;
import org.xidget.tree.Cell;
import org.xidget.tree.Row;
import org.xmodel.IModelObject;
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
    
    if ( xidget.getFeature( ISelectionModelFeature.class) != null)
      jtree.addTreeSelectionListener( selectionListener);
    
    jscrollPane = new JScrollPane( jtree);
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
   * Returns the row objects that are selected within the specified table group.
   * @param rows The selected rows.
   * @param group The table group.
   * @param result Returns the row objects that are selected within the specified table group.
   */
  private void extractGroupSelection( Row[] rows, IXidget group, List<IModelObject> result)
  {
    result.clear();
    for( Row row: rows)
    {
      if ( row.getTable() == group)
        result.add( row.getContext().getObject());
    }
  }
  
  /**
   * Update the selection in the selection model.
   */
  private void updateSelection()
  {
    IWidgetContextFeature widgetContextFeature = xidget.getFeature( IWidgetContextFeature.class);
    StatefulContext context = widgetContextFeature.getContext( jtree);

    // TODO: tree selection is not ordered and causes unnecessary change records
    TreePath[] paths = jtree.getSelectionPaths();
    Row[] rows = new Row[ paths.length];
    for( int i=0; i<paths.length; i++)
      rows[ i] = (Row)paths[ i].getLastPathComponent();
    
    // global selection
    List<IModelObject> allElements = new ArrayList<IModelObject>();
    if ( paths != null)
      for( int i=0; i<paths.length; i++)
        allElements.add( rows[ i].getContext().getObject());
    
    ISelectionModelFeature selectionModelFeature = xidget.getFeature( ISelectionModelFeature.class);
    selectionModelFeature.setSelection( context, allElements);
    
    // table group selections
    List<IModelObject> groupElements = new ArrayList<IModelObject>();
    for( IXidget group: xidget.getChildren())
    {
      selectionModelFeature = group.getFeature( ISelectionModelFeature.class);
      if ( selectionModelFeature != null)
      {
        extractGroupSelection( rows, group, groupElements);
        selectionModelFeature.setSelection( context, groupElements);
      }
    }
  }
  
  private TreeSelectionListener selectionListener = new TreeSelectionListener() {
    public void valueChanged( TreeSelectionEvent event)
    {
      if ( updating) return;
      updating = true;
      try { updateSelection();} finally { updating = false;}
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
    TreePath path = jtree.getPathForLocation( location.x, location.y);
    Row row = (Row)path.getLastPathComponent();
    int rIndex = row.getParent().getChildren().indexOf( row);
    int cIndex = 0; // limitation of Swing JTree
    
    StatefulContext rowContext = (row != null)? row.getContext(): null;
    StatefulContext dropContext = new StatefulContext( rowContext, xidget.getConfig());
    
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
    TreePath path = jtree.getPathForLocation( location.x, location.y);
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
      TreePath path = jtree.getPathForLocation( location.x, location.y);
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
