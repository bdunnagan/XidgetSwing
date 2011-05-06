/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.xidget.IXidget;
import org.xidget.graph.Point;
import org.xidget.graph.Scale;
import org.xidget.graph.Scale.Tick;
import org.xidget.ifeature.IPointsFeature;
import org.xidget.ifeature.graph.IGraphFeature;

/**
 * A custom widget that plots points on a two dimensional graph.  
 */
@SuppressWarnings("serial")
public class Graph2D extends JPanel implements IPointsFeature, IGraphFeature
{
  public Graph2D()
  {
    hscales = new ArrayList<HorizontalScale>();
    vscales = new ArrayList<VerticalScale>();
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
    
    if ( minX > point.coords[ 0]) minX = point.coords[ 0];
    if ( minY > point.coords[ 1]) minY = point.coords[ 1];
    if ( maxX < point.coords[ 0]) maxX = point.coords[ 0];
    if ( maxY < point.coords[ 1]) maxY = point.coords[ 1];
    
    points.add( index, point);
    repaint( index);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#update(org.xidget.graph.Point)
   */
  @Override
  public void update( Point point)
  {
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
    
    repaint( index);
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
    
    minX = points.get( 0).coords[ 0];
    minY = points.get( 0).coords[ 1];
    maxX = minX;
    maxY = minY;
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
    // repaint point
    int x1 = (int)(point.coords[ 0]);
    int y1 = (int)(point.coords[ 1]);
    repaint( x1-5, y1-5, 10, 10);
    
    // repaint line to the new point
    if ( point.prev != null)
    {
      int x0 = (int)(point.prev.coords[ 0]);
      int y0 = (int)(point.prev.coords[ 1]);
      repaint( x0, y0, (x1-x0), (y1-y0));
    }
    
    // repaint line from the new point
    if ( point.next != null)
    {
      int x0 = (int)(point.next.coords[ 0]);
      int y0 = (int)(point.next.coords[ 1]);
      repaint( x0, y0, (x1-x0), (y1-y0));
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.graph.IAxisScaleFeature#setScale(java.lang.String, org.xidget.IXidget)
   */
  @Override
  public void setScale( String axis, IXidget xidget)
  {
    
    repaint();
  }

  /**
   * Called when a scale widget is resized.
   * @param axis The axis that was resized.
   */
  public void axisResized( String axis)
  {
    repaint();
  }

  /**
   * Called when a scale widget cursor moves.
   * @param axis The axis whose cursor was moved.
   * @param cursor The new cursor position.
   */
  public void setAxisCursor( String axis, double cursor)
  {
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

    // draw vertical grid-lines
    int graphHeight = getHeight();
    if ( xGrid != null)
    {
      g2d.setColor( Color.lightGray);
      for( Tick tick: xGrid)
      {
        int x = (int)Math.round( hscale.plot( tick.value));
        g2d.drawLine( x, 0, x, graphHeight);
      }
    }
    
    // draw horizontal grid-lines
    int graphWidth = getWidth();
    if ( yGrid != null)
    {
      g2d.setColor( Color.lightGray);
      for( Tick tick: yGrid)
      {
        int y = (int)Math.round( vscale.plot( tick.value));
        g2d.drawLine( 0, y, graphWidth, y);
      }
    }
    
    // draw graph
    int prevX = 0;
    int prevY = 0;
    for( int i=0; i<points.size(); i++)
    {
      Point point = points.get( i);
      double x = hscale.plot( point.coords[ 0]);
      double y = vscale.plot( point.coords[ 1]);
      
      g2d.setColor( Color.black);
      if ( lines && i > 0)
      {
        g2d.drawLine( prevX, prevY, (int)x, (int)y);
        prevX = (int)x;
        prevY = (int)y;
      }
      
      Shape shape = PointShapes.getShape( point.style);
      AffineTransform locate = new AffineTransform();
      shape = locate.createTransformedShape( shape);
      g2d.draw( shape);
      
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

  private Scale vscale;
  private Scale hscale;
  private List<Tick> xGrid;
  private List<Tick> yGrid;
  private List<Point> points;
  private boolean lines;
  private double minX, minY;
  private double maxX, maxY;
  private List<HorizontalScale> hscales;
  private List<VerticalScale> vscales;
}
