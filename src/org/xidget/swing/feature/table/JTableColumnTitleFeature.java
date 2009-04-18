/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.table;

import javax.swing.JTable;
import org.xidget.IXidget;
import org.xidget.ifeature.ITitleFeature;
import org.xidget.swing.table.CustomTableModel;
import org.xmodel.IModelObject;

/**
 * An implementation of ITitleFeature for the column heading of a JTable.
 */
public class JTableColumnTitleFeature implements ITitleFeature
{
  /**
   * Create with the specified column xidget.
   * @param xidget The column xidget.
   */
  public JTableColumnTitleFeature( IXidget xidget)
  {
    this.xidget = xidget;
    
    // get column index
    IModelObject config = xidget.getConfig();
    column = config.getParent().getChildren( config.getType()).indexOf( config);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.ITitleFeature#setTitle(java.lang.String)
   */
  public void setTitle( String title)
  {
    JTable table = xidget.getFeature( JTable.class);
    CustomTableModel tableModel = (CustomTableModel)table.getModel();
    tableModel.setColumnTitle( column, title);
  }

  private IXidget xidget;
  private int column;
}
