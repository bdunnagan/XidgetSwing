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
import org.xidget.ifeature.ITextWidgetFeature;
import org.xidget.ifeature.IWidgetCreationFeature;

/**
 * An implementation of ITextWidgetFeature for containers so that they can transfer font assignments to their children.
 */
public class SwingContainerTextWidgetFeature implements ITextWidgetFeature
{
  public SwingContainerTextWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setHAlign(org.xidget.ifeature.ITextWidgetFeature.HAlign)
   */
  @Override
  public void setHAlign( HAlign alignment)
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setVAlign(org.xidget.ifeature.ITextWidgetFeature.VAlign)
   */
  @Override
  public void setVAlign( VAlign alignment)
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setEditable(boolean)
   */
  @Override
  public void setEditable( boolean editable)
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetFeature#setFontFamily(java.lang.String)
   */
  @Override
  public void setFontFamily( String family)
  {
    JComponent component = xidget.getFeature( JComponent.class);
    setChildrenFonts( component.getFont());
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetFeature#setFontStyles(java.util.EnumSet)
   */
  @Override
  public void setFontStyles( EnumSet<FontStyle> styles)
  {
    JComponent component = xidget.getFeature( JComponent.class);
    setChildrenFonts( component.getFont());
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetFeature#setFontSize(double)
   */
  @Override
  public void setFontSize( double size)
  {
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
  
  private IXidget xidget;
}
