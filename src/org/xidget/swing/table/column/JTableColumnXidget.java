/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table.column;

import org.xidget.feature.IIconFeature;
import org.xidget.feature.ITitleFeature;
import org.xidget.feature.IWidgetCreationFeature;
import org.xidget.swing.table.feature.JTableCellTextWidgetFeature;
import org.xidget.swing.table.feature.JTableColumnIconFeature;
import org.xidget.swing.table.feature.JTableColumnTitleFeature;
import org.xidget.table.column.ColumnXidget;
import org.xidget.text.feature.ITextWidgetFeature;

/**
 * A column xidget for Swing JTable widgets.
 */
public class JTableColumnXidget extends ColumnXidget
{
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#getWidgetCreationFeature()
   */
  @Override
  protected IWidgetCreationFeature getWidgetCreationFeature()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.ColumnXidget#getIconFeature()
   */
  @Override
  protected IIconFeature getIconFeature()
  {
    return new JTableColumnIconFeature( this);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.ColumnXidget#getTitleFeature()
   */
  @Override
  protected ITitleFeature getTitleFeature()
  {
    return new JTableColumnTitleFeature( this);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.column.ColumnXidget#getTextWidgetFeature()
   */
  @Override
  protected ITextWidgetFeature getTextWidgetFeature()
  {
    return new JTableCellTextWidgetFeature( this);
  }
}
