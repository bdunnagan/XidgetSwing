/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.menu;

import javax.swing.JMenuItem;

import org.xidget.IXidget;
import org.xidget.ifeature.model.ISingleValueWidgetFeature;

public class JMenuItemSingleValueWidgetFeature implements ISingleValueWidgetFeature
{
  public JMenuItemSingleValueWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueWidgetFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    JMenuItem menuItem = xidget.getFeature( JMenuItem.class);
    return menuItem.getSelectedObjects() != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueWidgetFeature#setValue(java.lang.Object)
   */
  @Override
  public void setValue( Object value)
  {
  }
  
  private IXidget xidget;
}
