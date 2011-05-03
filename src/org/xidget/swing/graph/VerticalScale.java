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
 * A custom widget that paints a vertical scale.  The ticks of the scale may be oriented
 * so that they are flush with the left or right side.  Labels are automatically chosen
 * and positioned. 
 */
@SuppressWarnings("serial")
public class VerticalScale extends JPanel
{
  public VerticalScale( double min, double max, double log, boolean left)
  {
    this.min = min;
    this.max = max;
    this.log = log;
    this.left = left;
    
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
    if ( scale == null) scale = new Scale( min, max, getHeight() / 2, log);
    return scale;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent( Graphics g)
  {
    super.paintComponent( g);
    
    if ( scale == null) scale = new Scale( min, max, getHeight() / 2, log);
    
    FontMetrics metrics = g.getFontMetrics();
    Graphics2D g2d = (Graphics2D)g;
    g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    int width = getWidth();
    int height = getHeight() - 1;
    
    g2d.setColor( Color.red);
    for( int i=0; i<5; i++)
    {
      if ( left)
      {
        g2d.drawLine( width - i, cursor-i, width - i, cursor+i);
      }
      else
      {
        g2d.drawLine( i, cursor-i, i, cursor+i);
      }
    }

    g2d.setColor( Color.black);
    int lastLength = 0;
    List<Tick> ticks = scale.getTicks();
    double divisions = ticks.get( 1).depth + 1;
    double a = (height + 4) / ticks.size() / metrics.getHeight();
    double b = height / 2.0 / ticks.size();
    int textDepth = (int)(a / b);
    System.out.printf( "a=%f, b=%f\n", a, b);
    for( int i=0; i<ticks.size(); i++)
    {
      Tick tick = ticks.get( i);
      
      double depth = (divisions - tick.depth) / divisions;
      int length = (int)(depth * width);
      int y = (int)Math.round( tick.scale * height);
      if ( left)
      {
        g2d.drawLine( width, y, width - length, y);
      }
      else
      {
        g2d.drawLine( 0, y, length, y);
      }
      
      if ( tick.depth < textDepth)
      {
        if ( i == 0) y += metrics.getAscent() + 2;
        
        if ( tick.text == null) tick.text = String.format( "%1.4g", tick.value);
        int textWidth = metrics.stringWidth( tick.text);
        
        if ( left)
        {
          int x0 = width - lastLength - textWidth - 10;
          int x1 = width - length;
          if ( x1 < x0) x0 = x1;
          g2d.drawString( tick.text, x0, y-1);
        }
        else
        {
          int x0 = lastLength + 10;
          int x1 = length - textWidth;
          if ( x1 > x0) x0 = x1;
          g2d.drawString( tick.text, x0, y-1);
        }
      }
      
      lastLength = length;
    }
  }
  
  private ComponentListener resizeListener = new ComponentAdapter() {
    public void componentResized( ComponentEvent e)
    {
      scale = null;
    }
  };
  
  private MouseMotionListener mouseListener = new MouseAdapter() {
    public void mouseMoved( MouseEvent event) 
    {
      if ( left)
      {
        repaint( getWidth()-5, cursor-5, 10, 10);
        cursor = event.getY();
        repaint( getWidth()-5, cursor-5, 10, 10);
      }
      else
      {
        repaint( 0, cursor-5, 10, 10);
        cursor = event.getY();
        repaint( 0, cursor-5, 10, 10);
      }
    }
  };
  
  private Scale scale;
  private double min;
  private double max;
  private double log;
  private int cursor;
  private boolean left;
  
  public static void main( String[] args) throws Exception
  {
    JFrame frame = new JFrame();
    
    VerticalScale vscale = new VerticalScale( 0, 100, 0, true);
    frame.getContentPane().add( vscale);
    
    frame.setSize( 10, 500);
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame.setVisible( true);
  }
}
