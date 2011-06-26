/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

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
   * @see org.xidget.feature.tree.ColumnWidthFeature#getMaxWidth()
   */
  @Override
  protected int getMaxWidth()
  {
    JTable jTable = xidget.getFeature( JTable.class);
    if ( jTable != null)
    {
      FontMetrics metrics = jTable.getFontMetrics( jTable.getFont());
      return metrics.getMaxAdvance(); 
    }
    return 0;
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.tree.ColumnWidthFeature#getTextWidth(java.lang.String, boolean)
   */
  @Override
  protected int getTextWidth( String text, boolean header)
  {
    if ( text == null) return 0;
    
    JTable jTable = xidget.getFeature( JTable.class);
    if ( jTable != null)
    {
      Font font = header? jTable.getTableHeader().getFont(): jTable.getFont();
      Graphics graphics = jTable.getGraphics();
      FontMetrics metrics = jTable.getFontMetrics( font);
      Rectangle2D bounds = metrics.getStringBounds( text, graphics);
      return (int)Math.ceil( bounds.getWidth() + 6);
    }
    return 1;
  }
}
