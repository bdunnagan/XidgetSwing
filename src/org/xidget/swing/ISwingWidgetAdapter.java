package org.xidget.swing;

import javax.swing.JComponent;

/**
 * An adapter interface for returning the Swing widget of a Swing xidget.
 */
public interface ISwingWidgetAdapter
{
  /**
   * Returns the widget.
   * @return Returns the widget.
   */
  public JComponent getWidget();
}
