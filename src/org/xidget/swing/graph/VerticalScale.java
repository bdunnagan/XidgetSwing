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

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.xidget.graph.Scale;
import org.xidget.graph.Scale.Tick;

/**
 * A custom widget that paints a vertical scale.
 */
public class VerticalScale extends JPanel
{
  public VerticalScale( double min, double max, double log)
  {
    this.min = min;
    this.max = max;
    this.log = log;
    
    setFont( Font.decode( "times-10"));
    
    setBackground( Color.white);
    addComponentListener( resizeListener);
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent( Graphics g)
  {
    super.paintComponent( g);
    
    if ( scale == null) scale = new Scale( min, max, getHeight() / 3, log);
    
    FontMetrics metrics = g.getFontMetrics();
    Graphics2D g2d = (Graphics2D)g;
    g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    int width = getWidth();
    int height = getHeight() - 1;
    int halfWidth = width / 2;
    double divisions = scale.getDivisions();
    int lastLength = 0;
    for( Tick tick: scale.getTicks())
    {
      double depth = (divisions - tick.depth) / divisions;
      int length = (int)(depth * halfWidth);
      int y = (int)(tick.scale * height);
      g2d.drawLine( width, y, width - length, y);
      
      if ( tick.depth < 4)
      {
        if ( tick.text == null) tick.text = String.format( "%1.4g", tick.value);
        int textWidth = metrics.stringWidth( tick.text);
        int x0 = width - lastLength - textWidth - 10;
        int x1 = width - length;
        if ( x1 < x0) x0 = x1;
        g2d.drawString( tick.text, x0, y-1);
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
  
  private Scale scale;
  private double min;
  private double max;
  private double log;
  
  public static void main( String[] args) throws Exception
  {
    JFrame frame = new JFrame();
    
    VerticalScale vscale = new VerticalScale( 0, 100, 0);
    frame.getContentPane().add( vscale);
    
    frame.setSize( 10, 500);
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame.setVisible( true);
  }
}
