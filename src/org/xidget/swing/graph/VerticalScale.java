/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    
    // draw ticks
    Graphics2D g2d = (Graphics2D)g;
    
    int width = getWidth();
    int height = getHeight() - 1;
    int divisions = scale.getDivisions() + 1;
    for( Tick tick: scale.getTicks())
    {
      int y = (int)(tick.scale * height);
      g2d.drawLine( 0, y, width * (divisions - tick.depth) / divisions, y);
    }
    
    // draw labels
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
