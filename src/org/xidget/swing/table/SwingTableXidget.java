/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.table.TableXidget;
import org.xidget.table.features.ITableModelFeature;
import org.xidget.table.features.ITableWidgetFeature;

/**
 * An implementation of TableXidget for use with the JTable widget.
 */
public class SwingTableXidget extends TableXidget
{
  /* (non-Javadoc)
   * @see org.xidget.table.TableXidget#getErrorFeature()
   */
  @Override
  protected IErrorFeature getErrorFeature()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.TableXidget#getTableModelFeature()
   */
  @Override
  protected ITableModelFeature getTableModelFeature()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.TableXidget#getTableWidgetFeature()
   */
  @Override
  protected ITableWidgetFeature getTableWidgetFeature()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.TableXidget#getWidgetFeature()
   */
  @Override
  protected IWidgetFeature getWidgetFeature()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
