/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.table;

import java.awt.Container;
import javax.swing.JComponent;
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
    // TODO: add support for widget label for jtable
    CustomTableModel tableModel = new CustomTableModel( xidget);
    jtable = new JTable( tableModel);
    jtable.setDefaultRenderer( IModelObject.class, new CustomCellRenderer());
    jtable.getTableHeader().setDefaultRenderer( new CustomHeaderCellRenderer());
    container.add( jtable);
    return jtable;
  }

  /**
   * Returns the JTable widget.
   * @return Returns the JTable widget.
   */
  public JTable getWidget()
  {
    return jtable;
  }

  private JTable jtable;
}
