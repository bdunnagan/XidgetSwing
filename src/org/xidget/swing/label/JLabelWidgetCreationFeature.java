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

import java.awt.Image;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.xidget.IXidget;
import org.xidget.ifeature.IImageFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xidget.swing.util.IconCache;

/**
 * An implementation of IWidgetCreationFeature for the Swing JLabel widget.
 */
public class JLabelWidgetCreationFeature extends SwingWidgetCreationFeature implements IImageFeature
{
  public JLabelWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IImageFeature#setIcon(java.lang.Object)
   */
  public void setImage( Object image)
  {
    jLabel.setIcon( IconCache.getInstance().getIcon( (Image)image));
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    jLabel = new TransparentLabel();
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
