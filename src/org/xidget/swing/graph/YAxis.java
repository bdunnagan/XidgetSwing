/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JFrame;

import org.xidget.graph.Scale;
import org.xidget.graph.Scale.Format;
import org.xidget.graph.Scale.Tick;

/**
 * A custom widget that paints a vertical scale.  The ticks of the scale may be oriented
 * so that they are flush with the left or right side.  Labels are automatically chosen
 * and positioned. 
 */
@SuppressWarnings("serial")
public class YAxis extends Axis
{
  public YAxis( boolean left)
  {
    this.left = left;
    setPreferredSize( new Dimension( 30, -1));
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

    int width = getWidth();
    int height = getHeight() - 1;
    
    // draw cursor
    int cursorY = (int)Math.round( scale.plot( cursor) * height);
    g2d.setColor( Color.lightGray);
    g2d.drawLine( 0, cursorY, width, cursorY);

    // find tick depth at which labels do not overlap
    if ( textDepth == -1) textDepth = findTextDepth( metrics.getAscent() + 2);
    
    // draw ticks and labels
    g2d.setColor( Color.black);
    List<Tick> ticks = scale.getTicks();
    double divisions = ticks.get( 1).depth + 1;
    for( int i=0; i<ticks.size(); i++)
    {
      Tick tick = ticks.get( i);
      
      double depth = (divisions - tick.depth) / divisions;
      int length = (int)(depth * width);
      int y = (int)Math.round( (1.0 - tick.scale) * height);
      if ( left)
      {
        g2d.drawLine( width, y, width - length, y);
      }
      else
      {
        g2d.drawLine( 0, y, length, y);
      }
     
      if ( tick.depth <= textDepth)
      {
        if ( i == (ticks.size() - 1)) y += metrics.getAscent() + 2;
        
        int textWidth = metrics.stringWidth( tick.label);
        
        double nextDepth = (divisions - tick.depth - 1) / divisions;
        int nextLength = (int)(nextDepth * width);
        if ( left)
        {
          int x0 = width - nextLength - textWidth - 2;
          int x1 = width - length;
          if ( x1 < x0) x0 = x1;
          if ( x0 < 0) x0 = 0;
          g2d.drawString( tick.label, x0, y-1);
        }
        else
        {
          int x0 = nextLength + 2;
          int x1 = length - textWidth;
          if ( x1 > x0) x0 = x1;
          if ( (x0 + textWidth) > width) x0 = width - textWidth;
          g2d.drawString( tick.label, x0, y-1);
        }
      }
    }
  }
  
  /**
   * Find the tick depth at which ticks are spaced far enough apart for the specified height.
   * @param textHeight The height of a label.
   * @return Returns the maximum tick depth for labelling.
   */
  private int findTextDepth( int textHeight)
  {
    int height = getHeight();
    List<Integer> counts = scale.getTickCounts();
    for( int i=counts.size()-1; i>=0; i--)
    {
      int count = counts.get( i);
      if ( textHeight <= (height / count))
        return i;
    }
    return 0;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.graph.Axis#mouseMoved(int, int)
   */
  @Override
  protected void mouseMoved( int x, int y)
  {
    // redraw old cursor region
    int y0 = (int)Math.round( scale.plot( cursor));
    repaint( 0, y0-1, getWidth(), 3);

    // set cursor
    cursor = scale.value( y, getHeight());
    //graph.setAxisCursor( axis, cursor);
    
    // redraw new cursor region
    int y1 = (int)Math.round( scale.plot( cursor));
    repaint( 0, y1-1, getWidth(), 3);
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.graph.Axis#mouseWheelMoved(int)
   */
  @Override
  protected void mouseWheelMoved( int clicks)
  {
  }

  private boolean left;
  
  public static void main( String[] args) throws Exception
  {
    JFrame frame = new JFrame();
    
    YAxis axis = new YAxis( false);
    axis.setExtrema( 30, 40000);
    axis.setFormat( Format.engineering);
    frame.getContentPane().add( axis);
    
    frame.setSize( 10, 500);
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame.setVisible( true);
  }
}
