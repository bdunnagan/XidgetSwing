/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.table;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.xidget.IXidget;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xidget.swing.table.CustomCellRenderer;
import org.xidget.swing.table.CustomHeaderCellRenderer;
import org.xidget.swing.table.CustomTableModel;
import org.xmodel.IModelObject;

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
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget(java.awt.Container)
   */
  @Override
  protected JComponent createSwingWidget( Container container)
  {
    CustomTableModel tableModel = new CustomTableModel();
    jtable = new JTable( tableModel);
    jtable.setDefaultRenderer( IModelObject.class, new CustomCellRenderer());
    jtable.getTableHeader().setDefaultRenderer( new CustomHeaderCellRenderer());
    
    jscrollPane = new JScrollPane( jtable);
    container.add( jscrollPane);
    
    return jtable;
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

  private JScrollPane jscrollPane;
  private JTable jtable;
}
