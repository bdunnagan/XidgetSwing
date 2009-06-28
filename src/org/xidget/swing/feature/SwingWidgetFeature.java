/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.JComponent;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetFeature;

/**
 * An adapter for Swing/AWT widgets.
 */
public class SwingWidgetFeature implements IWidgetFeature
{
  public SwingWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setBounds(float, float, float, float)
   */
  public void setBounds( float x, float y, float width, float height)
  {
    JComponent widget = xidget.getFeature( JComponent.class);
    widget.setBounds( (int)Math.round( x), (int)Math.round( y), (int)Math.round( width), (int)Math.round( height));
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetFeature#getBounds(org.xidget.feature.IWidgetFeature.Bounds)
   */
  public void getBounds( Bounds result)
  {
    JComponent widget = xidget.getFeature( JComponent.class);
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
    Component widget = xidget.getFeature( Component.class);
    widget.setVisible( visible);
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setEnabled(boolean)
   */
  public void setEnabled( boolean enabled)
  {
    JComponent widget = xidget.getFeature( JComponent.class);
    widget.setEnabled( enabled);
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setTooltip(java.lang.String)
   */
  public void setTooltip( String tooltip)
  {
    JComponent widget = xidget.getFeature( JComponent.class);
    widget.setToolTipText( tooltip);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return xidget.toString();
  }

  private IXidget xidget;
}
