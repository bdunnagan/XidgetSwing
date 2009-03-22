/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import javax.swing.JFrame;

/**
 * An interface for the top-level widget of a Swing application.
 */
public interface ISwingFrameFeature
{
  /**
   * Returns the top-level widget.
   * @return Returns the top-level widget.
   */
  public JFrame getFrame();
}
