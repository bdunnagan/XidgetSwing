/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * CustomCellEditor.java
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
package org.xidget.swing.table;

import java.awt.Component;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;
import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.model.ISingleValueWidgetFeature;
import org.xidget.tree.Cell;
import org.xidget.tree.Row;
import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An custom TableCellEditor that chooses the type of editor depending on the content of the cell
 * being edited.
 */
@SuppressWarnings("serial")
public class CustomCellEditor extends AbstractCellEditor implements TableCellEditor
{
  /**
   * Create a CustomCellEditor for the specified table xidget. This class handles editing for
   * all cells in the table.  It activates the appropriate editor child xidget for the cell
   * that is being edited.
   */
  public CustomCellEditor()
  {
    addCellEditorListener( listener);
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
    if ( cell == null) return null;

    editor = findEditor( row, columnIndex);
    if ( editor == null) return null;
    
    //
    // WORKAROUND:
    // Swing isn't firing the edit cancelled event which means that the widget
    // stays bounds after editing is complete and might need to be unbound before
    // we start editing again.
    //
    IBindFeature bindFeature = editor.getFeature( IBindFeature.class);
    StatefulContext[] array = bindFeature.getBoundContexts().toArray( new StatefulContext[ 0]);
    for( StatefulContext context: array) bindFeature.unbind( context);
    
    // bind editor
    editorSource = (cell.source != null)? cell.source: new ModelObject( "null"); 
    editorContext = new StatefulContext( row.getContext(), editorSource.cloneTree());
    editorContext.set( "row", row.getContext().getObject());
    bindFeature.bind( editorContext);
    
    // return widget
    IWidgetCreationFeature creationFeature = editor.getFeature( IWidgetCreationFeature.class);
    Object[] widgets = creationFeature.getLastWidgets();
    if ( widgets.length == 0) return null;

    JComponent component = (JComponent)widgets[ 0];

    // remove borders from any widget
    component.setBorder( null);
    
    // special treatment for each editor
    if ( component instanceof JComboBox)
    {
      ((JComboBox)component).putClientProperty( "JComboBox.isTableCellEditor", Boolean.TRUE);
    }
    else if ( component instanceof JTextComponent)
    {
      ((JTextComponent)component).selectAll();
    }
    else if ( component instanceof AbstractButton)
    {
      AbstractButton button = ((AbstractButton)component);
      button.setContentAreaFilled( false);
    }
    else
    {
      throw new UnsupportedOperationException( "The specified editor widget is not supported.");
    }
    
    return component;
  }

  /* (non-Javadoc)
   * @see javax.swing.CellEditor#getCellEditorValue()
   */
  public Object getCellEditorValue()
  {
    ISingleValueWidgetFeature feature = editor.getFeature( ISingleValueWidgetFeature.class);
    return feature.getValue();
  }
  
  /**
   * Cleanup the editor when editing is finished.
   */
  private void cleanupEditor()
  {
    if ( editorContext != null)
    {
      IBindFeature bindFeature = editor.getFeature( IBindFeature.class);
      bindFeature.unbind( editorContext);
      editorContext = null;
      editorSource = null;
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
  public static IXidget findEditor( Row row, int columnIndex)
  {
    IXidget table = row.getTable();
    IModelObject tableConfig = table.getConfig();
    IModelObject cellConfig = tableConfig.getChildren( "cell").get( columnIndex);
    
    for( IXidget child: table.getChildren())
    {
      IModelObject childConfig = child.getConfig();
      
      // editors are the only xidgets defined within a cell
      if ( childConfig.getParent().isType( "cell") && cellConfig == childConfig.getParent())
        return child;
    }
    
    return null;
  }

  private CellEditorListener listener = new CellEditorListener() {
    public void editingCanceled( ChangeEvent e)
    {
      cleanupEditor();
    }
    public void editingStopped( ChangeEvent e)
    {
      editorSource.setValue( editorContext.getObject().getValue());
      cleanupEditor();
    }
  };

  private IXidget editor;
  private IModelObject editorSource;
  private StatefulContext editorContext;
}
