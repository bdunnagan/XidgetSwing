/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.line;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPanel;
import org.xidget.IXidget;
import org.xidget.chart.Scale;
import org.xmodel.xpath.expression.IExpression;

/**
 * The common base class for horizontal and vertical axes widgets.
 */
@SuppressWarnings("serial")
public abstract class Axis extends JPanel
{
  public Axis( IXidget xidget)
  {
    this.xidget = xidget;
    
    this.min = 0;
    this.max = 1;
    this.log = 0;
    this.labelDepth = -1;
    this.tickSpacing = 15;
    this.tickLength = 4;
    
    setFont( Font.decode( "times-10"));
    
    setBackground( Color.white);
    addComponentListener( resizeListener);
  }
  
  /**
   * Reset the scale on this axis.
   */
  public void reset()
  {
    scale = null;
  }
  
  /**
   * @return Returns the scale used by this widget.
   */
  public abstract Scale getScale();
  
  /**
   * Create the tick label fonts.
   * @param g The graphics context.
   * @return Returns the label fonts for each tick depth.
   */
  protected Font[] getLabelFonts( Graphics2D g)
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
  
  private ComponentListener resizeListener = new ComponentAdapter() {
    public void componentResized( ComponentEvent event)
    {
      scale = null;
    }
  };
  
  protected IXidget xidget;
  protected LineChart graph;
  protected String axis;
  protected IExpression labelExpr;
  protected Scale scale;
  protected int labelDepth;
  protected int tickSpacing;
  protected int tickLength;
  protected double min;
  protected double max;
  protected double log;
  protected Font[] fonts;
}
