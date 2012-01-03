/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.plot2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JFrame;

import org.xidget.chart.Scale;
import org.xidget.chart.Scale.Format;
import org.xidget.chart.Scale.Tick;

/**
 * A custom widget that paints a horizontal scale.  The ticks of the scale may be oriented
 * so that they are flush with the top or bottom side.  Labels are automatically chosen
 * and positioned. 
 */
@SuppressWarnings("serial")
public class XAxis extends Axis
{
  public XAxis( boolean top)
  {
    this.top = top;
    setPreferredSize( new Dimension( -1, 30));
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.graph.Axis#getScale()
   */
  public Scale getScale()
  {
    int width = getWidth();
    if ( scale == null && min != max && width > 4) 
    {
      scale = new Scale( min, max, width / 4, log, format);
      textDepth = -1;
    }
    return scale;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent( Graphics g)
  {
    super.paintComponent( g);
    
    Scale scale = getScale();
    
    FontMetrics metrics = g.getFontMetrics();
    Graphics2D g2d = (Graphics2D)g;
    g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    int width = getWidth() - 1;
    int height = getHeight();

    // draw cursor
    int cursorX = (int)Math.round( scale.plot( cursor) * width);
    g2d.setColor( Color.lightGray);
    g2d.drawLine( cursorX, 0, cursorX, height);
    
    // find tick depth at which labels do not overlap
    List<Tick> ticks = scale.getTicks();
    if ( textDepth == -1) textDepth = findTextDepth( metrics);
    
    // draw ticks and labels
    g2d.setColor( Color.black);
    int adjHeight = height - metrics.getHeight();
    int divisions = ticks.get( 1).depth + 1;
    for( int i=0; i<ticks.size(); i++)
    {
      Tick tick = ticks.get( i);
      
      int x = (int)Math.round( tick.scale * width);
      int y = adjHeight * (divisions - tick.depth) / divisions;
      if ( top)
      {
        g2d.drawLine( x, height, x, height - y + 1);
      }
      else
      {
        g2d.drawLine( x, 0, x, y);
      }
      
      if ( tick.depth <= textDepth)
      {
        int textWidth = metrics.stringWidth( tick.label);
        int tx = x;
        if ( i > 0) tx -= (textWidth / 2);
        if ( i == ticks.size() - 1) tx -= (textWidth / 2);

        if ( top)
        {
          g2d.drawString( tick.label, tx, height - y);
        }
        else
        {
          g2d.drawString( tick.label, tx, y + metrics.getAscent());
        }
      }
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.graph.Axis#mouseMoved(int, int)
   */
  @Override
  protected void mouseMoved( int x, int y)
  {
    Scale scale = getScale();
    
    // redraw old cursor region
    int x0 = (int)Math.round( scale.plot( cursor));
    repaint( x0-1, 0, 3, getHeight());

    // set cursor
    cursor = scale.value( x, getWidth());
    //graph.setAxisCursor( axis, cursor);
    
    // redraw new cursor region
    int x1 = (int)Math.round( scale.plot( cursor));
    repaint( x1-1, 0, 3, getHeight());
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.graph.Axis#mouseWheelMoved(int)
   */
  @Override
  protected void mouseWheelMoved( int delta)
  {
    List<Tick> ticks = scale.getTicks();
    double d = ticks.get( 1).value - ticks.get( 0).value;
    if ( delta < 0)
    {
      min -= d;
      max += d;
    }
    else
    {
      min += d;
      max -= d;
    }
    
    scale = null;
    repaint();
  }

  /**
   * Find the tick depth at which ticks are spaced far enough apart for the specified width.
   * @param metric The FontMetrics instance.
   * @return Returns the maximum tick depth for labeling.
   */
  private int findTextDepth( FontMetrics metrics)
  {
    if ( scale == null) return 0;
    
    int width = getWidth();
    List<Tick> ticks = scale.getTicks();
    for( int i=0; i<=ticks.get( 1).depth; i++)
    {
      int max = 0;
      int count = 0;
      String maxLabel = "";
      for( Tick tick: ticks)
      {
        if ( tick.depth == i)
        {
          if ( max < tick.label.length())
          {
            max = tick.label.length();
            maxLabel = tick.label;
          }
          count++;
        }
      }
      
      int textWidth = metrics.stringWidth( maxLabel) + 10; 
      if ( textWidth > (width / count)) return i-1;
    }
    return ticks.get( 1).depth - 1;
  }
  
  private boolean top;
  
  public static void main( String[] args) throws Exception
  {
//    IModelObject points = new ModelObject( "points");
//    for( int i=0; i<10000; i++)
//    {
//      ModelObject point = new ModelObject( "point");
//      point.setAttribute( "x", i);
//      point.setAttribute( "y", Math.sin( i * Math.PI / 100));
//      points.addChild( point);
//    }
//    
//    for( int i=0; i<10; i++)
//    {
//      long t0 = System.nanoTime();
//      
//      double x = 0;
//      double y = 0;
//      double z = 0;
//      for( IModelObject node: points.getChildren())
//      {
//        x = Xlate.get( node, "x", 0.0);
//        y = Xlate.get( node, "y", 0.0);
//        z = x;
//      }
//      
//      long t1 = System.nanoTime();
//      long el = (t1 - t0);
//      System.out.printf( "%3.1fms, %f\n", el / 1e6, z);
//    }    
//    
    JFrame frame = new JFrame();
    
    XAxis axis = new XAxis( false);
    axis.setExtrema( 3, 5);
    axis.setFormat( Format.engineering);
    frame.getContentPane().add( axis);
    
    frame.setSize( 500, 50);
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame.setVisible( true);
  }
}
