/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JLabelIconFeature.java
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
import javax.swing.JLabel;
import org.xidget.ifeature.IIconFeature;

/**
 * An implementation of IIconFeature which updates the icon of a JLabel. 
 */
public class JLabelIconFeature implements IIconFeature
{
  public JLabelIconFeature( JLabel jlabel)
  {
    this.jlabel = jlabel;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.feature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    jlabel.setIcon( (Icon)icon);
  }

  private JLabel jlabel;
}
