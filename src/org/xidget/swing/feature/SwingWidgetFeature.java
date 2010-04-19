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
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.xidget.IXidget;
import org.xidget.Log;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.Bounds;
import org.xidget.layout.Margins;
import org.xidget.layout.Size;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An adapter for Swing/AWT widgets.
 */
public class SwingWidgetFeature implements IWidgetFeature
{
  public SwingWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setBounds(float, float, float, float)
   */
  public void setBounds( float x, float y, float width, float height)
  {
    if ( updating) return;
    
    Log.printf( "layout", "SET BOUNDS %s -> %2.1f, %2.1f, %2.1f, %2.1f\n", xidget, x, y, width, height);
    
    Component widget = xidget.getFeature( Component.class);
    widget.setBounds( 
      (int)Math.round( x), 
      (int)Math.round( y), 
      (int)Math.round( width), 
      (int)Math.round( height));
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetFeature#getBounds(org.xidget.feature.IWidgetFeature.Bounds)
   */
  public void getBounds( Bounds result)
  {
    Component widget = xidget.getFeature( Component.class);
    widget.getBounds( rectangle);
    result.x = rectangle.x;
    result.y = rectangle.y;
    result.width = rectangle.width;
    result.height = rectangle.height;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getBoundsNode()
   */
  public IModelObject getBoundsNode()
  {
    return boundsNode;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setBoundsNode(org.xmodel.IModelObject)
   */
  public void setBoundsNode( IModelObject node)
  {
    this.boundsNode = node;

    if ( node != null)
    {
      // create bounds listener
      if ( boundsListener == null) boundsListener = new BoundsListener();
      
      // add listener to widget
      IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
      Object[] widgets = creationFeature.getLastWidgets();
      ((Component)widgets[ 0]).addComponentListener( boundsListener);
      
      // update bounds
      Bounds bounds = new Bounds();
      getBounds( bounds);
      if ( bounds.parse( Xlate.get( node, "")))
      {
        setBounds( bounds.x, bounds.y, bounds.width, bounds.height);
      }
    }
    else
    {
      // remove listener from widget
      if ( boundsListener != null)
      {
        JFrame widget = xidget.getFeature( JFrame.class);
        widget.removeComponentListener( boundsListener);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getPreferredSize(org.xidget.layout.Size)
   */
  public void getPreferredSize( Size result)
  {
    JComponent widget = xidget.getFeature( JComponent.class);
    Dimension size = widget.getPreferredSize();
    result.width = size.width;
    result.height = size.height;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getOutsideMargins()
   */
  public Margins getOutsideMargins()
  {
    // not used yet
    return new Margins();
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
    JComponent widget = xidget.getFeature( JComponent.class);
    widget.setEnabled( enabled);
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setTooltip(java.lang.String)
   */
  public void setTooltip( String tooltip)
  {
    JComponent widget = xidget.getFeature( JComponent.class);
    widget.setToolTipText( tooltip);
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

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setFont(java.lang.String)
   */
  public void setFont( String name)
  {
    if ( name.contains( ","))
    {
      String[] split = name.split( "\\s*,\\s*");
      StringBuilder sb = new StringBuilder();
      for( int i=0; i<split.length; i++)
      {
        if ( i > 0) sb.append( '-');
        sb.append( split[ i]);
      }
      name = sb.toString();
    }
    
    JComponent widget = getPrimaryWidget( xidget);
    Font oldFont = widget.getFont();
    Font font = name.contains( "-")? Font.decode( name): new Font( name, oldFont.getStyle(), oldFont.getSize());
    widget.setFont( font);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setFontSize(int)
   */
  public void setFontSize( double size)
  {
    JComponent widget = getPrimaryWidget( xidget);
    Font font = widget.getFont();
    widget.setFont( font.deriveFont( (float)size));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setFontStyle(java.lang.String)
   */
  public void setFontStyle( String style)
  {
    JComponent widget = getPrimaryWidget( xidget);
    Font font = widget.getFont();
    int constant = Font.PLAIN;
    if ( style.equals( "italic") || style.equals( "italics")) constant = Font.ITALIC;
    if ( style.equals( "bold")) constant = Font.BOLD;
    widget.setFont( font.deriveFont( constant));
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

  private class BoundsListener extends ComponentAdapter
  {
    /* (non-Javadoc)
     * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
     */
    public void componentMoved( ComponentEvent e)
    {
      updating = true;
      try
      {
        if ( boundsNode != null)
        {
          getBounds( bounds);
          Xlate.set( boundsNode, bounds.toString());
        }
      }
      finally
      {
        updating = false;
      }
    }

    /* (non-Javadoc)
     * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
     */
    public void componentResized( ComponentEvent e)
    {
      updating = true;
      try
      {
        if ( boundsNode != null)
        {
          getBounds( bounds);
          Xlate.set( boundsNode, bounds.toString());
        }
      }
      finally
      {
        updating = false;
      }
    }
    
    private Bounds bounds = new Bounds();
  }
  
  private IXidget xidget;
  private IModelObject boundsNode;
  private ComponentListener boundsListener;
  private boolean updating;
  private Rectangle rectangle = new Rectangle( 0, 0, 0, 0);
}
