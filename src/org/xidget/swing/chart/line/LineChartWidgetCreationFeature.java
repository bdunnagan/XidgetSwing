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
package org.xidget.swing.chart.line;

import javax.swing.JComponent;

import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetCreationFeature which creates a Plot2D widget.
 */
public class LineChartWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public LineChartWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    chart = new LineChart();
    findGraphAxes( chart);
    return chart;
  }

  /**
   * Find the rulers and associate to the line chart.
   * @param chart The line chart.
   */
  private void findGraphAxes( LineChart chart)
  {
    Creator creator = Creator.getInstance();
    IModelObject parent = xidget.getConfig().getParent();
    
    for( IModelObject node: parent.getChildren( "ruler"))
    {
      IXidget xidget = creator.findXidget( node);
      if ( xidget != null)
      {
        Axis axis = xidget.getFeature( Axis.class);
        String style = Xlate.get( node, "style", "");
        chart.addAxis( style, axis);
      }
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { chart};
  }

  /**
   * @return Returns the component that was created.
   */
  public JComponent getComponent()
  {
    return chart;
  }
    
  /**
   * Returns the LineChart widget which may be different from the widget returned
   * by the <code>getWidget</code> method which will return a container if
   * a label is defined.
   * @return Returns the text widget.
   */
  public LineChart getLineChart()
  {
    return chart;
  }

  private LineChart chart;
}
