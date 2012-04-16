/*
 * Stonewall Networks, Inc.
 *
 *   Project: TopologyPlugin
 *   Author:  bdunnagan
 *   Date:    Jun 22, 2006
 *
 * Copyright 2006.  Stonewall Networks, Inc.
 */
package org.xidget.swing.shape;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of a natural cubic spline through any number of control points.
 */
public class Spline
{
  public Spline()
  {
    controls = new ArrayList<Point2D.Double>();
    points = new ArrayList<Point2D.Double>();
  }

  /**
   * Add a control point to the line.
   * @param x The x-coordinate.
   * @param y The y-coordinate.
   */
  public void addControl( double x, double y)
  {
    xs = null; ys = null;
    controls.add( new Point2D.Double( x, y));
  }
  
  /**
   * Remove a control point from the line.
   * @param index The index of the point.
   */
  public void removeControl( int index)
  {
    xs = null; ys = null;
    points.clear();
    controls.remove( index);
  }
  
  /**
   * Create the set of points used to draw the spline.
   */
  public List<Point2D.Double> interpolate()
  {
    return interpolate( 0.6);
  }
  
  /**
   * Create the set of points used to draw the spline.
   * @param error The maximum allowable error.
   */
  public List<Point2D.Double> interpolate( double error)
  {
    int count = controls.size();
    if ( xs == null)
    {
      xs = new double[ count];
      ys = new double[ count];
      for( int i=0; i<count; i++)
      {
        Point2D.Double point = controls.get( i);
        xs[ i] = point.x;
        ys[ i] = point.y;
      }
    }

    points.clear();
    if ( count >= 2) 
    {
      Cubic[] mX = naturalCubic( count-1, xs);
      Cubic[] mY = naturalCubic( count-1, ys);
      
      double x1 = mX[ 0].eval( 0);
      double y1 = mY[ 0].eval( 0);
      points.add( new Point2D.Double( x1, y1));
      for( int i = 0; i < mX.length; i++) 
      {
        double x2 = mX[ i].eval( 1);
        double y2 = mY[ i].eval( 1);
        interpolate( mX[ i], mY[ i], 0, 1, x1, y1, x2, y2, error);
        x1 = x2; y1 = y2;
      }
    }
    
    return points;
  }
  
  /**
   * Recursively render the section of the cubic spline segment with specified index until
   * the error falls below the threshold.  The error is defined as the square of the distance
   * between the interpolating line segment and the cubic spline segment evaluation.
   * @param mX The x-coordinate cubic segment.
   * @param mY The y-coordinate cubic segment.
   * @param u The starting parameter.
   * @param du The parameter increment.
   * @param x1 The x-coordinate of the evaluation at u.
   * @param y1 The y-coordinate of the evaluation at u.
   * @param x2 The x-coordinate of the evaluation at u+du.
   * @param y2 The y-coordinate of the evaluation at u+du.
   * @param error The maximum allowable error.
   */
  private void interpolate( Cubic mX, Cubic mY, double u, double du, double x1, double y1, double x2, double y2, double error)
  {
    // calculate approximation
    double hdu = du / 2;
    double mx = mX.eval( u+hdu);
    double my = mY.eval( u+hdu);

    // calculate error
    double dx1 = x2 - x1;
    double dy1 = y2 - y1;
    double dx2 = x1 - mx;
    double dy2 = y1 - my;
    double d = Math.abs( (dx1*dy2 - dx2*dy1) / Math.sqrt( dx1*dx1 + dy1*dy1));
    if ( d > error)
    {
      interpolate( mX, mY, u, hdu, x1, y1, mx, my, error);
      interpolate( mX, mY, u+hdu, hdu, mx, my, x2, y2, error);
    } 
    else
    {
      points.add( new Point2D.Double( x2, y2));
    }
  }
  
  /**
   * Calculate the natural cubic spline over the given set of points.
   * (Taken from http://www.cse.unsw.edu.au/~lambert/splines/source.html)
   */
  private Cubic[] naturalCubic( int n, double[] x) 
  {
    double[] gamma = new double[n+1];
    double[] delta = new double[n+1];
    double[] d = new double[n+1];
    int i;
    
    //
    // We solve the equation
    // [2 1       ] [D[0]]   [3(x[1] - x[0])  ]
    // |1 4 1     | |D[1]|   |3(x[2] - x[0])  |
    // |  1 4 1   | | .  | = |      .         |
    // |    ..... | | .  |   |      .         |
    // |     1 4 1| | .  |   |3(x[n] - x[n-2])|
    // [       1 2] [D[n]]   [3(x[n] - x[n-1])]
    //       
    // by using row operations to convert the matrix to upper triangular
    // and then back sustitution.  The D[i] are the derivatives at the knots.
    //
    gamma[ 0] = 1d / 2d;
    for ( i = 1; i < n; i++)
    {
      gamma[ i] = 1 / (4 - gamma[ i - 1]);
    }
    gamma[ n] = 1 / (2 - gamma[ n - 1]);

    delta[ 0] = 3d * (x[ 1] - x[ 0]) * gamma[ 0];
    for ( i = 1; i < n; i++)
    {
      delta[ i] = (3d * (x[ i + 1] - x[ i - 1]) - delta[ i - 1]) * gamma[ i];
    }
    delta[ n] = (3d * (x[ n] - x[ n - 1]) - delta[ n - 1]) * gamma[ n];

    d[ n] = delta[ n];
    for ( i = n - 1; i >= 0; i--)
    {
      d[ i] = delta[ i] - gamma[ i] * d[ i + 1];
    }

    /* now compute the coefficients of the cubics */
    Cubic[] cubic = new Cubic[n];
    for ( i = 0; i < n; i++)
    {
      cubic[ i] = new Cubic( (double)x[ i], d[ i], 3d * (x[ i + 1] - x[ i]) - 2 * d[ i] - d[ i + 1], 2d * (x[ i] - x[ i + 1]) + d[ i] + d[ i + 1]);
    }
    return cubic;
  }
  
  private List<Point2D.Double> controls;
  private List<Point2D.Double> points;
  private double xs[];
  private double ys[];
}

/**
 * Utility class to handle basic cubic calculations.
 */
class Cubic 
{
  double a, b, c, d; /* a + b*u + c*u^2 +d*u^3 */

  public Cubic( double a, double b, double c, double d)
  {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }

  /**
   * Evaluate the cubic at the specified value.
   * @param u The value.
   * @return Returns the value of the cubic polynomial.
   */
  public double eval( double u) 
  {
    return (((d*u) + c)*u + b)*u + a;
  }
  
  /**
   * Evaluate the second derivative of the cubic at the specified value.
   * @param u The value.
   * @return Returns the second derivative of the cubic polynomial.
   */
  public double rate( double u)
  {
    return (6*d*u) + (2*c);
  }
}
