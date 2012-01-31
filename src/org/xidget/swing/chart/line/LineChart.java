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
import java.util.List;
import javax.swing.JPanel;
import org.xidget.Creator;
import org.xidget.chart.Plot;
import org.xidget.chart.Point;
import org.xidget.chart.Scale;
import org.xidget.chart.Scale.Tick;
import org.xidget.ifeature.IColorFeature;
import org.xidget.ifeature.chart.IPlotFeature;
import org.xidget.swing.Toolkit;

/**
 * A custom widget that plots points on a two dimensional graph.  
 */
@SuppressWarnings("serial")
public class LineChart extends JPanel implements IPlotFeature
{
  public LineChart()
  {
    plots = new ArrayList<Plot>();
    plotArea = new Path2D.Double();    
    plotLine = new Path2D.Double();    
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#addPlot(org.xidget.chart.Plot)
   */
  @Override
  public void addPlot( Plot plot)
  {
    plots.add( plot);
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#removePlot(org.xidget.chart.Plot)
   */
  @Override
  public void removePlot( Plot plot)
  {
    plots.remove( plot);
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateForeground(org.xidget.chart.Plot, java.lang.String)
   */
  @Override
  public void updateForeground( Plot plot, String color)
  {
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateBackground(org.xidget.chart.Plot, java.lang.String)
   */
  @Override
  public void updateBackground( Plot plot, String color)
  {
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#addPoint(org.xidget.chart.Plot, int, org.xidget.chart.Point)
   */
  @Override
  public void addPoint( Plot plot, int index, Point point)
  {
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#removePoint(org.xidget.chart.Plot, int)
   */
  @Override
  public void removePoint( Plot plot, int index)
  {
    repaint( plot.getPoints().get( index));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateCoords(org.xidget.chart.Point, double[])
   */
  @Override
  public void updateCoords( Point point, double[] coords)
  {
    if ( point.coords != null) repaint( point);
    point.coords = coords;
    if ( point.coords != null) repaint( point);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateCoord(org.xidget.chart.Point, int, double)
   */
  @Override
  public void updateCoord( Point point, int coordinate, double value)
  {
    repaint( point);
    point.coords[ coordinate] = value;
    repaint( point);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateLabel(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateLabel( Point point, String label)
  {
    repaint( point);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateForeground(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateForeground( Point point, String fcolor)
  {
    repaint( point);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateBackground(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateBackground( Point point, String bcolor)
  {
    repaint( point);
  }
  
  /**
   * Repaint the specified point and connecting lines.
   * @param point The point to repaint.
   */
  private void repaint( Point point)
  {
    repaint();
//    if ( xAxis == null || yAxis == null) return;
//    
//    Scale xScale = xAxis.getScale();
//    Scale yScale = yAxis.getScale();
//    if ( xScale == null || yScale == null) return;
//    
//    int width = getWidth() - 1;
//    int height = getHeight() - 1;
//    
//    // repaint point
//    int x1 = (int)(xScale.plot( point.coords[ 0]) * width);
//    int y1 = (int)(yScale.plot( point.coords[ 1]) * height);
//    repaint( x1-5, y1-5, 10, 10);
//    
//    // repaint line to the new point
//    if ( point.prev != null)
//    {
//      int x0 = (int)(xScale.plot( point.prev.coords[ 0]) * width);
//      int y0 = (int)(yScale.plot( point.prev.coords[ 1]) * height);
//      repaint( x0, y0, (x1-x0)+2, (y1-y0)+2);
//    }
//    
//    // repaint line from the new point
//    if ( point.next != null)
//    {
//      int x0 = (int)(xScale.plot( point.next.coords[ 0]) * width);
//      int y0 = (int)(yScale.plot( point.next.coords[ 1]) * height);
//      repaint( x1, y1, (x0-x1)+2, (y0-y1)+2);
//    }
  }
  
  /**
   * Add an axis to the graph.
   * @param name The name.
   * @param axis The axis widget.
   */
  public void addAxis( String name, Axis axis)
  {
    if ( name.equals( "horizontal")) xAxis = axis;
    if ( name.equals( "vertical")) yAxis = axis;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @SuppressWarnings("unchecked")
  @Override
  protected void paintComponent( Graphics g) 
  {
    super.paintComponent( g);
    
    if ( xAxis == null || yAxis == null) return;
    
    Graphics2D g2d = (Graphics2D)g;
    g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    int width = getWidth() - 1;
    int height = getHeight() - 1;
    
    Toolkit toolkit = (Toolkit)Creator.getToolkit();
    IColorFeature<Color> colorFeature = toolkit.getFeature( IColorFeature.class);
    
    // draw gradient background
//    GradientPaint paint = new GradientPaint( 0, 0, Color.white, 0, height, Color.lightGray);
//    g2d.setPaint( paint);
//    g2d.fillRect( 0, 0, width, height);
    
    // set grid color
    g2d.setColor( getForeground());
    
    // draw vertical grid-lines
    Scale xscale = xAxis.getScale();
    if ( xscale == null) return;
    
    for( Tick tick: xscale.getTicks())
    {
      int x = (int)Math.round( xscale.plot( tick.value) * width);
      g2d.drawLine( x, 0, x, height);
    }
    
    // draw horizontal grid-lines
    Scale yscale = yAxis.getScale();
    if ( yscale == null) return;
    
    for( Tick tick: yscale.getTicks())
    {
      int y = (int)Math.round( yscale.plot( tick.value) * height);
      g2d.drawLine( 0, y, width, y);
    }
    
    // compute graph shape
    plotLine.reset();
    plotArea.reset();
    double x = 0;
    double y = 0;
    for( Plot plot: plots)
    {
      List<Point> points = plot.getPoints();
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
      
      g2d.setColor( colorFeature.getColor( plot.getBackground()));
      g2d.fill( plotArea);
      
      g2d.setColor( colorFeature.getColor( plot.getForeground()));
      g2d.draw( plotLine);
    }
  }

  private Axis xAxis;
  private Axis yAxis;
  
  private List<Plot> plots;
  
  private Path2D.Double plotLine;
  private Path2D.Double plotArea;
}
