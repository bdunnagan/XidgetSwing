/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.line;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import org.xidget.chart.Point;
import org.xidget.chart.Scale;
import org.xidget.chart.Scale.Tick;
import org.xidget.ifeature.chart.IPlotFeature;

/**
 * A custom widget that plots points on a two dimensional graph.  
 */
@SuppressWarnings("serial")
public class LineChart extends JPanel implements IPlotFeature
{
  public LineChart()
  {
    axes = new HashMap<String, Axis>();
    points = new ArrayList<Point>();
    setBackground( Color.white);
    
    gridColor = new Color( 0, 0, 0, 16);
    
    plotForeground = Color.red;
    plotBackground = new Color( 128, 128, 255, 128);
    plotArea = new Path2D.Double();    
    plotLine = new Path2D.Double();    
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
   * @see org.xidget.ifeature.IPointsFeature#updatePoint(org.xidget.chart.Point)
   */
  @Override
  public void updatePoint( Point point)
  {
    if ( minX > point.coords[ 0] || minY > point.coords[ 1] || maxX < point.coords[ 0] || maxY < point.coords[ 1]) findExtrema();
    repaint();
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
    
    int width = getWidth() - 1;
    int height = getHeight() - 1;
    
    // draw gradient background
//    GradientPaint paint = new GradientPaint( 0, 0, Color.white, 0, height, Color.lightGray);
//    g2d.setPaint( paint);
//    g2d.fillRect( 0, 0, width, height);
    
    // set grid color
    g2d.setColor( gridColor);
    
    // draw vertical grid-lines
    Scale xscale = xaxis.getScale();
    if ( xscale != null)
    {
      for( Tick tick: xscale.getTicks())
      {
        int x = (int)Math.round( xscale.plot( tick.value) * width);
        g2d.drawLine( x, 0, x, height);
      }
    }
    
    // draw horizontal grid-lines
    Scale yscale = yaxis.getScale();
    if ( yscale != null)
    {
      for( Tick tick: yscale.getTicks())
      {
        int y = (int)Math.round( yscale.plot( tick.value) * height);
        g2d.drawLine( 0, y, width, y);
      }
    }
    
    // compute graph shape
    plotLine.reset();
    plotArea.reset();
    double x = 0;
    double y = 0;
    for( int i=0; i<points.size(); i++)
    {
      Point point = points.get( i);
      x = (double)( xscale.plot( point.coords[ 0]) * width);
      y = (double)( height - yscale.plot( point.coords[ 1]) * height);
    
      if ( i == 0)
      {
        plotLine.moveTo( x, y);
        plotArea.moveTo( x, height);
        plotArea.lineTo( x, y);
      }
      else
      {
        plotLine.lineTo( x, y);
        plotArea.lineTo( x, y);
      }
    }
    
    plotArea.lineTo( x, height);
    plotArea.closePath();
    g2d.setColor( plotBackground);
    g2d.fill( plotArea);
    
    g2d.setColor( plotForeground);
    g2d.draw( plotLine);
  }

  private List<Point> points;
  private double minX, minY;
  private double maxX, maxY;
  private Color gridColor;
  
  private Color plotForeground;
  private Color plotBackground;
  private Path2D.Double plotLine;
  private Path2D.Double plotArea;
}
