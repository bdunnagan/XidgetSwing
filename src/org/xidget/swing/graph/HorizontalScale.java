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
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.xidget.graph.Scale;
import org.xidget.graph.Scale.Tick;

/**
 * A custom widget that paints a horizontal scale.  The ticks of the scale may be oriented
 * so that they are flush with the top or bottom side.  Labels are automatically chosen
 * and positioned. 
 */
@SuppressWarnings("serial")
public class HorizontalScale extends JPanel
{
  public HorizontalScale( double min, double max, double log, boolean top)
  {
    this.min = min;
    this.max = max;
    this.log = log;
    this.top = top;
    
    setFont( Font.decode( "times-10"));
    
    setBackground( Color.white);
    addComponentListener( resizeListener);
    addMouseMotionListener( mouseListener);
  }
  
  /**
   * @return Returns the scale used by this widget.
   */
  public Scale getScale()
  {
    if ( scale == null) 
    {
      scale = new Scale( min, max, getWidth() / 4, log);
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
    
    g2d.setColor( Color.red);
    for( int i=0; i<5; i++)
    {
      int y = top? (height - i): i;
      g2d.drawLine( cursor-i, y, cursor+i, y);
    }
    
    List<Tick> ticks = scale.getTicks();
    if ( textDepth == -1) 
    {
      String text = String.format( "%1.4g", ticks.get( 0).value);      
      textDepth = findTextDepth( metrics.stringWidth( text) + 2);
    }
    
    g2d.setColor( Color.black);
    int adjHeight = height - metrics.getHeight();
    int divisions = ticks.get( 1).depth;
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
        if ( tick.text == null) tick.text = String.format( "%1.4g", tick.value);
        
        int textWidth = metrics.stringWidth( tick.text);
        int tx = x;
        if ( i > 0) tx -= (textWidth / 2);
        if ( i == ticks.size() - 1) tx -= (textWidth / 2);

        if ( top)
        {
          g2d.drawString( tick.text, tx, height - y);
        }
        else
        {
          g2d.drawString( tick.text, tx, y + metrics.getAscent());
        }
      }
    }
  }
  
  /**
   * Find the tick depth at which ticks are spaced far enough apart for the specified width.
   * @param textWidth The width of a label.
   * @return Returns the maximum tick depth for labelling.
   */
  private int findTextDepth( int textWidth)
  {
    int width = getWidth();
    List<Integer> counts = scale.getTickCounts();
    for( int i=counts.size()-1; i>=0; i--)
    {
      int count = counts.get( i);
      if ( textWidth <= (width / count))
        return i;
    }
    return 0;
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
      if ( top)
      {
        int height = getHeight();
        repaint( cursor-5, height-10, 10, 10);
        cursor = event.getX();
        repaint( cursor-5, height-10, 10, 10);
      }
      else
      {
        repaint( cursor-5, 0, 10, 10);
        cursor = event.getX();
        repaint( cursor-5, 0, 10, 10);        
      }
    }
  };
  
  private Scale scale;
  private double min;
  private double max;
  private double log;
  private int cursor;
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
    
    HorizontalScale vscale = new HorizontalScale( 0, 100, 0, false);
    frame.getContentPane().add( vscale);
    
    frame.setSize( 500, 50);
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame.setVisible( true);
  }
}