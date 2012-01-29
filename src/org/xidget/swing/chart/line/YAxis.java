/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.line;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.JFrame;
import org.xidget.IXidget;
import org.xidget.chart.Scale;
import org.xidget.chart.Scale.Tick;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xmodel.xpath.expression.IContext;

/**
 * A custom widget that paints a vertical scale.  The ticks of the scale may be oriented
 * so that they are flush with the left or right side.  Labels are automatically chosen
 * and positioned. 
 */
@SuppressWarnings("serial")
public class YAxis extends Axis
{
  public YAxis( IXidget xidget, boolean left)
  {
    super( xidget);
    this.left = left;
    setPreferredSize( new Dimension( 30, -1));
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.graph.Axis#getScale()
   */
  public Scale getScale()
  {
    IContext context = null;
    
    if ( xidget != null)
    {
      IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
      context = contextFeature.getContext( this);
    }
    
    int height = getHeight();
    if ( scale == null && min != max && height > tickSpacing) 
    {
      scale = new Scale( min, max, height / tickSpacing, log, context);
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
    
    Graphics2D g2d = (Graphics2D)g;
    g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    int width = getWidth();
    int height = getHeight() - 1;
    
    // draw cursor
    int cursorY = (int)Math.round( scale.plot( cursor) * height);
    g2d.setColor( Color.lightGray);
    g2d.drawLine( 0, cursorY, width, cursorY);

    // get label fonts
    Font[] fonts = getLabelFonts( g2d);
    
    // find tick depth at which labels do not overlap, and max label width
    int labelDepth = this.labelDepth;
    if ( labelDepth == -1) 
    {
      // find maximum depth at which labels do not overlap
      labelDepth = findTextDepth( g2d);
      
      // find maximum width of text at each tick depth
      findMaxWidths( g2d);
    }
    
    // draw ticks and labels
    g2d.setColor( Color.black);
    List<Tick> ticks = scale.getTicks();
    double divisions = ticks.get( 1).depth + 1;
    for( int i=0; i<ticks.size(); i++)
    {
      Tick tick = ticks.get( i);
      
      double depth = (divisions - tick.depth) / divisions;
      int length = (int)(depth * (width - maxWidths[ tick.depth] - 2));
      int y = (int)Math.round( (1.0 - tick.scale) * height);
      if ( left)
      {
        g2d.drawLine( width, y, width - length, y);
      }
      else
      {
        g2d.drawLine( 0, y, length, y);
      }
     
      if ( tick.depth <= labelDepth)
      {
        int fontDepth = (tick.depth < fonts.length)? tick.depth: (fonts.length - 1);
        g2d.setFont( fonts[ fontDepth]);
        FontMetrics metrics = g2d.getFontMetrics();
        int halfHeight = metrics.getAscent() / 2;
        
        if ( i == 0) y -= halfHeight;
        if ( i == (ticks.size() - 1)) y += halfHeight;
        
        int textWidth = metrics.stringWidth( tick.label);
        if ( left)
        {
          int x0 = width - length - textWidth - 2;
          g2d.drawString( tick.label, x0, y + halfHeight - 1);
        }
        else
        {
          int x0 = length + 2;
          g2d.drawString( tick.label, x0, y + halfHeight - 1);
        }
      }
    }
    
    g2d.setFont( fonts[ 0]);
  }
  
  /**
   * Create the tick label fonts.
   * @param g The graphics context.
   * @return Returns the label fonts for each tick depth.
   */
  private Font[] getLabelFonts( Graphics2D g)
  {
    if ( fonts == null || g.getFont() != fonts[ 0])
    {
      fonts = new Font[ 4];
      fonts[ 0] = getFont();
      for( int i=1; i<fonts.length; i++)
      {
        fonts[ i] = fonts[ i-1].deriveFont( fonts[ i-1].getSize() * 0.85f);
      }
    }
    return fonts;
  }
  
  /**
   * Find the max width of the labels at each tick depth.
   * @param g The graphics context.
   */
  private void findMaxWidths( Graphics2D g)
  {
    List<Tick> ticks = scale.getTicks();
    maxWidths = new int[ ticks.get( 1).depth + 1];
    for( Tick tick: ticks)
    {
      if ( tick.depth <= labelDepth)
      {
        FontMetrics metrics = g.getFontMetrics( fonts[ tick.depth]);
        int labelWidth = metrics.stringWidth( tick.label);
        if ( labelWidth > maxWidths[ tick.depth]) maxWidths[ tick.depth] = labelWidth;
      }
    }
  }
  
  /**
   * Find the tick depth at which ticks are spaced far enough apart.
   * @param g The graphics context.
   * @return Returns the maximum tick depth for labeling.
   */
  private int findTextDepth( Graphics2D g)
  {
    int height = getHeight();
    List<Integer> counts = scale.getTickCounts();
    for( int i=counts.size()-1; i>=0; i--)
    {
      if ( i < fonts.length)
      {
        int count = counts.get( i);
        int textHeight = g.getFontMetrics( fonts[ i]).getHeight();
        if ( textHeight <= (height / count))
          return i;
      }
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
  private int[] maxWidths;
  private Font[] fonts;
  
  public static void main( String[] args) throws Exception
  {
    JFrame frame = new JFrame();
    
    YAxis axis = new YAxis( null, true);
    axis.setExtrema( 30, 40000);
    frame.getContentPane().add( axis);
    
    frame.setSize( 10, 500);
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame.setVisible( true);
  }
}
