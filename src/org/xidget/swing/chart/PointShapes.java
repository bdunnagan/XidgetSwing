/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart;

import java.awt.Graphics2D;

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
  public static final void drawShape( Graphics2D g, int[][] segments, double x, double y)
  {
    int ix = (int)x;
    int iy = (int)y;
    
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
}
