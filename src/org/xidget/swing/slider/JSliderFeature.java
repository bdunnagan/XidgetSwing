/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.slider;

import javax.swing.JSlider;

import org.xidget.IXidget;
import org.xidget.ifeature.slider.ISliderFeature;

/**
 * An implementation of the ISliderFeature for the Swing JSlider widget.
 */
public class JSliderFeature implements ISliderFeature
{
  public JSliderFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.precision = 1;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#setPrecision(int)
   */
  public void setPrecision( int precision)
  {
    this.precision = (int)Math.pow( 10, precision);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#getValue()
   */
  public double getValue()
  {
    JSlider slider = xidget.getFeature( JSlider.class);
    double raw = slider.getValue();
    double value = (raw * (y1 - y0) / maximum) + y0;
    return (double)Math.round( value * precision) / precision;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#setValue(double)
   */
  public void setValue( double raw)
  {
    JSlider slider = xidget.getFeature( JSlider.class);
    double value = (raw - y0) / (y1 - y0) * maximum;
    slider.setValue( (int)Math.round( value * precision) / precision);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#setMaximum(double)
   */
  public void setMaximum( double value)
  {
    y1 = value;
    JSlider slider = xidget.getFeature( JSlider.class);
    slider.setMaximum( maximum);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#getMaximum()
   */
  public double getMaximum()
  {
    return y1;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#setMinimum(double)
   */
  public void setMinimum( double value)
  {
    y0 = value;
    JSlider slider = xidget.getFeature( JSlider.class);
    slider.setMinimum( 0);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#getMinimum()
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
