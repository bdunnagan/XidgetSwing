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

import java.awt.Font;
import java.util.EnumSet;
import java.util.List;

import javax.swing.JComponent;

import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.ifeature.ITextWidgetFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xmodel.log.Log;

/**
 * An implementation of ITextWidgetFeature used by select Swing components.
 */
public abstract class SwingTextWidgetFeature implements ITextWidgetFeature
{
  public final static Log log = Log.getLog( SwingTextWidgetFeature.class);
  
  protected SwingTextWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setFontFamily(java.lang.String)
   */
  @Override
  public void setFontFamily( String family)
  {
    JComponent widget = getPrimaryWidget( xidget);
    Font font = widget.getFont();
    widget.setFont( new Font( matchFamily( family), font.getStyle(), font.getSize()));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setFontStyles( EnumSet<FontStyle>)
   */
  @Override
  public void setFontStyles( EnumSet<FontStyle> styles)
  {
    int awtFontStyles = Font.PLAIN;
    if ( styles.contains( FontStyle.italic)) awtFontStyles |= Font.ITALIC;
    if ( styles.contains( FontStyle.bold)) awtFontStyles |= Font.BOLD;
    
    JComponent widget = getPrimaryWidget( xidget);
    Font font = widget.getFont();
    widget.setFont( font.deriveFont( awtFontStyles));
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
  
  /**
   * Finds the first family containig the complete family string.
   * @param families The complete list of families.
   * @return Returns the first match.
   */
  public String matchFamily( String family)
  {
    family = family.toLowerCase();
    
    List<String> names = Creator.getToolkit().getFonts();
    for( String name: names)
    {
      if ( name.toLowerCase().contains( family))
        return name;
    }

    log.errorf( "Unknown font family, '%s'.", family);
    log.verbose( "Available Font Families: ");
    for( String name: names) log.verbose( name);
    
    return null;
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
}
