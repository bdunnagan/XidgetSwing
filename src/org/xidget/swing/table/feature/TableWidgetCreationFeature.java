/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table.feature;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JTable;
import org.xidget.IXidget;
import org.xidget.swing.ISwingWidgetFeature;
import org.xidget.swing.SwingCreationFeature;
import org.xidget.swing.table.CustomTableModel;
import org.xmodel.IModelObject;

/**
 * An implementation of IWidgetCreationFeature which creates a JTable.
 */
public class TableWidgetCreationFeature extends SwingCreationFeature implements ISwingWidgetFeature
{
  public TableWidgetCreationFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.SwingCreationFeature#createSwingWidget(java.awt.Container, java.lang.String, org.xmodel.IModelObject)
   */
  @Override
  protected JComponent createSwingWidget( Container container, String label, IModelObject element)
  {
    table = new JTable( new CustomTableModel( xidget));
    container.add( table);
    return table;
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.ISwingWidgetFeature#getWidget()
   */
  public JComponent getWidget()
  {
    return table;
  }

  private IXidget xidget;
  private JTable table;
}
