/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.pie;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
    stroke = new BasicStroke( (int)border, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    shape = new Arc2D.Double();
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
    
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#remove(int)
   */
  @Override
  public void remove( int index)
  {
    total -= points.remove( index).coords[ 0];
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
    
    repaint();
  }

  /**
   * Compute the fill and outline colors of the pie slices.
   */
  private void computeColors()
  {
    drawColor = new ArrayList<Color>( points.size());
    fillColor = new ArrayList<Color>( points.size());
    for( int i=0; i<points.size(); i++)
    {
      float hue = (float)i / points.size();
      fillColor.add( Color.getHSBColor( hue, 0.9f, 1f));
      drawColor.add( Color.getHSBColor( hue, 0.7f, 0.6f));
    }   
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent( Graphics g)
  {
    super.paintComponent( g);
    
    if ( fillColor == null || fillColor.size() != points.size()) computeColors();
    
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    FontMetrics metrics = g2d.getFontMetrics();
    g2d.setStroke( stroke);
    
    int width = (int)Math.round( getWidth() - border * 2);
    int height = (int)Math.round( getHeight() - border * 2);
    int size = ((width < height)? width: height);
    double radius = size / 2d - border - spread;

    double cx = getWidth() / 2d;
    double cy = getHeight() / 2d;

    Point2D.Double[] labelPoints = new Point2D.Double[ points.size()];
    
    double angleD = 0;
    for( int i=0; i<points.size(); i++)
    {
      Point point = points.get( i);
      
      double extentD = point.coords[ 0] / total * 360;
      
      double midD = (angleD + (extentD / 2)) * Math.PI / 180;
      double midUX = Math.cos( midD);
      double midUY = Math.sin( midD);
      
      double d = spread / 2 / Math.sin( extentD * Math.PI / 360);
      if ( d < 0) d = 0;
      double spreadX = midUX * d;
      double spreadY = midUY * d;
      
      shape.setArcByCenter( cx + spreadX, cy + spreadY, radius - d, -angleD, -extentD, Arc2D.PIE);
      
      g2d.setColor( fillColor.get( i));
      g2d.fill( shape);
      
      g2d.setColor( drawColor.get( i));
      g2d.draw( shape);
      
      if ( d < radius)
      {
        double labelRadius = (radius - d) / 2;
        double lx = cx + spreadX + midUX * labelRadius;
        double ly = cy + spreadY + midUY * labelRadius;
        labelPoints[ i] = new Point2D.Double( lx, ly);
      }
      else
      {
        labelPoints[ i] = null;
      }
      
      angleD += extentD;
    }

    g2d.setColor( Color.black);
    for( int i=0; i<points.size(); i++)
    {
      Point point = points.get( i);
      if ( point.label == null) point.label = "Label " + i;
      
      Point2D.Double labelPoint = labelPoints[ i];
      if ( labelPoint == null) continue;

      Rectangle2D bounds = metrics.getStringBounds( point.label, g2d);
      
      int lx = (int)Math.round( labelPoint.x - bounds.getWidth() / 2);
      int ly = (int)Math.round( labelPoint.y + metrics.getDescent());
      
      g2d.drawString( point.label, lx, ly);
    }
  }

  private double border = 1;
  private double spread = 3;
  private Stroke stroke;
  
  private List<Point> points;
  private double total;
  
  private Arc2D.Double shape = new Arc2D.Double();
  private List<Color> fillColor;
  private List<Color> drawColor;
}
