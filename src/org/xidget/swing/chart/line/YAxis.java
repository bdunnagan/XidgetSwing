/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.line;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.List;

import org.xidget.IXidget;
import org.xidget.chart.IScale;
import org.xidget.chart.IScale.Tick;
import org.xidget.chart.NumericScale;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.ifeature.chart.IAxisFeature;
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
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.graph.Axis#getScale()
   */
  public IScale getScale()
  {
    IContext context = null;
    
    if ( xidget != null)
    {
      IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
      context = contextFeature.getContext( this);
    }
    
    AxisFeature axisFeature = (AxisFeature)xidget.getFeature( IAxisFeature.class);
    
    int height = getHeight();
    if ( scale == null && min != max && height > axisFeature.tickSpacing) 
    {
      scale = new NumericScale( min, max, height / axisFeature.tickSpacing, axisFeature.log, context, axisFeature.labelExpr);
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
    
    IScale scale = getScale();
    if ( scale == null) return;
    
    Graphics2D g2d = (Graphics2D)g;
    g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    int width = getWidth();
    int height = getHeight() - 1 - LineChart.padHeight;
    
    AffineTransform shift = AffineTransform.getTranslateInstance( 0, LineChart.padHeight / 2f);
    AffineTransform transform = g2d.getTransform();
    if ( transform != null) transform.concatenate( shift); else transform = shift;
    g2d.setTransform( transform);
    
    // get label fonts
    Font[] fonts = getLabelFonts( g2d);
    
    // find tick depth at which labels do not overlap, and max label width
    AxisFeature axisFeature = (AxisFeature)xidget.getFeature( IAxisFeature.class);
    int labelDepth = axisFeature.labelDepth;
    if ( labelDepth == -1) 
    {
      // find maximum depth at which labels do not overlap
      labelDepth = findTextDepth( g2d);
      if ( labelDepth == -1) labelDepth = 0;
      
      // find maximum width of text at each tick depth
      findMaxWidths( g2d, labelDepth);
    }
    
    // draw ticks and labels
    List<Tick> ticks = scale.getTicks();
    double divisions = ticks.get( 1).depth + 1;
    for( int i=0; i<ticks.size(); i++)
    {
      Tick tick = ticks.get( i);
      
      int length = (int)(divisions * axisFeature.tickLength / (tick.depth + 1));
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
        
        //if ( i == 0) y -= halfHeight;
        //if ( i == (ticks.size() - 1)) y += halfHeight;
        
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
   * Find the max width of the labels at each tick depth.
   * @param g The graphics context.
   * @param labelDepth The maximum depth at which labels do not overlap.
   */
  private void findMaxWidths( Graphics2D g, int labelDepth)
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
  
  private boolean left;
  private int[] maxWidths;
}
