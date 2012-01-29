/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.line;

import java.awt.Color;
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
  public Scale getScale()
  {
    IContext context = null;
    
    if ( xidget != null)
    {
      IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
      context = contextFeature.getContext( this);
    }
    
    int width = getWidth();
    if ( scale == null && min != max && width > tickSpacing) 
    {
      scale = new Scale( min, max, width / tickSpacing, log, context);
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

    int width = getWidth() - 1;
    int height = getHeight();

    // get label fonts
    Font[] fonts = getLabelFonts( g2d);
    
    // find tick depth at which labels do not overlap
    List<Tick> ticks = scale.getTicks();
    int labelDepth = this.labelDepth;
    if ( labelDepth == -1) labelDepth = findTextDepth( g2d);
    if ( labelDepth == -1) labelDepth = 0;
    
    // draw ticks and labels
    g2d.setColor( Color.black);
    int divisions = ticks.get( 1).depth + 1;
    for( int i=0; i<ticks.size(); i++)
    {
      Tick tick = ticks.get( i);
      
      int x = (int)Math.round( tick.scale * width);
      int y = divisions * tickLength / (tick.depth + 1);
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
   * Find the tick depth at which ticks are spaced far enough apart for the specified width.
   * @param metric The FontMetrics instance.
   * @return Returns the maximum tick depth for labeling.
   */
  private int findTextDepth( Graphics2D g)
  {
    if ( scale == null) return 0;
    
    int width = getWidth();
    List<Tick> ticks = scale.getTicks();
    for( int i=0; i <= ticks.get( 1).depth && i < fonts.length; i++)
    {
      int max = 0;
      int count = 0;
      String maxLabel = "";
      for( Tick tick: ticks)
      {
        if ( tick.depth == i)
        {
          if ( max < tick.label.length())
          {
            max = tick.label.length();
            maxLabel = tick.label;
          }
          count++;
        }
      }
      
      FontMetrics metrics = g.getFontMetrics( fonts[ i]);
      int textWidth = metrics.stringWidth( maxLabel) + 10; 
      if ( textWidth > (width / count)) return i-1;
    }
    return ticks.get( 1).depth - 1;
  }
  
  private boolean top;
  
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
    
    XAxis axis = new XAxis( null, false);
    axis.setExtrema( 3, 5);
    frame.getContentPane().add( axis);
    
    frame.setSize( 500, 50);
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame.setVisible( true);
  }
}
