/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import java.awt.FontMetrics;

import javax.swing.JTable;

import org.xidget.IXidget;
import org.xidget.feature.tree.ColumnWidthFeature;

/**
 * A specialization of ColumnWidthFeature that uses FontMetrics to calculate character width.
 */
public class JTableColumnWidthFeature extends ColumnWidthFeature
{
  public JTableColumnWidthFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.tree.ColumnWidthFeature#getWidth(java.lang.String)
   */
  @Override
  protected int getWidth( String text)
  {
    JTable jTable = xidget.getFeature( JTable.class);
    if ( jTable != null)
    {
      FontMetrics metrics = jTable.getFontMetrics( jTable.getFont());
      return metrics.stringWidth( text) + 3;
    }
    return 1;
  }
}
