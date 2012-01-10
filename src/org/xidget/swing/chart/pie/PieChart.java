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

import org.xidget.chart.Point;
import org.xidget.ifeature.IPointsFeature;

/**
 * A custom widget that shows a two-dimensional pie-chart with optional labels.
 */
@SuppressWarnings("serial")
public class PieChart extends JPanel implements IPointsFeature
{
  public PieChart()
  {
    points = new ArrayList<Point>();
    arc = new Arc2D.Double();
    path = new Path2D.Double();
    setFont( getFont().deriveFont( 10f));
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
    
    if ( index > 0)
    {
      Point prev = points.get( index - 1);
      if ( prev.next != null) prev.next.prev = point;
      prev.next = point;
      point.prev = prev;
      point.next = prev.next;
    }
    
    total += point.coords[ 0];
    points.add( index, point);

    q1 = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#remove(int)
   */
  @Override
  public void remove( int index)
  {
    total -= points.remove( index).coords[ 0];
    
    q1 = null;
    repaint();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#update(org.xidget.graph.Point, int, double)
   */
  @Override
  public void update( Point point, int coordinate, double value)
  {
    total -= point.coords[ coordinate];
    point.coords[ coordinate] = value;
    total += value;
    
    q1 = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#update(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void update( Point point, String label)
  {
    point.label = label;
    repaint();
  }

  /**
   * Compute the fill and outline colors of the pie slices.
   */
  private void computeColors()
  {
    colors = new ArrayList<Color>( points.size());
    for( int i=0; i<points.size(); i++)
    {
      float hue = (float)i / points.size() * 4;
      for( int j=0; j<4; j++)
      {
        colors.add( Color.getHSBColor( hue, 0.2f + 0.2f * j, 1f));
      }
    }   
  }
  
  /**
   * Compute the slices.
   * @param g The graphics context.
   */
  private void computeSlices( Graphics2D g)
  {
    Font font = g.getFont();
    FontRenderContext fontRenderContext = g.getFontRenderContext();

    q1 = new ArrayList<Slice>();
    q2 = new ArrayList<Slice>();
    q3 = new ArrayList<Slice>();
    q4 = new ArrayList<Slice>();
    slices = new ArrayList<Slice>();
    
    double startAngle = 0;
    for( int i=0; i<points.size(); i++)
    {
      Point point = points.get( i);
      
      Slice slice = new Slice();
      slice.label = font.createGlyphVector( fontRenderContext, point.label);
      slice.startAngle = startAngle;
      slice.angleExtent = point.coords[ 0] / total;
      
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
    if ( colors == null || colors.size() != points.size()) computeColors();
    
    int width = getWidth();
    int height = getHeight();
    int size = ((width < height)? width: height);

    double cx = getWidth() / 2d;
    double cy = getHeight() / 2d;
    double r = size / 2d - 100;

    int color = 0;
    for( Slice slice: slices)
    {
      arc.setArcByCenter( cx, cy, r, -slice.startAngle * 360, -slice.angleExtent * 360 - 1, Arc2D.PIE);
      g2d.setColor( colors.get( color++));
      g2d.fill( arc);
    }
    
    double rTick = r + tickLength;

    FontMetrics metrics = g2d.getFontMetrics();
    double labelAdvanceY = labelGapY + metrics.getHeight();
    
    double labelX = r + labelMargin;
    double labelY0 = -labelAdvanceY / 2;
    for( Slice slice: q1)
    {
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

    labelY0 = labelAdvanceY / 2;
    for( Slice slice: q4)
    {
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
    
    labelX = -(r + labelMargin);
    labelY0 = -labelAdvanceY / 2;
    for( Slice slice: q2)
    {
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
    
    labelY0 = labelAdvanceY / 2;    
    for( Slice slice: q3)
    {
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
  }
  
  private final static double PI2 = Math.PI * 2;
  private final static int labelMargin = 10;
  private final static int labelGapX = 10;
  private final static int labelGapY = 3;
  private final static int tickLength = 10;
  
  private List<Point> points;
  private double total;

  private Arc2D.Double arc;
  private Path2D.Double path;
  private List<Color> colors;
  private List<Slice> q1;
  private List<Slice> q2;
  private List<Slice> q3;
  private List<Slice> q4;
  private List<Slice> slices;
}
