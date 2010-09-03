/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JFrameWidgetFeature.java
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

import java.awt.Component;
import java.awt.Window;

import javax.swing.JFrame;

import org.xidget.IXidget;
import org.xidget.ifeature.ITitleFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.Bounds;
import org.xidget.layout.Margins;

/**
 * An implementation of IWidgetFeature for top-level xidgets.  This implementation can
 * be used for java.swing.JFrame or any widget which inherits from java.awt.Window.
 */
public class ToplevelWidgetFeature extends SwingContainerWidgetFeature implements ITitleFeature
{
  public ToplevelWidgetFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetFeature#setDefaultBounds(float, float, float, float, boolean)
   */
  @Override
  public void setDefaultBounds( float x, float y, float width, float height, boolean clamp)
  {
    super.setDefaultBounds( x, y, width, height, clamp);
    
    //
    // Set the bounds of the frame.  Since the JFrame uses the AdapterLayoutManager which
    // inherits from BorderLayout, the content pane will be resized and the computed size
    // of the root form will be set (see AdapterLayoutManager.layoutContainer).
    //
    Component frame = xidget.getFeature( Component.class);
    frame.setLocation( (int)Math.round( x), (int)Math.round( y));
    frame.setSize( (int)Math.round( width), (int)Math.round( height));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITitleFeature#setTitle(java.lang.String)
   */
  public void setTitle( String title)
  {
    JFrame widget = xidget.getFeature( JFrame.class);
    if ( widget != null) widget.setTitle( title);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getOutsideMargins()
   */
  public Margins getOutsideMargins()
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetFeature#setVisible(boolean)
   */
  public void setVisible( boolean visible)
  {
    Window widget = xidget.getFeature( Window.class);
    
    //
    // Pack frame to preferred size of root form if default bounds are not specified.
    // This is the only case in which the Swing LayoutManager.preferredSize() method
    // will be called (see AdapterLayoutManager.preferredSize).
    //
    Bounds bounds = getDefaultBounds();
    if ( bounds.width < 0 && bounds.height < 0) widget.pack();
   
    // show
    widget.setVisible( visible);
    
    //
    // Update computed bounds of top-level widget before the computed bounds
    // are accessed by the WidgetBoundsListener due to ComponentListener
    // callback.
    //
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    widgetFeature.setComputedBounds( widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return xidget.toString();
  }
}
