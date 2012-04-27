/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.line;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import org.xidget.Creator;
import org.xidget.chart.IScale;
import org.xidget.chart.IScale.Tick;
import org.xidget.chart.Plot;
import org.xidget.chart.Point;
import org.xidget.ifeature.IColorFeature;
import org.xidget.ifeature.chart.IPlotFeature;
import org.xidget.swing.Toolkit;

/**
 * A custom widget that plots points on a two dimensional graph.  
 */
@SuppressWarnings("serial")
public class BarChart extends JPanel implements IPlotFeature
{
  public BarChart( boolean horizontal)
  {
    this.horizontal = horizontal;
    this.plots = new ArrayList<Plot>();
    this.plotBar = new Path2D.Double();
    this.strokes = new HashMap<Plot, Stroke>();
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
   * @see org.xidget.ifeature.chart.IPlotFeature#updateStrokeWidth(org.xidget.chart.Plot, double)
   */
  @Override
  public void updateStrokeWidth( Plot plot, double value)
  {
    BasicStroke stroke = new BasicStroke( (float)value, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
    strokes.put( plot, stroke);
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#addPoint(org.xidget.chart.Plot, int, org.xidget.chart.Point)
   */
  @Override
  public void addPoint( Plot plot, int index, Point point)
  {
    findExtrema();
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#removePoint(org.xidget.chart.Plot, int)
   */
  @Override
  public void removePoint( Plot plot, int index)
  {
    findExtrema();
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
    findExtrema();
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
    findExtrema();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateLabel(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateLabel( Point point, String label)
  {
    point.label = label;
    repaint();
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
  
  private void findExtrema()
  {
    max = 0;
    for( Plot plot: plots)
    {
      for( Point point: plot.getPoints())
      {
        if ( point.coords[ 0] > max) max = point.coords[ 0];
      }
    }
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent( Graphics g) 
  {
    super.paintComponent( g);
    
    Graphics2D g2d = (Graphics2D)g;
    g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    if ( horizontal) paintHorizontal( g2d); else paintVertical( g2d);
  }
  
  /**
   * Paint bar chart with horizontal bars.
   * @param g2d The graphics.
   */
  @SuppressWarnings("unchecked")
  private void paintHorizontal( Graphics2D g2d)
  {
    int width = getWidth() - 1 - LineChart.padWidth;
    int height = getHeight() - 1 - LineChart.padHeight;
    
    AffineTransform shift = AffineTransform.getTranslateInstance( LineChart.padWidth / 2f, LineChart.padHeight / 2f);
    AffineTransform transform = g2d.getTransform();
    if ( transform != null) transform.concatenate( shift); else transform = shift;
    g2d.setTransform( transform);
    
    Toolkit toolkit = (Toolkit)Creator.getToolkit();
    IColorFeature<Color, Graphics2D> colorFeature = toolkit.getFeature( IColorFeature.class);
    
    // draw gradient background
//    GradientPaint paint = new GradientPaint( 0, 0, Color.white, 0, height, Color.lightGray);
//    g2d.setPaint( paint);
//    g2d.fillRect( 0, 0, width, height);
    
    // set grid color
    g2d.setColor( getForeground());
    
    FontMetrics metrics = g2d.getFontMetrics();
    
    // draw vertical grid-lines
    IScale xscale = (xAxis != null)? xAxis.getScale(): null;
    if ( xscale != null)
    {
      for( Tick tick: xscale.getTicks())
      {
        int x = (int)Math.round( xscale.plot( tick.value) * width);
        g2d.drawLine( x, 0, x, height);
      }
    }
    else
    {
      for( int i=0; i<max; i++)
      {
        int x = (int)Math.round( i * width / max);
        g2d.drawLine( x, 0, x, height);
      }
    }
        
    // ick... get number of points... i hate this schema
    int n = 0;
    for( Plot plot: plots)
      n += plot.getPoints().size();
    
    // horizontal orientation
    double hbt = height / (n + 1);
    double hb = hbt * 0.75;
    double y = hbt;
    for( Plot plot: plots)
    {
      for( Point point: plot.getPoints())
      {
        plotBar.reset();
  
        double y0 = y - (hb / 2);
        double y1 = y + (hb / 2);
        double x = (xscale != null)? xscale.plot( point.coords[ 0]) * width: point.coords[ 0] * width / max;
        
        if ( x > 0)
        {
          plotBar.moveTo( 0, y0);
          plotBar.lineTo( x, y0);
          plotBar.lineTo( x, y1);
          plotBar.lineTo( 0, y1);
          plotBar.lineTo( 0, y0);
  
          // draw bar
          g2d.setColor( getBackground());
          colorFeature.applyColor( plot.getBackground(), g2d, width, height);
          g2d.fill( plotBar);
          
          Stroke stroke = strokes.get( plot);
          if ( stroke != null) g2d.setStroke( stroke);
          colorFeature.applyColor( plot.getForeground(), g2d, width, height);
          g2d.draw( plotBar);
        }
        
        // draw text
        double textWidth = metrics.stringWidth( point.label);
        double textX = (x / 2) - (textWidth / 2);
        if ( textWidth > (x + textPad * 2)) textX = x + textPad;
        g2d.drawString( point.label, (float)textX, (float)(y + metrics.getAscent() / 2));
        
        y += hbt;
      }
    }    
  }

  /**
   * Paint bar chart with vertical bars.
   * @param g2d The graphics.
   */
  @SuppressWarnings("unchecked")
  private void paintVertical( Graphics2D g2d)
  {
    int width = getWidth() - 1 - LineChart.padWidth;
    int height = getHeight() - 1 - LineChart.padHeight;
    
    AffineTransform shift = AffineTransform.getTranslateInstance( LineChart.padWidth / 2f, LineChart.padHeight / 2f);
    AffineTransform transform = g2d.getTransform();
    if ( transform != null) transform.concatenate( shift); else transform = shift;
    g2d.setTransform( transform);
    
    Toolkit toolkit = (Toolkit)Creator.getToolkit();
    IColorFeature<Color, Graphics2D> colorFeature = toolkit.getFeature( IColorFeature.class);
    
    // draw gradient background
//    GradientPaint paint = new GradientPaint( 0, 0, Color.white, 0, height, Color.lightGray);
//    g2d.setPaint( paint);
//    g2d.fillRect( 0, 0, width, height);
    
    // set grid color
    g2d.setColor( getForeground());
    
    FontMetrics metrics = g2d.getFontMetrics();
    height -= metrics.getHeight() + 3;
    
    // draw vertical grid-lines
    IScale yscale = (yAxis != null)? yAxis.getScale(): null;
    if ( yscale != null)
    {
      for( Tick tick: yscale.getTicks())
      {
        int y = (int)Math.round( yscale.plot( tick.value) * height);
        g2d.drawLine( 0, y, width, y);
      }
    }
    else
    {
      for( int i=0; i<max; i++)
      {
        int y = (int)Math.round( i * width / max);
        g2d.drawLine( 0, y, width, y);
      }
    }
    
    // ick... get number of points... i hate this schema
    int n = 0;
    for( Plot plot: plots)
      n += plot.getPoints().size();

    // horizontal orientation
    double wbt = width / (n + 1);
    double wb = wbt * 0.75;
    double x = wbt;
    for( Plot plot: plots)
    {
      for( Point point: plot.getPoints())
      {
        plotBar.reset();
  
        double x0 = x - (wb / 2);
        double x1 = x + (wb / 2);
        double y = (yscale != null)? yscale.plot( point.coords[ 0]) * height: point.coords[ 0] * height / max;
        
        if ( y > 0)
        {
          plotBar.moveTo( x0, height);
          plotBar.lineTo( x0, height - y);
          plotBar.lineTo( x1, height - y);
          plotBar.lineTo( x1, height);
          plotBar.lineTo( x0, height);
  
          // draw bar
          g2d.setColor( getBackground());
          colorFeature.applyColor( plot.getBackground(), g2d, width, height);
          g2d.fill( plotBar);
          
          Stroke stroke = strokes.get( plot);
          if ( stroke != null) g2d.setStroke( stroke);
          colorFeature.applyColor( plot.getForeground(), g2d, width, height);
          g2d.draw( plotBar);
        }
        
        // draw text
        colorFeature.applyColor( plot.getForeground(), g2d, width, height);
        float textWidth = metrics.stringWidth( point.label);
        g2d.drawString( point.label, (int)(x - (textWidth / 2)), height + 3 + metrics.getAscent());
        
        x += wbt;
      }
    }    
  }
  
  private final static int textPad = 3;
  
  private Axis xAxis;
  private Axis yAxis;

  private boolean horizontal;
  private List<Plot> plots;
  private double max;
  private Map<Plot, Stroke> strokes;
  private Path2D.Double plotBar;
}
