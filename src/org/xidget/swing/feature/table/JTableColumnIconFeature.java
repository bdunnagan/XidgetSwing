/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.table;

import javax.swing.JTable;
import org.xidget.IXidget;
import org.xidget.ifeature.IIconFeature;
import org.xidget.swing.ifeature.ISwingWidgetFeature;
import org.xidget.swing.table.CustomTableModel;
import org.xmodel.IModelObject;

/**
 * An implementation of IIconFeature for the column heading of a JTable.
 */
public class JTableColumnIconFeature implements IIconFeature
{
  /**
   * Create with the specified column xidget.
   * @param xidget The column xidget.
   */
  public JTableColumnIconFeature( IXidget xidget)
  {
    this.xidget = xidget;
    
    // get column index
    IModelObject config = xidget.getConfig();
    column = config.getParent().getChildren( config.getType()).indexOf( config);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    ISwingWidgetFeature feature = xidget.getFeature( ISwingWidgetFeature.class);
    JTable table = (JTable)feature.getWidget();
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.setColumnIcon( column, icon);
  }

  private IXidget xidget;
  private int column;
}
