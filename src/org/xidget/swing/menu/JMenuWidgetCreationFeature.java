/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JMenuWidgetCreationFeature.java
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
package org.xidget.swing.menu;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import org.xidget.IXidget;
import org.xidget.ifeature.IIconFeature;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;

/**
 * An implementation of IWidgetCreationFeature for the Swing JPopupMenu widget. 
 */
public class JMenuWidgetCreationFeature extends SwingWidgetCreationFeature implements IIconFeature, ILabelFeature
{
  public JMenuWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    jMenu = new JMenu();    
    return jMenu;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jMenu};
  }
  
  /**
   * Returns the JPopupMenu that was created.
   * @return Returns the JPopupMenu that was created.
   */
  public JMenu getJMenu()
  {
    return jMenu;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    jMenu.setIcon( (Icon)icon);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#setText(java.lang.String)
   */
  public void setText( String text)
  {
    jMenu.setText( text);
  }

  private JMenu jMenu;
}
