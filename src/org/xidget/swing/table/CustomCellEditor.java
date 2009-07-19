/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import java.awt.Component;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.tree.Cell;
import org.xidget.tree.Row;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An custom TableCellEditor that chooses the type of editor depending on the content of the cell
 * being edited.
 */
public class CustomCellEditor extends AbstractCellEditor implements TableCellEditor
{
  /**
   * Create a CustomCellEditor for the specified table xidget. This class handles editing for
   * all cells in the table.  It activates the appropriate editor child xidget for the cell
   * that is being edited.
   * @param xidget The table xidget.
   */
  public CustomCellEditor( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
   */
  public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int rowIndex, int columnIndex)
  {
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    List<Row> rows = tableModel.getRows();
    if ( rows.size() <= rowIndex) return null;
    
    Row row = rows.get( rowIndex);
    Cell cell = row.getCell( columnIndex);
    if ( cell == null || cell.source == null) return null;

    editor = findEditor( row, columnIndex);
    
    // bind editor
    editorContext = new StatefulContext( row.getContext(), cell.source);
    IBindFeature bindFeature = editor.getFeature( IBindFeature.class);
    bindFeature.bind( editorContext);
    
    // return widget
    IWidgetCreationFeature creationFeature = editor.getFeature( IWidgetCreationFeature.class);
    Object[] widgets = creationFeature.getLastWidgets();
    if ( widgets.length == 0) return null;
    
    return (Component)widgets[ 0];
  }

  /* (non-Javadoc)
   * @see javax.swing.CellEditor#getCellEditorValue()
   */
  public Object getCellEditorValue()
  {
    return Xlate.get( editorContext.getObject(), "");
  }

  /* (non-Javadoc)
   * @see javax.swing.AbstractCellEditor#cancelCellEditing()
   */
  @Override
  public void cancelCellEditing()
  {
    super.cancelCellEditing();
    unbind();
  }

  /* (non-Javadoc)
   * @see javax.swing.AbstractCellEditor#stopCellEditing()
   */
  @Override
  public boolean stopCellEditing()
  {
    boolean result = super.stopCellEditing();
    unbind();
    return result;
  }
  
  private void unbind()
  {
    if ( editorContext != null)
    {
      IBindFeature bindFeature = editor.getFeature( IBindFeature.class);
      bindFeature.unbind( editorContext);
      editorContext = null;
    }
  }

  /**
   * Find the editor for the specified cell.  Since editors are defined for a particular cell 
   * config, it is necessary to search the sub-tables based on the config that generated the
   * row.
   * @param row The row of the cell to be edited.
   * @param columnIndex The column index of the cell to be edited.
   * @return Returns null or the editor.
   */
  private IXidget findEditor( Row row, int columnIndex)
  {
    IModelObject tableConfig = row.getTable().getConfig();
    IModelObject cellConfig = tableConfig.getChildren( "cell").get( columnIndex);
    
    for( IXidget child: xidget.getChildren())
    {
      IModelObject childConfig = child.getConfig();
      if ( childConfig.getParent() == cellConfig)
        return child;
    }
    
    return null;
  }
  
  private IXidget xidget;
  private IXidget editor;
  private StatefulContext editorContext;
}
