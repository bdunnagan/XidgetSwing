/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.pie;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.xidget.Creator;
import org.xidget.chart.Plot;
import org.xidget.chart.Point;
import org.xidget.ifeature.IColorFeature;
import org.xidget.ifeature.chart.IPlotFeature;
import org.xidget.swing.Toolkit;

/**
 * A custom widget that shows a two-dimensional pie-chart with optional labels.
 */
@SuppressWarnings("serial")
public class PieChart extends JPanel implements IPlotFeature
{
  public PieChart()
  {
    plots = new ArrayList<Plot>();
    arc = new Arc2D.Double();
    path = new Path2D.Double();
    setFont( getFont().deriveFont( 10f));
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#addPlot(org.xidget.chart.Plot)
   */
  @Override
  public void addPlot( Plot plot)
  {
    plots.add( plot);
    
    for( Point point: plot.getPoints())
    {
      if ( point.coords != null && point.coords.length > 0)
        total += point.coords[ 0];
    }

    q1 = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#removePlot(org.xidget.chart.Plot)
   */
  @Override
  public void removePlot( Plot plot)
  {
    plots.remove( plot);
    
    for( Point point: plot.getPoints())
      if ( point.coords != null && point.coords.length > 0)
        total -= point.coords[ 0];
    
    q1 = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateForeground(org.xidget.chart.Plot, java.lang.String)
   */
  @Override
  public void updateForeground( Plot plot, String color)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateBackground(org.xidget.chart.Plot, java.lang.String)
   */
  @Override
  public void updateBackground( Plot plot, String color)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateStrokeWidth(org.xidget.chart.Plot, double)
   */
  @Override
  public void updateStrokeWidth( Plot plot, double value)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#addPoint(org.xidget.chart.Plot, int, org.xidget.chart.Point)
   */
  @Override
  public void addPoint( Plot plot, int index, Point point)
  {
    if ( point.coords != null && point.coords.length > 0)
      total += point.coords[ 0];
    
    q1 = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#removePoint(org.xidget.chart.Plot, int)
   */
  @Override
  public void removePoint( Plot plot, int index)
  {
    Point point = plot.getPoints().get( index);
    if ( point.coords != null && point.coords.length > 0)
      total -= point.coords[ 0];
    
    q1 = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateCoords(org.xidget.chart.Point, double[])
   */
  @Override
  public void updateCoords( Point point, double[] coords)
  {
    if ( point.coords != null && point.coords.length > 0)
      total -= point.coords[ 0];

    if ( coords != null && coords.length > 0)
      total += coords[ 0];
    
    q1 = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateCoord(org.xidget.chart.Point, int, double)
   */
  @Override
  public void updateCoord( Point point, int coordinate, double value)
  {
    if ( coordinate == 0)
    {
      total -= point.coords[ 0];
      total += value;
      
      q1 = null;
      repaint();
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateLabel(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateLabel( Point point, String label)
  {
    q1 = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateForeground(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateForeground( Point point, String fcolor)
  {
    q1 = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateBackground(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateBackground( Point point, String bcolor)
  {
    q1 = null;
    repaint();
  }

  /**
   * Compute the slices.
   * @param g The graphics context.
   */
  @SuppressWarnings("unchecked")
  private void computeSlices( Graphics2D g)
  {
    Toolkit toolkit = (Toolkit)Creator.getToolkit();
    IColorFeature<Color> feature = toolkit.getFeature( IColorFeature.class);
    
    Font font = g.getFont();
    FontRenderContext fontRenderContext = g.getFontRenderContext();

    q1 = new ArrayList<Slice>();
    q2 = new ArrayList<Slice>();
    q3 = new ArrayList<Slice>();
    q4 = new ArrayList<Slice>();
    slices = new ArrayList<Slice>();
    
    double startAngle = 0;
    for( Plot plot: plots)
    {
      List<Point> points = plot.getPoints();
      for( int i=0; i<points.size(); i++)
      {
        Point point = points.get( i);
        if ( point.coords[ 0] == 0) continue;
        
        Slice slice = new Slice();
        if ( point.label != null) slice.label = font.createGlyphVector( fontRenderContext, point.label);
        slice.startAngle = startAngle;
        slice.angleExtent = point.coords[ 0] / total;
        slice.fcolor = feature.getColor( point.fcolor);
        slice.bcolor = feature.getColor( point.bcolor);
        
        double w = (slice.startAngle + (slice.angleExtent / 2)) * PI2;
        slice.midUX = Math.cos( w);
        slice.midUY = Math.sin( w);
        
        if ( slice.midUX < 0)
        {
          if ( slice.midUY < 0) q3.add( slice); else q2.add( 0, slice);
        }
        else
        {
          if ( slice.midUY < 0) q4.add( 0, slice); else q1.add( slice);
        }
        
        slices.add( slice);
        startAngle += slice.angleExtent;
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
    
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    if ( q1 == null) computeSlices( g2d);
    
    int width = getWidth();
    int height = getHeight();
    int size = ((width < height)? width: height);

    double cx = getWidth() / 2d;
    double cy = getHeight() / 2d;
    double r = size / 2d - 3;

    if ( slices.size() == 0)
    {
      arc.setArcByCenter( cx, cy, r, 0, 360, Arc2D.OPEN);
      g2d.setColor( Color.gray);
      g2d.draw( arc);
    }
    else
    {
      for( int i=0; i<slices.size(); i++)
      {
        Slice slice = slices.get( i);
        arc.setArcByCenter( cx, cy, r, -slice.startAngle * 360, -slice.angleExtent * 360 - 1, Arc2D.PIE);
        g2d.setColor( slice.bcolor);
        g2d.fill( arc);
      }
    }
    
    double rTick = r + tickLength;

    FontMetrics metrics = g2d.getFontMetrics();
    double labelAdvanceY = labelGapY + metrics.getHeight();

    //
    // Quadrant 1
    //
    double labelX = r + labelMargin;
    double labelY0 = -labelAdvanceY / 2;
    for( Slice slice: q1)
    {
      if ( slice.label == null) continue;
      
      double labelY1 = rTick * slice.midUY;
      if ( (labelY1 - labelY0) < labelAdvanceY) labelY1 = labelY0 + labelAdvanceY;
      
      double pieX = r * slice.midUX;
      double pieY = r * slice.midUY;
      double tickX = labelY1 * slice.midUX / slice.midUY;
      
      double labelOffsetX = labelGapX;
      double labelOffsetY = slice.label.getVisualBounds().getHeight() / 2;
      
      path.reset();
      path.moveTo( cx + pieX, cy + pieY);
      if ( tickX < labelX) path.lineTo( cx + tickX, cy + labelY1);
      path.lineTo( cx + labelX, cy + labelY1);
      
      g2d.setColor( Color.gray);
      g2d.draw( path);
      
      g2d.setColor( Color.black);
      g2d.drawGlyphVector( slice.label, (float)(cx + labelX + labelOffsetX), (float)(cy + labelY1 + labelOffsetY));
      
      labelY0 = labelY1;
    }

    //
    // Quadrant 4
    //
    labelY0 = labelAdvanceY / 2;
    for( Slice slice: q4)
    {
      if ( slice.label == null) continue;
      
      double labelY1 = rTick * slice.midUY;
      if ( (labelY0 - labelY1) < labelAdvanceY) labelY1 = labelY0 - labelAdvanceY;
      
      double pieX = r * slice.midUX;
      double pieY = r * slice.midUY;
      double tickX = labelY1 * slice.midUX / slice.midUY;
      
      double labelOffsetX = labelGapX;
      double labelOffsetY = slice.label.getVisualBounds().getHeight() / 2;
      
      path.reset();
      path.moveTo( cx + pieX, cy + pieY);
      if ( tickX < labelX) path.lineTo( cx + tickX, cy + labelY1);
      path.lineTo( cx + labelX, cy + labelY1);
      
      g2d.setColor( Color.gray);
      g2d.draw( path);
      
      g2d.setColor( Color.black);
      g2d.drawGlyphVector( slice.label, (float)(cx + labelX + labelOffsetX), (float)(cy + labelY1 + labelOffsetY));
      
      labelY0 = labelY1;
    }
    
    //
    // Quadrant 2
    //
    labelX = -(r + labelMargin);
    labelY0 = -labelAdvanceY / 2;
    for( Slice slice: q2)
    {
      if ( slice.label == null) continue;
      
      double labelY1 = rTick * slice.midUY;
      if ( (labelY1 - labelY0) < labelAdvanceY) labelY1 = labelY0 + labelAdvanceY;
      
      double pieX = r * slice.midUX;
      double pieY = r * slice.midUY;
      double tickX = labelY1 * slice.midUX / slice.midUY;
      
      double labelOffsetX = -slice.label.getVisualBounds().getWidth() - labelGapX;
      double labelOffsetY = slice.label.getVisualBounds().getHeight() / 2;
      
      path.reset();
      path.moveTo( cx + pieX, cy + pieY);
      if ( tickX > labelX) path.lineTo( cx + tickX, cy + labelY1);
      path.lineTo( cx + labelX, cy + labelY1);
      
      g2d.setColor( Color.gray);
      g2d.draw( path);
      
      g2d.setColor( Color.black);
      g2d.drawGlyphVector( slice.label, (float)(cx + labelX + labelOffsetX), (float)(cy + labelY1 + labelOffsetY));
      
      labelY0 = labelY1;
    }
    
    //
    // Quadrant 3
    //    
    labelY0 = labelAdvanceY / 2;    
    for( Slice slice: q3)
    {
      if ( slice.label == null) continue;
      
      double labelY1 = rTick * slice.midUY;
      if ( (labelY0 - labelY1) < labelAdvanceY) labelY1 = labelY0 - labelAdvanceY;
      
      double pieX = r * slice.midUX;
      double pieY = r * slice.midUY;
      double tickX = labelY1 * slice.midUX / slice.midUY;
      
      double labelOffsetX = -slice.label.getVisualBounds().getWidth() - labelGapX;
      double labelOffsetY = slice.label.getVisualBounds().getHeight() / 2;
      
      path.reset();
      path.moveTo( cx + pieX, cy + pieY);
      if ( tickX > labelX) path.lineTo( cx + tickX, cy + labelY1);
      path.lineTo( cx + labelX, cy + labelY1);
      
      g2d.setColor( Color.gray);
      g2d.draw( path);
      
      g2d.setColor( Color.black);
      g2d.drawGlyphVector( slice.label, (float)(cx + labelX + labelOffsetX), (float)(cy + labelY1 + labelOffsetY));
      
      labelY0 = labelY1;
    }
  }
  
  private final static class Slice
  {
    public GlyphVector label;
    public double startAngle;
    public double angleExtent;
    public double midUX;
    public double midUY;
    public Color fcolor;
    public Color bcolor;
  }
  
  private final static double PI2 = Math.PI * 2;
  private final static int labelMargin = 10;
  private final static int labelGapX = 10;
  private final static int labelGapY = 3;
  private final static int tickLength = 10;
  
  private List<Plot> plots;
  private double total;

  private Arc2D.Double arc;
  private Path2D.Double path;
  private List<Slice> q1;
  private List<Slice> q2;
  private List<Slice> q3;
  private List<Slice> q4;
  private List<Slice> slices;
}
