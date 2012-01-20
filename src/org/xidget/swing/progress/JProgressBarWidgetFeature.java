/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.progress;

import javax.swing.JProgressBar;

import org.xidget.IXidget;
import org.xidget.ifeature.slider.ISliderWidgetFeature;

/**
 * An implementation of the ISliderFeature for the Swing JProgressBar widget.
 */
public class JProgressBarWidgetFeature implements ISliderWidgetFeature
{
  public JProgressBarWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.precision = 1;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.progress.ISliderFeature#setPrecision(int)
   */
  public void setPrecision( int precision)
  {
    this.precision = (int)Math.pow( 10, precision);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.progress.ISliderFeature#getValue()
   */
  public double getValue()
  {
    JProgressBar progress = xidget.getFeature( JProgressBar.class);
    double raw = progress.getValue();
    double value = (raw * (y1 - y0) / maximum) + y0;
    return (double)Math.round( value * precision) / precision;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.progress.ISliderFeature#setValue(double)
   */
  public void setValue( double raw)
  {
    JProgressBar progress = xidget.getFeature( JProgressBar.class);
    double value = (raw - y0) / (y1 - y0) * maximum;
    progress.setValue( (int)Math.round( value * precision) / precision);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.progress.ISliderFeature#setMaximum(double)
   */
  public void setMaximum( double value)
  {
    y1 = value;
    JProgressBar progress = xidget.getFeature( JProgressBar.class);
    progress.setMaximum( maximum);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.progress.ISliderFeature#getMaximum()
   */
  public double getMaximum()
  {
    return y1;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.progress.ISliderFeature#setMinimum(double)
   */
  public void setMinimum( double value)
  {
    y0 = value;
    JProgressBar progress = xidget.getFeature( JProgressBar.class);
    progress.setMinimum( 0);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.progress.ISliderFeature#getMinimum()
   */
  public double getMinimum()
  {
    return y0;
  }
  
  private final static int maximum = 1000000;
  
  private IXidget xidget;
  private double y0;
  private double y1;
  private int precision;
}
