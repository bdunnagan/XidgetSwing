/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.util;

import javax.swing.SwingUtilities;
import org.xmodel.IDispatcher;

/**
 * An implementation of IDispatcher that dispatches to the Swing event thread.
 */
public class SwingDispatcher implements IDispatcher
{
  /* (non-Javadoc)
   * @see org.xmodel.IDispatcher#execute(java.lang.Runnable)
   */
  @Override
  public void execute( Runnable runnable)
  {
    SwingUtilities.invokeLater( runnable);
  }

  /* (non-Javadoc)
   * @see org.xmodel.IDispatcher#shutdown(boolean)
   */
  @Override
  public void shutdown( boolean immediate)
  {
    // see JSR 296
    throw new UnsupportedOperationException();
  }
}
