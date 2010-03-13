/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * GenericContainerFeature.java
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
package org.xidget.swing.application;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import org.xidget.IXidget;
import org.xidget.Log;
import org.xidget.ifeature.IAsyncFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.canvas.ICanvasFeature;
import org.xidget.ifeature.canvas.IPaintFeature;
import org.xidget.layout.Margins;

/**
 * An implementation of IWidgetContainerFeature that assumes that the parent xidget exports
 * and instance of a Swing Container.
 */
public class JFrameContainerFeature implements IWidgetContainerFeature
{
  public final static int validationDelay = 0;
  
  public JFrameContainerFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#addWidget(org.xidget.IXidget)
   */
  public void addWidget( IXidget child)
  {
    addWidget( -1, child);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#addWidget(int, org.xidget.IXidget)
   */
  public void addWidget( int index, IXidget child)
  {
    Container container = xidget.getFeature( Container.class);
    
    // normal swing widgets
    IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
    if ( creationFeature != null)
    {
      Object[] widgets = creationFeature.getLastWidgets();
      if ( widgets.length > 0) 
      {
        Log.printf( "xidget", "JFrameContainerFeature.addWidget: %s <- %s\n", xidget, child);
        if ( child.getConfig().isType( "menubar"))
        {
          if ( container instanceof JFrame || container instanceof JApplet)
          {
            ((JFrame)container).setJMenuBar( (JMenuBar)widgets[ 0]);
          }
        }
        else
        {
          if ( index == -1) container.add( (Component)widgets[ 0]);
          else container.add( (Component)widgets[ 0], index);
        }
        
        // validate the container later to improve performance
        if ( container.isShowing())
        {
          IAsyncFeature asyncFeature = xidget.getFeature( IAsyncFeature.class);
          asyncFeature.schedule( this, validationDelay, false, validateRunnable);
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#removeWidget(org.xidget.IXidget)
   */
  public void removeWidget( IXidget child)
  {
    Container container = xidget.getFeature( Container.class);
    // normal swing widgets
    IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
    if ( creationFeature != null)
    {
      Object[] widgets = creationFeature.getLastWidgets();
      if ( widgets.length > 0) container.remove( (Component)widgets[ 0]);
    }
    
    // paintable xidgets
    IPaintFeature paintFeature = child.getFeature( IPaintFeature.class);
    if ( paintFeature != null)
    {
      ICanvasFeature canvasFeature = xidget.getFeature( ICanvasFeature.class);
      if ( canvasFeature != null) canvasFeature.removeChild( child);
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#relayout()
   */
  public void relayout()
  {
    ILayoutFeature layoutFeature = xidget.getFeature( ILayoutFeature.class);
    if ( layoutFeature != null) layoutFeature.invalidate();
    
    Container container = xidget.getFeature( Container.class);
    if ( container.isShowing()) container.validate();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#getInsideMargins()
   */
  public Margins getInsideMargins()
  {
    Margins margins = new Margins();
    
    Container container = xidget.getFeature( Container.class);
    if ( container != null)
    {
      Insets insets = container.getInsets();
      margins.x0 = insets.left;
      margins.y0 = insets.top;
      margins.x1 = insets.right;
      margins.y1 = insets.bottom;
    }
    
    return margins;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return xidget.toString();
  }

  private Runnable validateRunnable = new Runnable() {
    public void run()
    {
      Container container = xidget.getFeature( Container.class);
      container.validate();
    }
  };
  
  private IXidget xidget;
}
