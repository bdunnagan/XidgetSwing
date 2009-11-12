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
package org.xidget.swing.feature;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import org.xidget.IXidget;
import org.xidget.ifeature.IIconFeature;

/**
 * An implementation of ITitleFeature for suitable for all Swing containers.
 */
public class SwingIconFeature implements IIconFeature
{
  public SwingIconFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    IXidget parent = xidget.getParent();
    if ( parent == null) return;
    
    if ( parent.getConfig().isType( "tabs"))
    {
      JTabbedPane jtabbedPane = parent.getFeature( JTabbedPane.class);
      int index = parent.getChildren().indexOf( xidget);
      jtabbedPane.setIconAt( index, (Icon)icon);
    }
  }

  private IXidget xidget;
}
