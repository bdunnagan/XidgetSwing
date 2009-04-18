/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table.feature;

import org.xidget.IXidget;
import org.xidget.ifeature.IIconFeature;
import org.xidget.ifeature.table.IRowSetFeature;
import org.xidget.ifeature.table.ITableWidgetFeature;
import org.xmodel.IModelObject;

/**
 * An implementation of IIconFeature for setting the icon of a JTable cell.
 */
public class JTableCellIconFeature implements IIconFeature
{
  public JTableCellIconFeature( IXidget xidget)
  {
    this.xidget = xidget;

    // determine column index from config element
    IModelObject element = xidget.getConfig();
    this.column = element.getParent().getChildren( element.getType()).indexOf( element);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    IRowSetFeature rowSetFeature = xidget.getParent().getFeature( IRowSetFeature.class);
    ITableWidgetFeature widgetFeature = xidget.getParent().getFeature( ITableWidgetFeature.class);
    widgetFeature.setIcon( rowSetFeature.getCurrentRow(), column, icon);
  }

  private IXidget xidget;
  private int column;
}
