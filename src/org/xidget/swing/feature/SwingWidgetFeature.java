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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.Bounds;
import org.xidget.layout.Margins;
import org.xidget.swing.layout.WidgetBoundsListener;
import org.xmodel.IModelObject;

/**
 * An adapter for Swing/AWT widgets.
 */
public class SwingWidgetFeature implements IWidgetFeature
{
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
   * @see org.xidget.ifeature.IWidgetFeature#setOutsideMargins(org.xidget.layout.Margins)
   */
  @Override
  public void setOutsideMargins( Margins margins)
  {
    JComponent widget = xidget.getFeature( JComponent.class);
    if ( widget != null) 
    {
      Border border = widget.getBorder();
      Border outside = BorderFactory.createEmptyBorder( margins.y0, margins.x0, margins.y1, margins.x1);
      widget.setBorder( (border == null)? outside: BorderFactory.createCompoundBorder( outside, border));
    }
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
    Component widget = xidget.getFeature( Component.class);
    widget.setEnabled( enabled);
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setTooltip(java.lang.String)
   */
  public void setTooltip( String tooltip)
  {
    JComponent widget = xidget.getFeature( JComponent.class);
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

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setFont(java.lang.String)
   */
  public void setFont( String name)
  {
    JComponent widget = getPrimaryWidget( xidget);
    Font oldFont = widget.getFont();
    
    String family = oldFont.getFamily();
    int size = oldFont.getSize();
    int style = oldFont.getStyle();
    
    String[] split = name.split( "\\s*[,\\- /]\\s*");
    if ( split.length > 0)
    {
      String newFamily = matchFamily( split[ 0]);
      if ( newFamily != null) family = newFamily;
    }
    
    if ( split.length > 1)
    {
      try { size = Integer.parseInt( split[ 1]);} catch( Exception e) {}
    }
    
    if ( split.length > 2)
    {
      style = parseFontStyle( split[ 2]);
    }
    
    widget.setFont( new Font( family, style, size));
  }

  /**
   * Finds the first family containig the complete family string.
   * @param families The complete list of families.
   * @return Returns the first match.
   */
  public String matchFamily( String family)
  {
    List<String> names = Creator.getInstance().getToolkit().getFonts();
    for( String name: names)
    {
      if ( name.contains( family))
        return name;
    }
    return null;
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
  public void setFontStyle( String styles)
  {
    JComponent widget = getPrimaryWidget( xidget);
    Font font = widget.getFont();
    int constant = parseFontStyle( styles);
    widget.setFont( font.deriveFont( constant));
  }
  
  /**
   * Parse the font style from the specified styles string.
   * @param styles The styles string.
   * @return Returns the font style.
   */
  private int parseFontStyle( String styles)
  {
    int constant = Font.PLAIN;
    String[] split = styles.split( "[,\\- /]");
    for( String style: split)
    {
      if ( style.equalsIgnoreCase( "italic") || style.equalsIgnoreCase( "italics")) constant |= Font.ITALIC;
      if ( style.equalsIgnoreCase( "bold")) constant |= Font.BOLD;
    }
    return constant;
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
}
