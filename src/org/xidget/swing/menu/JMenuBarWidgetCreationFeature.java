/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JMenuBarWidgetCreationFeature.java
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

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import org.xidget.IXidget;
import org.xidget.swing.feature.SwingWidgetCreationFeature;

/**
 * An implementation of IWidgetCreationFeature for the Swing JMenuBar widget. 
 */
public class JMenuBarWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public JMenuBarWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    jMenuBar = new JMenuBar();
    return jMenuBar;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jMenuBar};
  }
  
  /**
   * Returns the JMenuBar that was created.
   * @return Returns the JMenuBar that was created.
   */
  public JMenuBar getJMenuBar()
  {
    return jMenuBar;
  }
  
  private JMenuBar jMenuBar;
}
