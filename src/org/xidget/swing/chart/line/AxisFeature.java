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
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.graph.IAxisFeature#setLogBase(int)
   */
  @Override
  public void setLogBase( int base)
  {
    Axis axis = xidget.getFeature( Axis.class);
    axis.log = base;
    axis.scale = null;
    axis.repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IAxisFeature#setLabelDepth(int)
   */
  @Override
  public void setLabelDepth( int depth)
  {
    Axis axis = xidget.getFeature( Axis.class);
    axis.labelDepth = depth;
    axis.scale = null;
    axis.repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IAxisFeature#setTickSpacing(int)
   */
  @Override
  public void setTickSpacing( int spacing)
  {
    Axis axis = xidget.getFeature( Axis.class);
    axis.tickSpacing = spacing;
    axis.scale = null;
    axis.repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IAxisFeature#setLabelExpression(org.xmodel.xpath.expression.IExpression)
   */
  @Override
  public void setLabelExpression( IExpression labelExpr)
  {
    Axis axis = xidget.getFeature( Axis.class);
    axis.labelExpr = labelExpr;
    axis.scale = null;
    axis.repaint();
  }

  private IXidget xidget;
}
