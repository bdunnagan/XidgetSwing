/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.pie;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Arc2D;
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
    if ( maxX < point.coords[ 0]) maxX = point.coords[ 0];
        
    points.add( index, point);
    
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#update(org.xidget.graph.Point, int, double)
   */
  @Override
  public void update( Point point, int coordinate, double value)
  {
    point.coords[ coordinate] = value;
    if ( minX > point.coords[ 0] || maxX < point.coords[ 0])
    {
      findExtrema();
    }
    
    repaint();
  }

  /**
   * Find the minimum and maximum values of the coordinates in the point list.
   */
  private void findExtrema()
  {
    if ( points == null || points.size() == 0)
    {
      minX = 0;
      maxX = 0;
      return;
    }
    
    minX = maxX = points.get( 0).coords[ 0];
    for( int i=1; i<points.size(); i++)
    {
      Point point = points.get( i);
      if ( point.coords[ 0] < minX) minX = point.coords[ 0];
      if ( point.coords[ 0] > maxX) maxX = point.coords[ 0];
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
    
    Color oldColor = g2d.getColor();
    Stroke oldStroke = g2d.getStroke();
    
    g2d.setStroke( stroke);
    
    int width = (int)Math.round( getWidth() - border * 2);
    int height = (int)Math.round( getHeight() - border * 2);
    double radius = ((width < height)? width: height) / 2d - border - spread;

    double cx = getWidth() / 2;
    double cy = getHeight() / 2;
    
    float hue = 0;
    double range = (maxX - minX);
    double curX = points.get( 0).coords[ 0];
    double angleD = 0;
    for( int i=1; i<points.size(); i++)
    {
      Point point = points.get( i);
      
      double extent = (point.coords[ 0] - curX) / range;
      double extentD = extent * 360d;
      
      double spreadR = -(angleD + (extentD / 2)) * Math.PI / 180;
      double spreadX = Math.cos( spreadR) * spread;
      double spreadY = Math.sin( spreadR) * spread;
      
      Arc2D.Double arc = new Arc2D.Double();
      arc.setArcByCenter( cx + spreadX, cy + spreadY, radius, angleD, extentD, Arc2D.PIE);
      
      g2d.setColor( Color.getHSBColor( hue, 0.8f, 1f));
      g2d.fill( arc);
      
      g2d.setColor( Color.getHSBColor( hue, 0.8f, 0.6f));
      g2d.draw( arc);
      
      curX = point.coords[ 0];
      angleD += extentD;
      hue += extentD * Math.PI / 180;
    }    
    
    g2d.setColor( oldColor);
    g2d.setStroke( oldStroke);
  }

  private double border = 1;
  private double spread = 3;
  private Stroke stroke;
  
  private List<Point> points;
  private double minX;
  private double maxX;
}
