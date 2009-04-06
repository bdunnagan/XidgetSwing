/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table.feature;

import org.xidget.IXidget;
import org.xidget.feature.IIconFeature;
import org.xidget.feature.ITitleFeature;
import org.xidget.swing.feature.ITableHeaderFeature;

/**
 * An implementation of IIconFeature which sends an event to the JTable to update the column header text and icon.
 */
public class SwingColumnHeaderFeature implements ITitleFeature, IIconFeature
{
  /**
   * Cretae a column header feature for the specified column xidget.
   * @param xidget The column xidget.
   * @param column The column index.
   */
  public SwingColumnHeaderFeature( IXidget xidget, int column)
  {
    this.xidget = xidget;
    this.column = column;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.ITitleFeature#setTitle(java.lang.String)
   */
  public void setTitle( String title)
  {
    ITableHeaderFeature feature = xidget.getParent().getFeature( ITableHeaderFeature.class);
    feature.setTitle( column, title);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    ITableHeaderFeature feature = xidget.getParent().getFeature( ITableHeaderFeature.class);
    feature.setIcon( column, icon);
  }
  
  private IXidget xidget;
  private int column;
}
