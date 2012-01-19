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

import javax.swing.JComponent;

import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.layout.Bounds;

/**
 * An adapter for Swing/AWT container widgets. This implementation should be used
 * for all containers that will contain xidget children since we do not use the
 * getPreferredSize() method to obtain the default bounds of a container.
 */
public class SwingContainerWidgetFeature extends SwingWidgetFeature
{
  public SwingContainerWidgetFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getDefaultBounds()
   */
  @Override
  public Bounds getDefaultBounds()
  {
    return defaultBounds;
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetFeature#setFontFamily(java.lang.String)
   */
  @Override
  public void setFontFamily( String family)
  {
    super.setFontFamily( family);
    
    JComponent component = xidget.getFeature( JComponent.class);
    setChildrenFonts( component.getFont());
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetFeature#setFontStyles(java.util.EnumSet)
   */
  @Override
  public void setFontStyles( EnumSet<FontStyle> styles)
  {
    super.setFontStyles( styles);
    
    JComponent component = xidget.getFeature( JComponent.class);
    setChildrenFonts( component.getFont());
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetFeature#setFontSize(double)
   */
  @Override
  public void setFontSize( double size)
  {
    super.setFontSize( size);
    
    JComponent component = xidget.getFeature( JComponent.class);
    setChildrenFonts( component.getFont());
  }  

  /**
   * Set the font of each child.
   * @param font The new font.
   */
  private void setChildrenFonts( Font font)
  {
    for( IXidget child: xidget.getChildren())
    {
      IWidgetCreationFeature widgetFeature = child.getFeature( IWidgetCreationFeature.class);
      Object[] widgets = widgetFeature.getLastWidgets();
      JComponent component = (JComponent)widgets[ widgets.length - 1];
      component.setFont( font);
    }
  }
}
