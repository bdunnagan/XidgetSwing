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
package org.xidget.swing.chart.pie;

import javax.swing.JComponent;

import org.xidget.IXidget;
import org.xidget.swing.feature.SwingWidgetCreationFeature;

/**
 * An implementation of IWidgetCreationFeature which creates a PieChart widget.
 */
public class PieChartWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public PieChartWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    pie = new PieChart();
    return pie;
  }

  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { pie};
  }

  /**
   * @return Returns the component that was created.
   */
  public JComponent getComponent()
  {
    return pie;
  }
    
  /**
   * Returns the PieChart widget which may be different from the widget returned
   * by the <code>getWidget</code> method which will return a container if
   * a label is defined.
   * @return Returns the text widget.
   */
  public PieChart getPieChart()
  {
    return pie;
  }

  private PieChart pie;
}
