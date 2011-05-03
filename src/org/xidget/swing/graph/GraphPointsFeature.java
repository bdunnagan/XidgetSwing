/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.graph;

import org.xidget.IXidget;
import org.xidget.graph.Point;
import org.xidget.ifeature.IPointsFeature;

/**
 * An implementation of IPointsFeature for plotting points on a Graph2D widget.
 */
public class GraphPointsFeature implements IPointsFeature
{
  public GraphPointsFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#add(org.xidget.graph.Point)
   */
  @Override
  public void add( Point point)
  {
    Graph2D graph = xidget.getFeature( Graph2D.class);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#add(int, org.xidget.graph.Point)
   */
  @Override
  public void add( int index, Point point)
  {
    Graph2D graph = xidget.getFeature( Graph2D.class);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#remove(int)
   */
  @Override
  public void remove( int index)
  {
    Graph2D graph = xidget.getFeature( Graph2D.class);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#update(org.xidget.graph.Point)
   */
  @Override
  public void update( Point point)
  {
    Graph2D graph = xidget.getFeature( Graph2D.class);
  }
  
  private IXidget xidget;
}
