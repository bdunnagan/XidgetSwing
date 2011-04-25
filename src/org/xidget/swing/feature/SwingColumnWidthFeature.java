/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Component;
import java.awt.FontMetrics;

import org.xidget.IXidget;
import org.xidget.feature.tree.ColumnWidthFeature;

/**
 * A specialization of ColumnWidthFeature that uses FontMetrics to calculate character width.
 */
public class SwingColumnWidthFeature extends ColumnWidthFeature
{
  public SwingColumnWidthFeature( IXidget xidget)
  {
    super( xidget, 1);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.tree.ColumnWidthFeature#getWidth(java.lang.String)
   */
  @Override
  protected int getWidth( String text)
  {
    Component component = xidget.getFeature( Component.class);
    if ( component != null)
    {
      FontMetrics metrics = component.getFontMetrics( component.getFont());
      return metrics.stringWidth( text);
    }
    return 1;
  }
}
