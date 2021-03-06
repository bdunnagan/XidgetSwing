/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * AbstractButtonIconFeature.java
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

import java.awt.Image;
import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import org.xidget.IXidget;
import org.xidget.ifeature.IImageFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.Bounds;

/**
 * An implementation of IImageFeature which updates the image of an AbstractButton. 
 */
public class AbstractButtonImageFeature implements IImageFeature
{
  public AbstractButtonImageFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.feature.IImageFeature#setIcon(java.lang.Object)
   */
  public void setImage( Object image)
  {
    //
    // HACK: Original default bounds are overwritten by computed bounds!
    //
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = widgetFeature.getDefaultBounds();
    widgetFeature.setDefaultBounds( bounds.x, bounds.y, -1, -1, false);
    
    AbstractButton button = xidget.getFeature( AbstractButton.class);
    if ( button != null) button.setIcon( new ImageIcon( (Image)image));
    
    String text = button.getText();
    if ( text == null || text.length() == 0)
    {
      button.setMargin( new Insets( 0, 0, 0, 0));
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IImageFeature#setImageHover(java.lang.Object)
   */
  @Override
  public void setImageHover( Object image)
  {
    //
    // HACK: Original default bounds are overwritten by computed bounds!
    //
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = widgetFeature.getDefaultBounds();
    widgetFeature.setDefaultBounds( bounds.x, bounds.y, -1, -1, false);
    
    AbstractButton button = xidget.getFeature( AbstractButton.class);
    if ( button != null) button.setRolloverIcon( new ImageIcon( (Image)image));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IImageFeature#setImagePress(java.lang.Object)
   */
  @Override
  public void setImagePress( Object image)
  {
    //
    // HACK: Original default bounds are overwritten by computed bounds!
    //
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = widgetFeature.getDefaultBounds();
    widgetFeature.setDefaultBounds( bounds.x, bounds.y, -1, -1, false);
    
    AbstractButton button = xidget.getFeature( AbstractButton.class);
    if ( button != null) button.setPressedIcon( new ImageIcon( (Image)image));
  }

  private IXidget xidget;
}
