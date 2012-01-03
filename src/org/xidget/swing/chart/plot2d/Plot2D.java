/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.plot2d;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.xidget.chart.Point;
import org.xidget.chart.Scale;
import org.xidget.chart.Scale.Tick;
import org.xidget.ifeature.IPointsFeature;
import org.xidget.swing.chart.PointShapes;

/**
 * A custom widget that plots points on a two dimensional graph.  
 */
@SuppressWarnings("serial")
public class Plot2D extends JPanel implements IPointsFeature
{
  public Plot2D()
  {
    axes = new HashMap<String, Axis>();
    points = new ArrayList<Point>();
    setBackground( Color.white);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#add(org.xidget.graph.Point)
   */
  @Override
  public void add( Point point)
  {
    add( points.size(), point);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#add(int, org.xidget.graph.Point)
   */
  @Override
  public void add( int index, Point point)
  {
    if ( index < 0 || index > points.size()) return;
    
    if ( points.size() == 0)
    {
      minX = maxX = point.coords[ 0];
      minY = maxY = point.coords[ 1];
    }
    
    if ( index > 0)
    {
      Point prev = points.get( index - 1);
      if ( prev.next != null) prev.next.prev = point;
      prev.next = point;
      point.prev = prev;
      point.next = prev.next;
    }
    
    if ( minX > point.coords[ 0]) minX = point.coords[ 0];
    if ( minY > point.coords[ 1]) minY = point.coords[ 1];
    if ( maxX < point.coords[ 0]) maxX = point.coords[ 0];
    if ( maxY < point.coords[ 1]) maxY = point.coords[ 1];
    
    for( Axis axis: axes.values())
    {
      if ( axis instanceof XAxis)
      {
        axis.setExtrema( minX, maxX);
      }
      else
      {
        axis.setExtrema( minY, maxY);
      }
    }
    
    points.add( index, point);
    repaint( point);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#update(org.xidget.graph.Point, int, double)
   */
  @Override
  public void update( Point point, int coordinate, double value)
  {
    repaint( point);

    point.coords[ coordinate] = value;
    if ( minX > point.coords[ 0] || minY > point.coords[ 1] || maxX < point.coords[ 0] || maxY < point.coords[ 1])
    {
      findExtrema();
    }
    
    repaint( point);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#remove(int)
   */
  @Override
  public void remove( int index)
  {
    if ( index < 0 || index >= points.size()) return;
    
    Point point = points.remove( index);
    if ( point.prev != null) point.prev.next = point.next;
    if ( point.next != null) point.next.prev = point.prev;
    
    if ( minX > point.coords[ 0] || minY > point.coords[ 1] || maxX < point.coords[ 0] || maxY < point.coords[ 1])
    {
      findExtrema();
    }
    
    for( Axis axis: axes.values())
    {
      if ( axis instanceof XAxis)
      {
        axis.setExtrema( minX, maxX);
      }
      else
      {
        axis.setExtrema( minY, maxY);
      }
    }
    
    repaint( point);
  }
  
  /**
   * Find the minimum and maximum values of the coordinates in the point list.
   */
  public void findExtrema()
  {
    if ( points == null || points.size() == 0)
    {
      minX = 0;
      minY = 0;
      maxX = 0;
      maxY = 0;
      return;
    }
    
    minX = maxX = points.get( 0).coords[ 0];
    minY = maxY = points.get( 0).coords[ 1];
    for( int i=1; i<points.size(); i++)
    {
      Point point = points.get( i);
      if ( point.coords[ 0] < minX) minX = point.coords[ 0];
      if ( point.coords[ 1] < minY) minY = point.coords[ 1];
      if ( point.coords[ 0] > maxX) maxX = point.coords[ 0];
      if ( point.coords[ 1] > maxY) maxY = point.coords[ 1];
    }
  }
  
  /**
   * Repaint the specified point and connecting lines.
   * @param point The point to repaint.
   */
  private void repaint( Point point)
  {
    Axis xaxis = axes.get( "x");
    Axis yaxis = axes.get( "y");
    if ( xaxis == null || yaxis == null) return;
    
    Scale xscale = xaxis.getScale();
    Scale yscale = yaxis.getScale();
    if ( xscale == null || yscale == null) return;
    
    int width = getWidth() - 1;
    int height = getHeight() - 1;
    
    // repaint point
    int x1 = (int)(xscale.plot( point.coords[ 0]) * width);
    int y1 = (int)(yscale.plot( point.coords[ 1]) * height);
    repaint( x1-5, y1-5, 10, 10);
    
    // repaint line to the new point
    if ( point.prev != null)
    {
      int x0 = (int)(xscale.plot( point.prev.coords[ 0]) * width);
      int y0 = (int)(yscale.plot( point.prev.coords[ 1]) * height);
      repaint( x0, y0, (x1-x0)+2, (y1-y0)+2);
    }
    
    // repaint line from the new point
    if ( point.next != null)
    {
      int x0 = (int)(xscale.plot( point.next.coords[ 0]) * width);
      int y0 = (int)(yscale.plot( point.next.coords[ 1]) * height);
      repaint( x1, y1, (x0-x1)+2, (y0-y1)+2);
    }
  }
  
  /**
   * Add an axis to the graph.
   * @param name The name.
   * @param axis The axis widget.
   */
  public void addAxis( String name, Axis axis)
  {
    axes.put( name, axis);
  }
  
  /**
   * Called when a scale widget is resized.
   * @param axis The axis that was resized.
   */
  public void axisResized( String axis)
  {
    repaint();
  }
      
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent( Graphics g) 
  {
    super.paintComponent( g);
    
//    FontMetrics metrics = g.getFontMetrics();
    Graphics2D g2d = (Graphics2D)g;
    g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    Axis xaxis = axes.get( "x");
    Axis yaxis = axes.get( "y");
    if ( xaxis == null || yaxis == null) return;
    
    // draw vertical grid-lines
    Scale xscale = xaxis.getScale();
    int graphHeight = getHeight();
    if ( xGrid != null)
    {
      g2d.setColor( Color.lightGray);
      for( Tick tick: xGrid)
      {
        int x = (int)Math.round( xscale.plot( tick.value));
        g2d.drawLine( x, 0, x, graphHeight);
      }
    }
    
    // draw horizontal grid-lines
    Scale yscale = yaxis.getScale();
    int graphWidth = getWidth();
    if ( yGrid != null)
    {
      g2d.setColor( Color.lightGray);
      for( Tick tick: yGrid)
      {
        int y = (int)Math.round( yscale.plot( tick.value));
        g2d.drawLine( 0, y, graphWidth, y);
      }
    }
    
    // draw graph
    int prevX = 0;
    int prevY = 0;
    int width = getWidth() - 1;
    int height = getHeight() - 1;
    for( int i=0; i<points.size(); i++)
    {
      Point point = points.get( i);
      double x = xscale.plot( point.coords[ 0]) * width;
      double y = height - yscale.plot( point.coords[ 1]) * height;
      
      g2d.setColor( Color.black);
      if ( lines && i > 0)
      {
        g2d.drawLine( prevX, prevY, (int)x, (int)y);
        prevX = (int)x;
        prevY = (int)y;
      }
      
      PointShapes.drawShape( g2d, point.shape, x, y);
      
//      if ( point.color != null) g2d.setColor( point.color);
//      
//      int textX = 0;
//      if ( point.image != null)
//      {
//        int width = point.image.getWidth( this);
//        int height = point.image.getHeight( this);
//        int imageX = (int)Math.round( x - (width / 2.0)); 
//        int imageY = (int)Math.round( y - (height / 2.0));
//        g2d.drawImage( point.image, imageX, imageY, this);
//        textX = imageX + width + 2;
//      }
//      else
//      {
//        int ix = (int)x;
//        int iy = (int)y;
//        g2d.drawRect( ix, iy, 1, 1);
//        textX = ix + 1;
//      }
//      
//      if ( point.label != null)
//      {
//        int labelY = (int)(point.y + metrics.getAscent() + 1);
//        g2d.drawImage( point.image, textX, labelY, this);
//      }
    }
  }

  private List<Tick> xGrid;
  private List<Tick> yGrid;
  private List<Point> points;
  private boolean lines;
  private double minX, minY;
  private double maxX, maxY;
  private Map<String, Axis> axes;
}
