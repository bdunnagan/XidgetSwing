/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

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
      TableCellRenderer renderer = header? jTable.getTableHeader().getDefaultRenderer(): jTable.getDefaultRenderer( String.class);
      Component component = renderer.getTableCellRendererComponent( jTable, text, true, true, 0, 0);
      Dimension size = component.getPreferredSize();
      return size.width;
    }
    return 10;
  }
}
