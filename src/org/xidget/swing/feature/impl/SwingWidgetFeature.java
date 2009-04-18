/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.impl;

import java.awt.Rectangle;
import javax.swing.JComponent;
import org.xidget.feature.IWidgetFeature;
import org.xidget.swing.feature.ISwingWidgetFeature;

/**
 * An adapter for Swing/AWT widgets.
 */
public class SwingWidgetFeature implements IWidgetFeature, ISwingWidgetFeature
{
  public SwingWidgetFeature( JComponent widget)
  {
    this.widget = widget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetFeature#setBounds(int, int, int, int)
   */
  public void setBounds( int x, int y, int width, int height)
  {
    widget.setBounds( x, y, width, height);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetFeature#getBounds(org.xidget.feature.IWidgetFeature.Bounds)
   */
  public void getBounds( Bounds result)
  {
    Rectangle rectangle = widget.getBounds();
    result.x = rectangle.x;
    result.y = rectangle.y;
    result.width = rectangle.width;
    result.height = rectangle.height;
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetFeature#setVisible(boolean)
   */
  public void setVisible( boolean visible)
  {
    widget.setVisible( visible);
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setEnabled(boolean)
   */
  public void setEnabled( boolean enabled)
  {
    widget.setEnabled( enabled);
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setTooltip(java.lang.String)
   */
  public void setTooltip( String tooltip)
  {
    widget.setToolTipText( tooltip);
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.ISwingWidgetAdapter#getWidget()
   */
  public JComponent getWidget()
  {
    return widget;
  }

  private JComponent widget;
}
