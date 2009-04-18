/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.impl;

import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;
import org.xidget.feature.IAsyncFeature;

/**
 * An implementation of the IAsyncFeature for Swing/AWT.
 */
public class SwingAsyncFeature implements IAsyncFeature
{
  public SwingAsyncFeature()
  {
    tasks = new Hashtable<Object, Task>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IAsyncFeature#run(java.lang.Runnable)
   */
  public void run( Runnable runnable)
  {
    SwingUtilities.invokeLater( runnable);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IAsyncFeature#schedule(java.lang.Object, int, java.lang.Runnable)
   */
  public void schedule( Object key, int delay, Runnable runnable)
  {
    if ( timer == null) timer = new Timer();
    
    Task task = tasks.remove( key);
    if ( task != null) task.cancel();
    
    task = new Task();
    task.runnable = runnable;
    timer.schedule( task, delay);
    tasks.put( key, task);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IAsyncFeature#cancel(java.lang.Object)
   */
  public void cancel( Object key)
  {
    Task task = tasks.remove( key);
    if ( task != null) task.cancel();
  }

  private class Task extends TimerTask
  {
    public void run()
    {
      SwingAsyncFeature.this.run( runnable);
    }
    
    public Runnable runnable;
  }
  
  private static Timer timer;
  private Hashtable<Object, Task> tasks;
}
