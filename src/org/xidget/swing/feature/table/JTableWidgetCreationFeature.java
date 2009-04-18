/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.table;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JTable;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xidget.swing.ifeature.ISwingWidgetFeature;
import org.xidget.swing.table.CustomCellRenderer;
import org.xidget.swing.table.CustomHeaderCellRenderer;
import org.xidget.swing.table.CustomTableModel;
import org.xmodel.IModelObject;

/**
 * An implementation of IWidgetCreationFeature for creating a JTable.
 */
public class JTableWidgetCreationFeature extends SwingWidgetCreationFeature implements IWidgetCreationFeature, ISwingWidgetFeature
{
  public JTableWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.SwingCreationFeature#createSwingWidget(java.awt.Container, java.lang.String, org.xmodel.IModelObject)
   */
  @Override
  protected JComponent createSwingWidget( Container container, String label, IModelObject element)
  {
    // TODO: add support for widget label for jtable
    CustomTableModel tableModel = new CustomTableModel( xidget);
    jtable = new JTable( tableModel);
    jtable.setDefaultRenderer( IModelObject.class, new CustomCellRenderer());
    jtable.getTableHeader().setDefaultRenderer( new CustomHeaderCellRenderer());
    container.add( jtable);
    return jtable;
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.ISwingWidgetFeature#getWidget()
   */
  public JComponent getWidget()
  {
    return jtable;
  }

  private IXidget xidget;
  private JTable jtable;
}