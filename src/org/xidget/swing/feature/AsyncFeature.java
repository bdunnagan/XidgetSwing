/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * AsyncFeature.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.swing.feature;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.xidget.ifeature.IAsyncFeature;

/**
 * An implementation of the IAsyncFeature for Swing/AWT.
 */
public class AsyncFeature implements IAsyncFeature
{
  public AsyncFeature()
  {
    timers = new Hashtable<Object, Timer>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IAsyncFeature#run(java.lang.Runnable)
   */
  public void run( Runnable runnable)
  {
    SwingUtilities.invokeLater( runnable);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IAsyncFeature#schedule(java.lang.Object, int, boolean, java.lang.Runnable)
   */
  public void schedule( Object key, int delay, boolean repeat, Runnable runnable)
  {
    if ( delay == 0 && !repeat) run( runnable);
    
    Timer timer = timers.remove( key);
    if ( timer != null) timer.stop();
    
    timer = new Timer( delay, new Task( runnable));
    timer.setRepeats( repeat);
    timers.put( key, timer);
    
    timer.start();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IAsyncFeature#cancel(java.lang.Object)
   */
  public void cancel( Object key)
  {
    Timer timer = timers.remove( key);
    if ( timer != null) timer.stop();
  }

  private class Task implements ActionListener
  {
    public Task( Runnable runnable)
    {
      this.runnable = runnable;
    }
    
    public void actionPerformed( ActionEvent e)
    {
      AsyncFeature.this.run( runnable);
    }

    private Runnable runnable;
  }
  
  private Hashtable<Object, Timer> timers;
}
