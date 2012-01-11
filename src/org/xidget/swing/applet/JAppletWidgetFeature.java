/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JAppletWidgetFeature.java
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
package org.xidget.swing.applet;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JApplet;

import org.xidget.IXidget;
import org.xidget.ifeature.ITitleFeature;
import org.xidget.layout.Margins;
import org.xidget.swing.feature.SwingContainerWidgetFeature;

/**
 * An implementation of IWidgetFeature for JApplet.
 */
public class JAppletWidgetFeature extends SwingContainerWidgetFeature implements ITitleFeature
{
  public JAppletWidgetFeature( IXidget xidget)
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
    // Set the bounds of the frame.  Since the JApplet uses the AdapterLayoutManager which
    // inherits from BorderLayout, the content pane will be resized and the computed size
    // of the root form will be set (see AdapterLayoutManager.layoutContainer).
    //
    JApplet frame = xidget.getFeature( JApplet.class);
    frame.setLocation( (int)Math.round( x), (int)Math.round( y));
    frame.setSize( (int)Math.round( width), (int)Math.round( height));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITitleFeature#setTitle(java.lang.String)
   */
  public void setTitle( String title)
  {
    JApplet widget = xidget.getFeature( JApplet.class);
    widget.setName( title);
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
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getVisible()
   */
  public boolean getVisible()
  {
    JApplet widget = xidget.getFeature( JApplet.class);
    return widget.isVisible(); 
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setEnabled(boolean)
   */
  public void setEnabled( boolean enabled)
  {
    JApplet widget = xidget.getFeature( JApplet.class);
    widget.setEnabled( enabled);
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setTooltip(java.lang.String)
   */
  public void setTooltip( String tooltip)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setBackground(int)
   */
  public void setBackground( int color)
  {
    JApplet widget = xidget.getFeature( JApplet.class);
    widget.setBackground( new Color( color));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setForeground(int)
   */
  public void setForeground( int color)
  {
    JApplet widget = xidget.getFeature( JApplet.class);
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
    
    JApplet widget = xidget.getFeature( JApplet.class);
    Font oldFont = widget.getFont();
    Font font = new Font( name, oldFont.getStyle(), oldFont.getSize());
    widget.setFont( font);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setFontSize(int)
   */
  public void setFontSize( double size)
  {
    JApplet widget = xidget.getFeature( JApplet.class);
    Font font = widget.getFont();
    widget.setFont( font.deriveFont( (float)size));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setFontStyle(java.lang.String)
   */
  public void setFontStyle( String style)
  {
    JApplet widget = xidget.getFeature( JApplet.class);
    Font font = widget.getFont();
    int constant = Font.PLAIN;
    if ( style.equals( "italic") || style.equals( "italics")) constant = Font.ITALIC;
    if ( style.equals( "bold")) constant = Font.BOLD;
    widget.setFont( font.deriveFont( constant));
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
