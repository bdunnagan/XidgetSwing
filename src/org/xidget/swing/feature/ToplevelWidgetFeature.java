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
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.ITitleFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
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
   * @see org.xidget.swing.feature.SwingWidgetFeature#setComputedBounds(float, float, float, float)
   */
  @Override
  public void setComputedBounds( float x, float y, float width, float height)
  {
    super.setComputedBounds( x, y, width, height);
    
    IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    Object[] widgets = creationFeature.getLastWidgets();
    Component window = (Component)widgets[ 0];
    Rectangle windowBounds = window.getBounds();
    
    Bounds computedBounds = getComputedBounds();
    if ( computedBounds.isXDefined() && computedBounds.x != windowBounds.x) windowBounds.x = (int)Math.floor( computedBounds.x);
    if ( computedBounds.isYDefined() && computedBounds.y != windowBounds.y) windowBounds.y = (int)Math.floor( computedBounds.y);
    if ( computedBounds.isWidthDefined() && computedBounds.width != windowBounds.width) windowBounds.width = (int)Math.ceil( computedBounds.width);
    if ( computedBounds.isHeightDefined() && computedBounds.height != windowBounds.height) windowBounds.height = (int)Math.ceil( computedBounds.height);
    
    window.setBounds( windowBounds);
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
   * @see org.xidget.ifeature.IWidgetFeature#setInsideMargins(org.xidget.layout.Margins)
   */
  @Override
  public void setInsideMargins( Margins margins)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getInsideMargins()
   */
  @Override
  public Margins getInsideMargins()
  {
    if ( insideMargins == null) insideMargins = new Margins();
    return insideMargins;
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetFeature#setOutsideMargins(org.xidget.layout.Margins)
   */
  @Override
  public void setOutsideMargins( Margins margins)
  {
    throw new UnsupportedOperationException();
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
    if ( visible)
    {
      ILayoutFeature layoutFeature = xidget.getFeature( ILayoutFeature.class);
      layoutFeature.layout();
    }

    Window widget = xidget.getFeature( Window.class);
    widget.setVisible( visible);
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
