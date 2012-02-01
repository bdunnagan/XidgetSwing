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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.xidget.IXidget;
import org.xidget.chart.Plot;
import org.xidget.chart.Point;
import org.xidget.chart.Scale;
import org.xidget.ifeature.chart.IPlotFeature;
import org.xmodel.xpath.expression.IExpression;

/**
 * The common base class for horizontal and vertical axes widgets.
 */
@SuppressWarnings("serial")
public abstract class Axis extends JPanel implements IPlotFeature
{
  public Axis( IXidget xidget)
  {
    this.xidget = xidget;
   
    this.plots = new ArrayList<Plot>( 1);
    this.log = 0;
    this.labelDepth = -1;
    this.tickSpacing = 15;
    this.tickLength = 4;
    
    setFont( Font.decode( "times-10"));
    
    setBackground( Color.white);
    addComponentListener( resizeListener);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#addPlot(org.xidget.chart.Plot)
   */
  @Override
  public void addPlot( Plot plot)
  {
    plots.add( plot);

    for( Point point: plot.getPoints())
    {
      if ( point.coords != null || point.coords.length > 0) 
      {
        if ( point.coords[ 0] < min) min = point.coords[ 0];
        if ( point.coords[ 0] > max) max = point.coords[ 0];
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#removePlot(org.xidget.chart.Plot)
   */
  @Override
  public void removePlot( Plot removed)
  {
    plots.remove( removed);
    findExtrema();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateForeground(org.xidget.chart.Plot, java.lang.String)
   */
  @Override
  public void updateForeground( Plot plot, String color)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateBackground(org.xidget.chart.Plot, java.lang.String)
   */
  @Override
  public void updateBackground( Plot plot, String color)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateStrokeWidth(org.xidget.chart.Plot, double)
   */
  @Override
  public void updateStrokeWidth( Plot plot, double value)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#addPoint(org.xidget.chart.Plot, int, org.xidget.chart.Point)
   */
  @Override
  public void addPoint( Plot plot, int index, Point point)
  {
    if ( point.coords != null && point.coords.length > 0)
    {
      if ( point.coords[ 0] < min) setExtrema( point.coords[ 0], max);
      if ( point.coords[ 0] > max) setExtrema( min, point.coords[ 0]);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#removePoint(org.xidget.chart.Plot, int)
   */
  @Override
  public void removePoint( Plot plot, int index)
  {
    Point point = plot.getPoints().get( index);
    if ( point.coords != null && point.coords.length > 0)
    {
      if ( point.coords[ 0] == min) { findExtrema(); return;}
      if ( point.coords[ 0] == max) { findExtrema(); return;}
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateCoords(org.xidget.chart.Point, double[])
   */
  @Override
  public void updateCoords( Point point, double[] coords)
  {
    if ( point.coords != null && point.coords.length > 0)
    {
      if ( point.coords[ 0] == min) { findExtrema(); return;}
      if ( point.coords[ 0] == max) { findExtrema(); return;}
      
      if ( coords[ 0] < min) setExtrema( coords[ 0], max);
      if ( coords[ 0] > max) setExtrema( min, coords[ 0]);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateCoord(org.xidget.chart.Point, int, double)
   */
  @Override
  public void updateCoord( Point point, int coordinate, double value)
  {
    if ( coordinate == 0)
    {
      if ( point.coords[ 0] == min) { findExtrema(); return;}
      if ( point.coords[ 0] == max) { findExtrema(); return;}
      
      if ( value < min) setExtrema( value, max);
      if ( value > max) setExtrema( min, value);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateLabel(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateLabel( Point point, String label)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateForeground(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateForeground( Point point, String fcolor)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateBackground(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateBackground( Point point, String bcolor)
  {
  }
  
  /**
   * Set the extrema.
   * @param min The minimum value.
   * @param max The maximum value.
   */
  private void setExtrema( double min, double max)
  {
    this.min = min;
    this.max = max;
    reset();
  }
  
  /**
   * Recompute the extrema based on the complete set of points.
   */
  protected void findExtrema()
  {
    for( Plot plot: plots)
    {
      for( Point point: plot.getPoints())
      {
        if ( point.coords != null || point.coords.length > 0) 
        {
          if ( point.coords[ 0] < min) min = point.coords[ 0];
          if ( point.coords[ 0] > max) max = point.coords[ 0];
        }
      }
    }
    
    reset();
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
  protected IExpression labelExpr;
  protected List<Plot> plots;
  protected Scale scale;
  protected double min;
  protected double max;
  protected int labelDepth;
  protected int tickSpacing;
  protected int tickLength;
  protected double log;
  protected Font[] fonts;
}
