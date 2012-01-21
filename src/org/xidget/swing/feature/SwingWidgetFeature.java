/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * SwingWidgetFeature.java
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.Bounds;
import org.xidget.layout.Margins;
import org.xidget.swing.layout.WidgetBoundsListener;
import org.xmodel.IModelObject;
import org.xmodel.log.Log;

/**
 * An adapter for Swing/AWT widgets.
 */
public class SwingWidgetFeature implements IWidgetFeature
{
  public final static Log log = Log.getLog( SwingWidgetFeature.class);
  
  public SwingWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.defaultBounds = new Bounds( 0, 0, -1, -1);
    this.computedBounds = new Bounds( 0, 0, -1, -1);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setDefaultBounds(float, float, float, float, boolean)
   */
  public void setDefaultBounds( float x, float y, float width, float height, boolean clamp)
  {
    defaultBounds.x = x;
    defaultBounds.y = y;
    defaultBounds.width = width;
    defaultBounds.height = height;
    clampBounds = clamp;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getDefaultBounds()
   */
  public Bounds getDefaultBounds()
  {
    //
    // Use the preferred size of the Swing width for any value which is not specified.
    // The Swing preferred size is only used for non-container widgets since containers
    // determine their preferred size via layout or according to the default bounds 
    // specified for the container.
    //
    Component widget = xidget.getFeature( Component.class);
    Dimension size = widget.getPreferredSize();
    if ( defaultBounds.width < 0) defaultBounds.width = size.width;
    if ( defaultBounds.height < 0) defaultBounds.height = size.height;
    
    return defaultBounds;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setComputedBounds(float, float, float, float)
   */
  public void setComputedBounds( float x, float y, float width, float height)
  {
    if ( x == computedBounds.x && y == computedBounds.y && width == computedBounds.width && height == computedBounds.height)
      return;
    
    computedBounds.x = x;
    computedBounds.y = y;
    computedBounds.width = width;
    computedBounds.height = height;
    
    if ( !clampBounds)
    {
      setDefaultBounds( x, y, width, height, false);
    }
    
    Component widget = xidget.getFeature( Component.class);
    widget.setBounds( 
      (int)Math.round( x), 
      (int)Math.round( y), 
      (int)Math.round( width), 
      (int)Math.round( height));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getComputedBounds()
   */
  public Bounds getComputedBounds()
  {
    return computedBounds;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setBoundsNode(org.xmodel.IModelObject)
   */
  public void setBoundsNode( IModelObject node)
  {
    boundsNode = node;
    
    //
    // Add a listener to update the bounds node when the component bounds change.
    // Note that this doesn't work for JMenuItem because it plays tricks with the
    // widget location state.
    //
    if ( node != null)
    {
      Component component = xidget.getFeature( Component.class);
      if ( component != null) component.addComponentListener( new WidgetBoundsListener( xidget));
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getBoundsNode()
   */
  public IModelObject getBoundsNode()
  {
    return boundsNode;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setInsideMargins(org.xidget.layout.Margins)
   */
  @Override
  public void setInsideMargins( Margins margins)
  {
    insideMargins = margins;
    
    IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    Object[] widgets = creationFeature.getLastWidgets();
    JComponent widget = (JComponent)widgets[ widgets.length - 1];
    if ( widget != null)
    {
      Border border = widget.getBorder();
      if ( border != null && border == insideBorder)
      {
        border = (border instanceof CompoundBorder)? ((CompoundBorder)border).getOutsideBorder(): null;
      }
      
      if ( border == null)
      {
        insideBorder = BorderFactory.createEmptyBorder( margins.y0, margins.x0, margins.y1, margins.x1);
        widget.setBorder( insideBorder);
      }
      else
      {
        insideBorder = BorderFactory.createEmptyBorder( margins.y0, margins.x0, margins.y1, margins.x1);
        widget.setBorder( BorderFactory.createCompoundBorder( border, insideBorder));
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getInsideMargins()
   */
  @Override
  public Margins getInsideMargins()
  {
    if ( insideMargins == null)
    {
      insideMargins = new Margins();
      JComponent component = xidget.getFeature( JComponent.class);
      if ( component != null)
      {
        Insets insets = component.getInsets();
        insideMargins.x0 = insets.left;
        insideMargins.y0 = insets.top;
        insideMargins.x1 = insets.right;
        insideMargins.y1 = insets.bottom;
      }
    }
    return insideMargins;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setOutsideMargins(org.xidget.layout.Margins)
   */
  @Override
  public void setOutsideMargins( Margins margins)
  {
    outsideMargins = margins;
    
    IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    Object[] widgets = creationFeature.getLastWidgets();
    JComponent widget = (JComponent)widgets[ 0];
    if ( widget != null)
    {
      Border border = widget.getBorder();
      if ( border != null && border == outsideBorder)
      {
        border = (border instanceof CompoundBorder)? ((CompoundBorder)border).getInsideBorder(): null;
      }
      
      if ( border == null)
      {
        outsideBorder = BorderFactory.createEmptyBorder( margins.y0, margins.x0, margins.y1, margins.x1);
        widget.setBorder( outsideBorder);
      }
      else
      {
        outsideBorder = BorderFactory.createEmptyBorder( margins.y0, margins.x0, margins.y1, margins.x1);
        widget.setBorder( BorderFactory.createCompoundBorder( outsideBorder, border));
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getOutsideMargins()
   */
  @Override
  public Margins getOutsideMargins()
  {
    if ( outsideMargins == null) outsideMargins = new Margins();
    return outsideMargins;
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetFeature#setVisible(boolean)
   */
  public void setVisible( boolean visible)
  {
    Component widget = xidget.getFeature( Component.class);
    widget.setVisible( visible);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getVisible()
   */
  public boolean getVisible()
  {
    Component widget = xidget.getFeature( Component.class);
    return widget.isVisible(); 
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setEnabled(boolean)
   */
  public void setEnabled( boolean enabled)
  {
    JComponent widget = getPrimaryWidget( xidget); 
    widget.setEnabled( enabled);
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setTooltip(java.lang.String)
   */
  public void setTooltip( String tooltip)
  {
    JComponent widget = getPrimaryWidget( xidget); 
    if ( widget != null) widget.setToolTipText( tooltip);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setBackground(int)
   */
  public void setBackground( int color)
  {
    JComponent widget = getPrimaryWidget( xidget);
    widget.setBackground( new Color( color));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setForeground(int)
   */
  public void setForeground( int color)
  {
    JComponent widget = getPrimaryWidget( xidget);
    widget.setForeground( new Color( color));
  }

  /**
   * Returns the primary interactive widget associated with the xidget.
   * @param xidget The xidget.
   * @return Returns the primary interactive widget associated with the xidget.
   */
  private static JComponent getPrimaryWidget( IXidget xidget)
  {
    IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    Object[] widgets = creationFeature.getLastWidgets();
    return (JComponent)widgets[ widgets.length - 1];
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return xidget.toString();
  }

  protected IXidget xidget;
  protected IModelObject boundsNode;
  protected Bounds defaultBounds = new Bounds();
  protected Bounds computedBounds = new Bounds();
  protected boolean clampBounds;
  protected Margins insideMargins;
  protected Margins outsideMargins;
  protected Border insideBorder;
  protected Border outsideBorder;
}
