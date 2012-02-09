/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.line;

import org.xidget.IXidget;
import org.xidget.ifeature.chart.IAxisFeature;
import org.xmodel.xpath.expression.IExpression;

public class AxisFeature implements IAxisFeature
{
  public AxisFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.log = 0;
    this.labelDepth = -1;
    this.tickSpacing = 15;
    this.tickLength = 4;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.graph.IAxisFeature#setLogBase(int)
   */
  @Override
  public void setLogBase( int base)
  {
    log = base;
    
    Axis axis = xidget.getFeature( Axis.class);
    if ( axis != null)
    {
      axis.scale = null;
      axis.repaint();
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IAxisFeature#setLabelDepth(int)
   */
  @Override
  public void setLabelDepth( int depth)
  {
    labelDepth = depth;
    
    Axis axis = xidget.getFeature( Axis.class);
    if ( axis != null)
    {
      axis.scale = null;
      axis.repaint();
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IAxisFeature#setTickSpacing(int)
   */
  @Override
  public void setTickSpacing( int spacing)
  {
    tickSpacing = spacing;
    
    Axis axis = xidget.getFeature( Axis.class);
    if ( axis != null)
    {
      axis.scale = null;
      axis.repaint();
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IAxisFeature#setLabelExpression(org.xmodel.xpath.expression.IExpression)
   */
  @Override
  public void setLabelExpression( IExpression expression)
  {
    this.labelExpr = expression;
    
    Axis axis = xidget.getFeature( Axis.class);
    if ( axis != null)
    {
      axis.scale = null;
      axis.repaint();
    }
  }

  private IXidget xidget;
  protected IExpression labelExpr;
  protected int labelDepth;
  protected int tickSpacing;
  protected int tickLength;
  protected double log;
}
