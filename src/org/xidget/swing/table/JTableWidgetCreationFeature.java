/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.xidget.IXidget;
import org.xidget.ifeature.IDragAndDropFeature;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xidget.tree.Cell;
import org.xidget.tree.Row;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IWidgetCreationFeature for creating a JTable.
 */
public class JTableWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public JTableWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    CustomTableModel tableModel = new CustomTableModel( xidget);
    
    jtable = new JTable( tableModel);
    jtable.setDragEnabled( true);
    try
    {
      jtable.getDropTarget().addDropTargetListener( dndListener);
    }
    catch( Exception e)
    {
      e.printStackTrace( System.err);
    }
    
    jtable.setShowGrid( true);
    jtable.setShowHorizontalLines( true);
    jtable.setShowVerticalLines( true);
    jtable.setGridColor( Color.LIGHT_GRAY);
    
    jtable.setDefaultRenderer( IModelObject.class, new CustomCellRenderer());
    jtable.setDefaultEditor( IModelObject.class, new CustomCellEditor());
    jtable.getTableHeader().setDefaultRenderer( new CustomHeaderCellRenderer());
    jtable.getSelectionModel().addListSelectionListener( selectionListener);
    
    jscrollPane = new JScrollPane( jtable);    
    return jscrollPane;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jscrollPane, jtable};
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
   * Returns the JTable widget.
   * @return Returns the JTable widget.
   */
  public JTable getJTable()
  {
    return jtable;
  }
  
  /**
   * Update the selection in the model.
   */
  public void updateSelection()
  {
    IWidgetContextFeature widgetContextFeature = xidget.getFeature( IWidgetContextFeature.class);
    StatefulContext context = widgetContextFeature.getContext( jtable);

    int[] selected = jtable.getSelectedRows();
    Arrays.sort( selected);
    
    CustomTableModel model = (CustomTableModel)jtable.getModel();
    List<Row> rows = model.getRows();
    
    // global table selection
    List<IModelObject> tableElements = new ArrayList<IModelObject>();
    for( int i=0; i<selected.length; i++)
    {
      Row row = rows.get( selected[ i]);
      tableElements.add( row.getContext().getObject());
    }
    
    ISelectionModelFeature selectionModelFeature = xidget.getFeature( ISelectionModelFeature.class);
    selectionModelFeature.setSelection( context, tableElements);
    
    // table group selections
    List<IModelObject> groupElements = new ArrayList<IModelObject>();
    for( IXidget group: xidget.getChildren())
    {
      selectionModelFeature = group.getFeature( ISelectionModelFeature.class);
      if ( selectionModelFeature != null)
      {
        extractGroupSelection( rows, group, selected, groupElements);
        selectionModelFeature.setSelection( context, groupElements);
      }
    }
  }

  /**
   * Returns the row objects that are selected within the specified table group.
   * @param group The table group.
   * @param selected The current selection indices.
   * @param result Returns the row objects that are selected within the specified table group.
   */
  private void extractGroupSelection( List<Row> rows, IXidget group, int[] selected, List<IModelObject> result)
  {
    result.clear();
    for( int i=0; i<selected.length; i++)
    {
      Row row = rows.get( selected[ i]);
      if ( row.getTable() == group)
        result.add( row.getContext().getObject());
    }
  }
  
  private ListSelectionListener selectionListener = new ListSelectionListener() {
    public void valueChanged( ListSelectionEvent event)
    {
      if ( updating) return;
      updating = true;      
      try { updateSelection();} finally { updating = false;}
    }
    
    private boolean updating;
  };
  
  /**
   * Returns the row beneath the specified display location.
   * @param point The display location.
   * @return Returns null or the row.
   */
  private Row getRowAt( Point point)
  {
    int rIndex = jtable.rowAtPoint( point);
    if ( rIndex < 0) return null;
    
    CustomTableModel model = (CustomTableModel)jtable.getModel();
    List<Row> rows = model.getRows();
    if ( rIndex >= rows.size()) return null;
    
    return rows.get( rIndex);
  }
  
  /**
   * Create a drop context and populate the appropriate variables.
   * @param location The current drag or drop location.
   * @return Returns the new drop context.
   */
  private StatefulContext createDropContext( Point location)
  {
    int rIndex = jtable.rowAtPoint( location);
    int cIndex = jtable.columnAtPoint( location);
    
    Row row = getRowAt( location);
    StatefulContext rowContext = (row != null)? row.getContext(): null;
    StatefulContext dropContext = new StatefulContext( rowContext, rowContext.getObject().getParent());
    
    dropContext.set( "row", rowContext.getObject());
    dropContext.set( "rowIndex", rIndex);
    dropContext.set( "columnIndex", cIndex);
    
    Rectangle cellBounds = jtable.getCellRect( rIndex, cIndex, false);
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
    Row row = getRowAt( location);
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
      Row row = getRowAt( event.getLocation());
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
  private JTable jtable;
}