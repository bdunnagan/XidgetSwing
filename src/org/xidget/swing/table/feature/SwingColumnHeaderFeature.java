/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table.feature;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import org.xidget.feature.IIconFeature;
import org.xidget.feature.ITitleFeature;

/**
 * An implementation of IIconFeature which sends an event to the JTable to update the column header text and icon.
 */
public class SwingColumnHeaderFeature implements ITitleFeature, IIconFeature
{
  public SwingColumnHeaderFeature( JTable table)
  {
    this.table = table;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.ITitleFeature#setTitle(java.lang.String)
   */
  public void setTitle( String title)
  {
    TableModelEvent event = new TableModelEvent( table.getModel(), TableModelEvent.HEADER_ROW);
    table.tableChanged( event);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    TableModelEvent event = new TableModelEvent( table.getModel(), TableModelEvent.HEADER_ROW);
    table.tableChanged( event);
  }

  private JTable table;
}
