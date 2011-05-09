/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JTextComponentWidgetCreationFeature.java
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
package org.xidget.swing.graph;

import javax.swing.JComponent;

import org.xidget.IXidget;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetCreationFeature which creates an Axis widget.
 */
public class YAxisWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public YAxisWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    boolean left = Xlate.get( xidget.getConfig(), "left", true);
    axis = new YAxis( left);
    return axis;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { axis};
  }

  /**
   * @return Returns the component that was created.
   */
  public JComponent getComponent()
  {
    return axis;
  }

  /**
   * @return Returns the Axis widget.
   */
  public Axis getAxis()
  {
    return axis;
  }
  
  private Axis axis;
}
