/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import javax.swing.JComponent;

/**
 * An adapter interface for returning the Swing widget of a Swing xidget.
 */
public interface ISwingWidgetFeature
{
  /**
   * Returns the widget.
   * @return Returns the widget.
   */
  public JComponent getWidget();
}
