/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

import org.xidget.graph.Point;
import org.xidget.graph.Scale;
import org.xidget.graph.Scale.Tick;
import org.xidget.ifeature.IPointsFeature;

/**
 * A custom widget that plots points on a two dimensional graph.  
 */
@SuppressWarnings("serial")
public class Graph2D extends JPanel implements IPointsFeature
{
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#add(org.xidget.graph.Point)
   */
  @Override
  public void add( Point point)
  {
    points.add( point);

    // repaint point
    int x1 = (int)(point.coords[ 0]);
    int y1 = (int)(point.coords[ 1]);
    repaint( x1-5, y1-5, 10, 10);
    
    // repaint line
    if ( points.size() > 1)
    {
      Point last = points.get( points.size() - 2);
      int x0 = (int)(last.coords[ 0]);
      int y0 = (int)(last.coords[ 1]);
      
      repaint( x0-5, y0-5, (x1-x0)+10, (y1-y0)+10);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#add(int, org.xidget.graph.Point)
   */
  @Override
  public void add( int index, Point point)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#update(org.xidget.graph.Point)
   */
  @Override
  public void update( Point point)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPointsFeature#remove(int)
   */
  @Override
  public void remove( int index)
  {
  }
  
  /**
   * Set the vertical scale for this graph.
   * @param scale The scale.
   */
  public void setVerticalScale( Scale scale)
  {
    vscale = scale;
    repaint();
  }
  
  /**
   * Set the horizontal scale for this graph.
   * @param scale The scale.
   */
  public void setHorizontalScale( Scale scale)
  {
    hscale = scale;
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
}
