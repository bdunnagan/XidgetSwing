package org.xidget.swing;

import javax.swing.JComponent;
import org.xidget.IWidgetAdapter;

/**
 * An adapter for Swing/AWT widgets.
 */
public class SwingWidgetAdapter implements IWidgetAdapter, ISwingWidgetAdapter
{
  public SwingWidgetAdapter( JComponent widget)
  {
    this.widget = widget;
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
