/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

/**
 * An interface for setting the title and icon of a column of a tabular widget (table, tree, etc...).
 */
public interface ITableHeaderFeature
{
  /**
   * Set the title of the specified column.
   * @param column The column index.
   * @param title The title.
   */
  public void setTitle( int column, String title);
  
  /**
   * Set the icon of the specified column.
   * @param column The column index.
   * @param icon The icon.
   */
  public void setIcon( int column, Object icon);
}
