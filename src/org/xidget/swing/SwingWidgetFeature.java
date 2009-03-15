package org.xidget.swing;

import java.awt.Rectangle;
import javax.swing.JComponent;
import org.xidget.feature.IWidgetFeature;

/**
 * An adapter for Swing/AWT widgets.
 */
public class SwingWidgetFeature implements IWidgetFeature, ISwingWidgetAdapter
{
  public SwingWidgetFeature( JComponent widget)
  {
    this.widget = widget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetFeature#hasBounds()
   */
  public boolean hasBounds()
  {
    Rectangle rectangle = widget.getBounds();
    boolean result = rectangle.width >= 0 && rectangle.height >= 0;
    if ( !result) System.out.println( "!hasBounds: "+widget);
    return result;
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
