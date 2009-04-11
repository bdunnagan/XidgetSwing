/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import org.xidget.feature.IIconFeature;
import org.xidget.feature.ITitleFeature;
import org.xidget.layout.ILayoutFeature;
import org.xidget.swing.table.feature.SwingCellIconFeature;
import org.xidget.swing.table.feature.SwingCellTextWidgetFeature;
import org.xidget.table.column.ColumnXidget;
import org.xidget.text.feature.ITextWidgetFeature;

/**
 * A column xidget for Swing JTable widgets.
 */
public class SwingColumnXidget extends ColumnXidget
{
  /* (non-Javadoc)
   * @see org.xidget.table.ColumnXidget#getIconFeature()
   */
  @Override
  protected IIconFeature getIconFeature()
  {
    return new SwingCellIconFeature( this);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.ColumnXidget#getTextWidgetFeature()
   */
  @Override
  protected ITextWidgetFeature getTextWidgetFeature()
  {
    return new SwingCellTextWidgetFeature( this);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.ColumnXidget#getTitleFeature()
   */
  @Override
  protected ITitleFeature getTitleFeature()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
