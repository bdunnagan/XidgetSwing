/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.util;

import java.util.concurrent.Executor;
import javax.swing.SwingUtilities;

/**
 * An implementation of Executor that dispatches to the Swing event thread.
 */
public class SwingExecutor implements Executor
{
  /* (non-Javadoc)
   * @see org.xmodel.IDispatcher#execute(java.lang.Runnable)
   */
  @Override
  public void execute( Runnable runnable)
  {
    SwingUtilities.invokeLater( runnable);
  }
}
