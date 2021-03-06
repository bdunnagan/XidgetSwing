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
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.List;

import org.xidget.IXidget;
import org.xidget.chart.IScale;
import org.xidget.chart.IScale.Tick;
import org.xidget.chart.NumericScale;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.ifeature.chart.IAxisFeature;
import org.xidget.swing.chart.line.ParagraphLayout.Justify;
import org.xmodel.xpath.expression.IContext;

/**
 * A custom widget that paints a horizontal scale.  The ticks of the scale may be oriented
 * so that they are flush with the top or bottom side.  Labels are automatically chosen
 * and positioned. 
 */
@SuppressWarnings("serial")
public class XAxis extends Axis
{
  public XAxis( IXidget xidget, boolean top)
  {
    super( xidget);
    this.top = top;
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
    
    int width = getWidth();
    if ( scale == null && min != max && width > axisFeature.tickSpacing) 
    {
      scale = new NumericScale( min, max, width / axisFeature.tickSpacing, axisFeature.log, context, axisFeature.labelExpr);
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

    int width = getWidth() - 1 - LineChart.padWidth;
    int height = getHeight();
    
    AffineTransform shift = AffineTransform.getTranslateInstance( LineChart.padWidth / 2f, 0);
    AffineTransform transform = g2d.getTransform();
    if ( transform != null) transform.concatenate( shift); else transform = shift;
    g2d.setTransform( transform);

    // get label fonts
    Font[] fonts = getLabelFonts( g2d);
    
    // layout tick labels
    ParagraphLayout[] layouts = layoutTicks( fonts, g2d.getFontRenderContext());
    
    // find tick depth at which labels do not overlap
    AxisFeature axisFeature = (AxisFeature)xidget.getFeature( IAxisFeature.class);
    List<Tick> ticks = scale.getTicks();
    int labelDepth = axisFeature.labelDepth;
    if ( labelDepth == -1) labelDepth = findTextDepth( layouts);
    if ( labelDepth == -1) labelDepth = 0;
    
    // draw ticks and labels
    int divisions = ticks.get( 1).depth + 1;
    for( int i=0; i<ticks.size(); i++)
    {
      Tick tick = ticks.get( i);
      
      int x = (int)Math.round( tick.scale * width);
      int y = divisions * axisFeature.tickLength / (tick.depth + 1);
      if ( top)
      {
        g2d.drawLine( x, height, x, height - y + 1);
      }
      else
      {
        g2d.drawLine( x, 0, x, y);
      }
      
      if ( tick.depth <= labelDepth)
      {
        Font font = fonts[ tick.depth];
        FontMetrics metrics = g.getFontMetrics( font);
        int textWidth = metrics.stringWidth( tick.label);
        int tx = x;
        if ( i > 0) tx -= (textWidth / 2);
        if ( i == ticks.size() - 1) tx -= (textWidth / 2);

        g2d.setFont( font);
        if ( top)
        {
          g2d.drawString( tick.label, tx, height - y);
        }
        else
        {
          g2d.drawString( tick.label, tx, y + metrics.getHeight());
        }
      }
    }
  }

  /**
   * Layout each tick paragraph.
   * @param fonts The label fonts by depth.
   * @param frc The FontRenderContext for layout.
   * @return Returns the paragraph layouts.
   */
  private ParagraphLayout[] layoutTicks( Font[] fonts, FontRenderContext frc)
  {
    List<Tick> ticks = scale.getTicks();
    ParagraphLayout[] layouts = new ParagraphLayout[ ticks.size()];
    for( int i=0; i<ticks.size(); i++)
    {
      Tick tick = ticks.get( i);
      int fontIndex = (tick.depth < fonts.length)? tick.depth: fonts.length-1;
      layouts[ i] = new ParagraphLayout( tick.label, fonts[ fontIndex], Justify.center, frc);
    }
    return layouts;
  }
  
  /**
   * Find the tick depth at which ticks are spaced far enough apart for the width of the component.
   * @param Layouts for each tick.
   * @return Returns the maximum tick depth for labeling.
   */
  private int findTextDepth( ParagraphLayout[] layouts)
  {
    int width = getWidth();
    List<Tick> ticks = scale.getTicks();
    int maxDepth = ticks.get( 1).depth;
    for( int i=0; i <= maxDepth && i < fonts.length; i++)
    {
      int widthAtDepth = 0;
      for( int j=0; j<ticks.size(); j++)
      {
        Tick tick = ticks.get( j);
        if ( tick.depth == i)
        {
          float tickWidth = layouts[ j].getWidth();
          widthAtDepth += tickWidth;
          if ( widthAtDepth > width) return i-1;
        }
      }
    }
    return maxDepth;
  }
  
  private boolean top;
}
