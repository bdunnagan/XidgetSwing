/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JLabelWidgetCreationFeature.java
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
package org.xidget.swing.label;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.xidget.IXidget;
import org.xidget.ifeature.IIconFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;

/**
 * An implementation of IWidgetCreationFeature for the Swing JLabel widget.
 */
public class JLabelWidgetCreationFeature extends SwingWidgetCreationFeature implements IIconFeature
{
  public JLabelWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    jLabel.setIcon( (Icon)icon);
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    jLabel = new JLabel();
    return jLabel;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jLabel};
  }

  /**
   * Returns the JLabel.
   * @return Returns the JLabel.
   */
  public JLabel getJLabel()
  {
    return jLabel;
  }
  
  private JLabel jLabel;
}
