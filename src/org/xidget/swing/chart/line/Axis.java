/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.line;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
    this.tickSpacing = 20;
    
    setFont( Font.decode( "times-10"));
    
    setBackground( Color.white);
    addComponentListener( resizeListener);
    addMouseMotionListener( mouseListener);
    addMouseWheelListener( wheelListener);
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
   * Called when the mouse is moved.
   * @param x The new x-coordinate.
   * @param y The new y-coordinate.
   */
  protected abstract void mouseMoved( int x, int y);
  
  /**
   * Called when the mouse wheel is moved.
   * @param clicks The number of clicks.
   */
  protected abstract void mouseWheelMoved( int clicks);
  
  private ComponentListener resizeListener = new ComponentAdapter() {
    public void componentResized( ComponentEvent event)
    {
      scale = null;
      if ( graph != null) graph.axisResized( axis);
    }
  };
  
  private MouseMotionListener mouseListener = new MouseAdapter() {
    public void mouseMoved( MouseEvent event) 
    {
      if ( scale != null) Axis.this.mouseMoved( event.getX(), event.getY());
    }
  };
  
  private MouseWheelListener wheelListener = new MouseWheelListener() {
    public void mouseWheelMoved( MouseWheelEvent e)
    {
      if ( scale != null)
      {
        int delta = e.getWheelRotation();
        Axis.this.mouseWheelMoved( delta);
      }
    }
  };
  
  protected IXidget xidget;
  protected LineChart graph;
  protected String axis;
  protected IExpression labelExpr;
  protected Scale scale;
  protected int labelDepth;
  protected int tickSpacing;
  protected double min;
  protected double max;
  protected double log;
  protected double cursor;
}
