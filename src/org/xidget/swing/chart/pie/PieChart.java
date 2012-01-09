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
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
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
    
    stroke = new BasicStroke( (int)sliceBorder, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
    inside = new Arc2D.Double();
    outside = new Arc2D.Double();
    p1 = new Point2D.Double();
    p2 = new Point2D.Double();
    p3 = new Point2D.Double();
    p4 = new Point2D.Double();
    slice = new Path2D.Double();
    
    textGraphics = new ArrayList<TextLayout>();
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
    textGraphics.add( index, null);
    
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

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#update(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void update( Point point, String label)
  {
    point.label = label;
    textGraphics.set( points.indexOf( point), null);
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
  
  /**
   * Compute the text graphics for each point.
   * @param g The graphics context.
   */
  private void computeText( Graphics2D g)
  {
    FontRenderContext fontRenderContext = g.getFontRenderContext();
    for( int i=0; i<points.size(); i++)
    {
      Point point = points.get( i);
      
      if ( textGraphics.size() < i) textGraphics.add( null);
      
      TextLayout textGraphic = textGraphics.get( i);
      if ( textGraphic == null)
      {
        textGraphic = new TextLayout( point.label, getFont(), fontRenderContext);
        textGraphics.set( i, textGraphic);
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
    
    if ( fillColor == null || fillColor.size() != points.size()) computeColors();
    
    Graphics2D g2d = (Graphics2D)g.create();
    g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    g2d.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    
    computeText( g2d);
    
    g2d.setStroke( stroke);
    
    int width = getWidth();
    int height = getHeight();
    int size = ((width < height)? width: height);

    double cx = getWidth() / 2d;
    double cy = getHeight() / 2d;

    Point2D.Double[] labelPoints = new Point2D.Double[ points.size()];

    double r1 = innerRadius;
    double r2 = size / 2d - sliceBorder;

    double a1 = 2 * Math.asin( sliceMargin / 2 / r1);
    double a2 = 2 * Math.asin( sliceMargin / 2 / r2);
    double b1 = a1 * 2;
    double b2 = a2 * 2;
    double w = 0;

    AffineTransform ident = new AffineTransform();
    for( int i=0; i<points.size(); i++)
    {
      Point point = points.get( i);

      double t = 2 * Math.PI * point.coords[ 0] / total;
      
      slice.reset();
      
      if ( t <= b1)
      {
        if ( t >= b2)
        {
          double rp = sliceMargin / 2 / Math.sin( t / 4);
          
          p1.x = cx + rp * Math.cos( w + (t / 2));
          p1.y = cy + rp * Math.sin( w + (t / 2));
          
          outside.setArcByCenter( p1.x, p1.y, r2 - rp, -(w + a2) * 180 / Math.PI, -(t - b2) * 180 / Math.PI, Arc2D.PIE);
          
          g2d.setColor( fillColor.get( i));
          g2d.fill( outside);
          
          g2d.setColor( drawColor.get( i));
          g2d.draw( outside);
        }
      }
      else
      {
        p1.x = cx + r1 * Math.cos( w + a1);
        p1.y = cy + r1 * Math.sin( w + a1);
        
        p2.x = cx + r2 * Math.cos( w + a2);
        p2.y = cy + r2 * Math.sin( w + a2);
        
        p3.x = cx + r2 * Math.cos( w + t - a2);
        p3.y = cy + r2 * Math.sin( w + t - a2);
        
        p4.x = cx + r1 * Math.cos( w + t - a1);
        p4.y = cy + r1 * Math.sin( w + t - a1);

        inside.setArcByCenter( cx, cy, r1, -(w + t - a1) * 180 / Math.PI, (t - b1) * 180 / Math.PI, Arc2D.OPEN);
        outside.setArcByCenter( cx, cy, r2, -(w + a2) * 180 / Math.PI, -(t - b2) * 180 / Math.PI, Arc2D.OPEN);
        
        slice.moveTo( p1.x, p1.y);
        slice.lineTo( p2.x, p2.y);
        slice.append( outside.getPathIterator( ident), true);
        slice.lineTo( p4.x, p4.y);
        slice.append( inside.getPathIterator( ident), true);
        
        g2d.setColor( fillColor.get( i));
        g2d.fill( slice);
        
        g2d.setColor( drawColor.get( i));
        g2d.draw( slice);
      }
        
      w += t;
    }

//    g2d.setColor( getForeground());
//    for( int i=0; i<points.size(); i++)
//    {
//      TextLayout textGraphic = textGraphics.get( i);
//      
//      Point2D.Double labelPoint = labelPoints[ i];
//      if ( labelPoint == null) continue;
//
//      Rectangle2D bounds = textGraphic.getBounds();
//      
//      double lx = labelPoint.x - bounds.getWidth() / 2;
//      double ly = labelPoint.y + textGraphic.getDescent();
//      
//      textGraphic.draw( g2d, (float)lx, (float)ly);
//    }
  }

  private double sliceBorder = 2;
  private double innerRadius = 3;
  private double sliceMargin = 2;
  
  private List<Point> points;
  private double total;

  private Stroke stroke;
  private Arc2D.Double outside;
  private Arc2D.Double inside;
  private Point2D.Double p1;
  private Point2D.Double p2;
  private Point2D.Double p3;
  private Point2D.Double p4;
  private Path2D.Double slice;
  private List<Color> fillColor;
  private List<Color> drawColor;
  private List<TextLayout> textGraphics;
}
