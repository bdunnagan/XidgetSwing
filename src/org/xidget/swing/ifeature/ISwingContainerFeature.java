/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.ifeature;

import java.awt.Container;

/**
 * An interface for accessing the Swing/AWT Container class from a xidget.
 */
public interface ISwingContainerFeature
{
  /**
   * Returns the container.
   * @return Returns the container.
   */
  public Container getContainer();
}
