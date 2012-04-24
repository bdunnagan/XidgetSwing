/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * SwingIconFeature.java
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
package org.xidget.swing.form;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import org.xidget.IXidget;
import org.xidget.ifeature.IImageFeature;
import org.xidget.swing.tabs.CustomTab;

/**
 * An implementation of IImageFeature for tab icons.
 */
public class TabImageFeature implements IImageFeature
{
  public TabImageFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IImageFeature#setIcon(java.lang.Object)
   */
  public void setImage( Object image)
  {
    IXidget parent = xidget.getParent();
    if ( parent == null) return;
    
    if ( parent.getConfig().isType( "tabs"))
    {
      JTabbedPane jtabbedPane = parent.getFeature( JTabbedPane.class);
      int index = parent.getChildren().indexOf( xidget);
      CustomTab tab = (CustomTab)jtabbedPane.getTabComponentAt( index);
      tab.setIcon( new ImageIcon( (Image)image));
      //jtabbedPane.setIconAt( index, (Icon)icon);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IImageFeature#setImageHover(java.lang.Object)
   */
  @Override
  public void setImageHover( Object image)
  {
    throw new UnsupportedOperationException( "Xidget does not support hover images.");
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IImageFeature#setImagePress(java.lang.Object)
   */
  @Override
  public void setImagePress( Object image)
  {
    throw new UnsupportedOperationException( "Xidget does not support hover images.");
  }

  private IXidget xidget;
}
