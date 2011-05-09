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
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetCreationFeature which creates a Graph2D widget.
 */
public class GraphWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public GraphWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    graph = new Graph2D();
    findGraphAxes( graph);
    return graph;
  }

  /**
   * Find the graph axes and associate to the graph.
   * @param graph The graph.
   */
  private void findGraphAxes( Graph2D graph)
  {
    IModelObject parent = xidget.getConfig().getParent();
    
    for( IModelObject node: parent.getChildren( "xaxis"))
    {
      IXidget xidget = (IXidget)node.getAttribute( "instance");
      if ( xidget != null)
      {
        Axis axis = xidget.getFeature( Axis.class);
        String name = Xlate.get( node, "name", "x");
        graph.addAxis( name, axis);
      }
    }
    
    for( IModelObject node: parent.getChildren( "yaxis"))
    {
      IXidget xidget = (IXidget)node.getAttribute( "instance");
      if ( xidget != null)
      {
        Axis axis = xidget.getFeature( Axis.class);
        String name = Xlate.get( node, "name", "y");
        graph.addAxis( name, axis);
      }
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { graph};
  }

  /**
   * @return Returns the component that was created.
   */
  public JComponent getComponent()
  {
    return graph;
  }
    
  /**
   * Returns the text widget which may be different from the widget returned
   * by the <code>getWidget</code> method which will return a container if
   * a label is defined.
   * @return Returns the text widget.
   */
  public Graph2D getGraph()
  {
    return graph;
  }

  private Graph2D graph;
}
