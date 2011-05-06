/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.graph;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.xidget.graph.Scale;
import org.xidget.graph.Scale.Format;
import org.xidget.graph.Scale.Tick;

/**
 * A custom widget that paints a horizontal scale.  The ticks of the scale may be oriented
 * so that they are flush with the top or bottom side.  Labels are automatically chosen
 * and positioned. 
 */
@SuppressWarnings("serial")
public class HorizontalScale extends JPanel
{
  public HorizontalScale( double min, double max, double log, boolean top, Format format)
  {
    this.format = format;
    this.min = min;
    this.max = max;
    this.log = log;
    this.top = top;
    
    setFont( Font.decode( "times-10"));
    
    setBackground( Color.white);
    addComponentListener( resizeListener);
    addMouseMotionListener( mouseListener);
    addMouseWheelListener( wheelListener);
  }
  
  /**
   * @return Returns the scale used by this widget.
   */
  public Scale getScale()
  {
    if ( scale == null) 
    {
      scale = new Scale( min, max, getWidth() / 4, log, format);
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
  
  /**
   * Find the tick depth at which ticks are spaced far enough apart for the specified width.
   * @param metric The FontMetrics instance.
   * @return Returns the maximum tick depth for labeling.
   */
  private int findTextDepth( FontMetrics metrics)
  {
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
  
  private ComponentListener resizeListener = new ComponentAdapter() {
    public void componentResized( ComponentEvent event)
    {
      scale = null;
    }
  };
  
  private MouseMotionListener mouseListener = new MouseAdapter() {
    public void mouseMoved( MouseEvent event) 
    {
      Scale scale = getScale();
      
      // redraw old cursor region
      int x = (int)Math.round( scale.plot( cursor));
      repaint( x-1, 0, x+1, getHeight());

      // set cursor
      x = event.getX();
      cursor = scale.value( x, getWidth() - 1);
      
      // redraw new cursor region
      repaint( x-1, 0, x+1, getHeight());
    }
  };
  
  private MouseWheelListener wheelListener = new MouseWheelListener() {
    public void mouseWheelMoved( MouseWheelEvent e)
    {
      if ( scale == null) return;
      
      List<Tick> ticks = scale.getTicks();
      double d = ticks.get( 1).value - ticks.get( 0).value;
//      for( int i=1; i<ticks.size(); i++)
//      {
//        if ( ticks.get( i).depth == 0)
//        {
//          d = ticks.get( i).value - ticks.get( 0).value;
//          break;
//        }
//      }
      
      System.out.printf( "%f\n", d);
      int delta = e.getWheelRotation();
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
  };
  
  private Format format;
  private Scale scale;
  private double min;
  private double max;
  private double log;
  private double cursor;
  private boolean top;
  private int textDepth;
  
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
    
    HorizontalScale vscale = new HorizontalScale( 3, 5, 0, false, Format.engineering);
    frame.getContentPane().add( vscale);
    
    frame.setSize( 500, 50);
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame.setVisible( true);
  }
}
