/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JAppletWidgetCreationFeature.java
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
package org.xidget.swing.applet;

import javax.swing.JApplet;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;

/**
 * An implementation of IWidgetCreationFeature which does nothing except export the JApplet widget.
 */
public class JAppletWidgetCreationFeature implements IWidgetCreationFeature
{
  public JAppletWidgetCreationFeature( IXidget xidget, JApplet applet)
  {
    this.applet = applet;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#createWidget()
   */
  public void createWidgets()
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidgets()
  {
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { applet};
  }

  /**
   * Returns the JApplet created for the xidget.
   * @return Returns the JApplet created for the xidget.
   */
  public JApplet getJApplet()
  {
    return applet;
  }

  private JApplet applet;
}
