/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.graph;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.util.HashMap;
import java.util.Map;

import org.xidget.graph.PointStyles;
import org.xidget.graph.Point.Style;

/**
 * A class that returns a Shape
 */
public class PointShapes
{
  /**
   * Draw the specified shape on the specified device.
   * @param g The graphics device.
   * @param style The point style to draw.
   * @param x The x-coordinate.
   * @param y The y-coordinate.
   */
  public static final void drawShape( Graphics2D g, Style style, double x, double y)
  {
    int ix = (int)x;
    int iy = (int)y;
    
    int[][] segments = PointStyles.getLineSegments( style);
    for( int i=0; i<segments.length; )
    {
      int x0 = segments[ i][ 0] + ix;
      int y0 = segments[ i][ 1] + iy;
      i++;
      
      int x1 = segments[ i][ 0] + ix;
      int y1 = segments[ i][ 1] + iy;
      i++;
      
      g.drawLine( x0, y0, x1, y1);
    }
  }
  
  /**
   * Returns a Polygon containing the specified point style.
   * @param style The point style.
   * @return Returns a Polygon containing the specified point style.
   */
  public static final GeneralPath getShape( Style style)
  {
    if ( shapes == null) shapes = new HashMap<Style, GeneralPath>();
    
    GeneralPath shape = shapes.get( style);
    if ( shape == null)
    {
      shape = new GeneralPath();
      int[][] segments = PointStyles.getLineSegments( style);
      for( int i=0; i<segments.length; i+=2)
      {
        shape.moveTo( segments[ i][ 0], segments[ i][ 1]);
        shape.lineTo( segments[ i+1][ 0], segments[ i+1][ 1]);
      }
      shapes.put( style, shape);
    }
    
    return shape;
  }
  
  private static Map<Style, GeneralPath> shapes;
}
