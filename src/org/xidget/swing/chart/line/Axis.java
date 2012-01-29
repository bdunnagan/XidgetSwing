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
import org.xidget.ifeature.chart.IAxisFeature;
import org.xmodel.xpath.expression.IExpression;

/**
 * The common base class for horizontal and vertical axes widgets.
 */
@SuppressWarnings("serial")
public abstract class Axis extends JPanel implements IAxisFeature
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
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.graph.IAxisFeature#setGraph(java.lang.String, org.xidget.IXidget)
   */
  @Override
  public void setGraph( String axis, IXidget xidget)
  {
    this.axis = axis; 
    this.graph = xidget.getFeature( LineChart.class);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.graph.IAxisFeature#setExtrema(double, double)
   */
  @Override
  public void setExtrema( double min, double max)
  {
    this.min = min;
    this.max = max;
    scale = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.graph.IAxisFeature#setLogBase(int)
   */
  @Override
  public void setLogBase( int base)
  {
    this.log = base;
    scale = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IAxisFeature#setLabelDepth(int)
   */
  @Override
  public void setLabelDepth( int depth)
  {
    this.labelDepth = depth;
    scale = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IAxisFeature#setTickSpacing(int)
   */
  @Override
  public void setTickSpacing( int spacing)
  {
    this.tickSpacing = spacing;
    scale = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IAxisFeature#setLabelExpression(org.xmodel.xpath.expression.IExpression)
   */
  @Override
  public void setLabelExpression( IExpression labelExpr)
  {
    this.labelExpr = labelExpr;
    scale = null;
    repaint();
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
      if ( graph != null) graph.axisResized( axis);
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
