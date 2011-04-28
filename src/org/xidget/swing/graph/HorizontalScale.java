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
 * A custom widget that paints a horizontal scale.
 */
@SuppressWarnings("serial")
public class HorizontalScale extends JPanel
{
  public HorizontalScale( double min, double max, double log)
  {
    this.min = min;
    this.max = max;
    this.log = log;
    
    setFont( Font.decode( "times-10"));
    
    setBackground( Color.white);
    addComponentListener( resizeListener);
    addMouseMotionListener( mouseListener);
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent( Graphics g)
  {
    super.paintComponent( g);
    
    if ( scale == null) scale = new Scale( min, max, getWidth() / 3, log);
    
    FontMetrics metrics = g.getFontMetrics();
    Graphics2D g2d = (Graphics2D)g;
    g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    g2d.setColor( Color.red);
    for( int i=0; i<5; i++)
    {
      g2d.drawLine( mouse-i, i, mouse+i, i);
    }
    
    g2d.setColor( Color.black);
    int width = getWidth() - 1;
    int height = getHeight() - metrics.getAscent();
    int divisions = scale.getDivisions() + 1;
    List<Tick> ticks = scale.getTicks();
    for( int i=0; i<ticks.size(); i++)
    {
      Tick tick = ticks.get( i);
      int x = (int)(tick.scale * width);
      int y = height * (divisions - tick.depth) / divisions;
      g2d.drawLine( x, 0, x, y);
      
      if ( tick.depth < 5)
      {
        if ( tick.text == null) tick.text = String.format( "%1.4g", tick.value);
        int textWidth = metrics.stringWidth( tick.text);
        int tx = x;
        if ( i > 0) tx -= (textWidth / 2);
        if ( i == ticks.size() - 1) tx -= (textWidth / 2);
        g2d.drawString( tick.text, tx, y + metrics.getAscent());
      }
    }
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
      mouse = event.getX();
      repaint();
    }
  };
  
  private Scale scale;
  private double min;
  private double max;
  private double log;
  private int mouse;
  
  public static void main( String[] args) throws Exception
  {
    JFrame frame = new JFrame();
    
    HorizontalScale vscale = new HorizontalScale( 0, 100, 0);
    frame.getContentPane().add( vscale);
    
    frame.setSize( 500, 100);
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame.setVisible( true);
  }
}
