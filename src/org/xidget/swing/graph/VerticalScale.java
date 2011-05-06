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

import org.xidget.IXidget;
import org.xidget.graph.Scale;
import org.xidget.graph.Scale.Format;
import org.xidget.graph.Scale.Tick;
import org.xidget.ifeature.graph.IScaleFeature;

/**
 * A custom widget that paints a vertical scale.  The ticks of the scale may be oriented
 * so that they are flush with the left or right side.  Labels are automatically chosen
 * and positioned. 
 */
@SuppressWarnings("serial")
public class VerticalScale extends JPanel implements IScaleFeature
{
  public VerticalScale( double min, double max, double log, boolean left, Format format)
  {
    this.format = format;
    this.min = min;
    this.max = max;
    this.log = log;
    this.left = left;
    
    setFont( Font.decode( "times-10"));
    
    setBackground( Color.white);
    addComponentListener( resizeListener);
    addMouseMotionListener( mouseListener);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.graph.IScaleFeature#setGraph(java.lang.String, org.xidget.IXidget)
   */
  @Override
  public void setGraph( String axis, IXidget xidget)
  {
    this.axis = axis; 
    this.graph = xidget.getFeature( Graph2D.class);
  }

  /**
   * @return Returns the scale used by this widget.
   */
  public Scale getScale()
  {
    if ( scale == null) 
    {
      scale = new Scale( min, max, getHeight() / 4, log, format);
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
  
  private ComponentListener resizeListener = new ComponentAdapter() {
    public void componentResized( ComponentEvent e)
    {
      scale = null;
      graph.axisResized( axis);
    }
  };
  
  private MouseMotionListener mouseListener = new MouseAdapter() {
    public void mouseMoved( MouseEvent event) 
    {
      // redraw old cursor region
      int y = (int)Math.round( scale.plot( cursor));
      repaint( 0, y-1, getWidth(), y+1);

      // set cursor
      y = event.getY();
      cursor = scale.value( y, getHeight() - 1);
      graph.setAxisCursor( axis, cursor);
      
      // redraw new cursor region
      repaint( 0, y-1, getWidth(), y+1);
    }
  };
  
  private Graph2D graph;
  private String axis;
  private Format format;
  private Scale scale;
  private double min;
  private double max;
  private double log;
  private double cursor;
  private boolean left;
  private int textDepth;
  
  public static void main( String[] args) throws Exception
  {
    JFrame frame = new JFrame();
    
    VerticalScale vscale = new VerticalScale( 30, 4000000, 0, false, Format.engineering);
    frame.getContentPane().add( vscale);
    
    frame.setSize( 10, 500);
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame.setVisible( true);
  }
}
