/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * ButtonLabelFeature.java
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
package org.xidget.swing.button;

import javax.swing.AbstractButton;

import org.xidget.IXidget;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.Bounds;

/**
 * An implementation of ILabelFeature for Swing AbstractButton widgets.
 */
public class ButtonLabelFeature implements ILabelFeature
{
  public ButtonLabelFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#getLabelWidth()
   */
  public int getLabelWidth()
  {
//    String type = Xlate.get( xidget.getConfig(), "type", "push");
//    if ( type.equals( "check") || type.equals( "radio"))
//    {
//      AbstractButton button = xidget.getFeature( AbstractButton.class);
//      Dimension size = button.getPreferredSize();
//      return size.width;
//    }
    return 0;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#setWidth(int)
   */
  public void setLabelWidth( int width)
  {
//    String type = Xlate.get( xidget.getConfig(), "type", "push");
//    if ( type.equals( "check") || type.equals( "radio"))
//    {
//      AbstractButton button = xidget.getFeature( AbstractButton.class);
//      Dimension size = button.getPreferredSize();
//      button.setIconTextGap( width - size.width);
//    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#setLabelText(java.lang.String)
   */
  public void setLabelText( String text)
  {
    AbstractButton button = xidget.getFeature( AbstractButton.class);
    if ( button != null) 
    {
      //
      // HACK: Original default bounds are overwritten by computed bounds!
      //
      IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
      Bounds bounds = widgetFeature.getDefaultBounds();
      widgetFeature.setDefaultBounds( bounds.x, bounds.y, -1, -1, false);
      
      button.setText( text);
    }
  }
  
  private IXidget xidget;
}
