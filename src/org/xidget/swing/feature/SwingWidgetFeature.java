/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Rectangle;
import javax.swing.JComponent;
import org.xidget.IXidget;
import org.xidget.Log;
import org.xidget.ifeature.IWidgetFeature;
import org.xmodel.util.Radix;

/**
 * An adapter for Swing/AWT widgets.
 */
public class SwingWidgetFeature implements IWidgetFeature
{
  public SwingWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    
    StringBuilder sb = new StringBuilder();
    sb.append( "@"); sb.append( Radix.convert( hashCode(), 36));
    Log.printf( "xidget", "%s: %s\n", xidget, sb);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetFeature#setBounds(int, int, int, int)
   */
  public void setBounds( int x, int y, int width, int height)
  {
    JComponent widget = xidget.getFeature( JComponent.class);
    widget.setBounds( x, y, width, height);
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
    JComponent widget = xidget.getFeature( JComponent.class);
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

  private IXidget xidget;
}
