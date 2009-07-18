/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.xidget.IXidget;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xidget.swing.table.CustomCellRenderer;
import org.xidget.swing.table.CustomHeaderCellRenderer;
import org.xidget.swing.table.CustomTableModel;
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
  
  @Override
  protected JComponent createSwingWidget()
  {
    CustomTableModel tableModel = new CustomTableModel( xidget);
    jtable = new JTable( tableModel);
    jtable.setDefaultRenderer( IModelObject.class, new CustomCellRenderer());
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
  
  private ListSelectionListener selectionListener = new ListSelectionListener() {
    public void valueChanged( ListSelectionEvent event)
    {
      if ( updating) return;
      updating = true;
      
      try
      {
        IWidgetContextFeature widgetContextFeature = xidget.getFeature( IWidgetContextFeature.class);
        StatefulContext context = widgetContextFeature.getContext( event.getSource());

        // TODO: table selection is not ordered and causes unnecessary change records
        int[] selected = jtable.getSelectedRows();
        
        CustomTableModel model = (CustomTableModel)jtable.getModel();
        List<Row> rows = model.getRows();
        List<IModelObject> elements = new ArrayList<IModelObject>();
        for( int i=0; i<selected.length; i++)
        {
          elements.add( rows.get( selected[ i]).getContext().getObject());
        }
        
        ISelectionModelFeature selectionModelFeature = xidget.getFeature( ISelectionModelFeature.class);
        selectionModelFeature.setSelection( context, elements);
      }
      finally
      {
        updating = false;
      }
    }
    
    private boolean updating;
  };

  private JScrollPane jscrollPane;
  private JTable jtable;
}
