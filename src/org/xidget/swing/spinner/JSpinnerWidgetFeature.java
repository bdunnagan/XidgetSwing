/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.spinner;

import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.xidget.IXidget;
import org.xidget.ifeature.slider.ISliderWidgetFeature;

/**
 * An implementation of the ISliderFeature for the Swing JSpinner widget.
 */
public class JSpinnerWidgetFeature implements ISliderWidgetFeature
{
  public JSpinnerWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.precision = 1;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.spinner.ISliderFeature#setPrecision(int)
   */
  public void setPrecision( int precision)
  {
    this.precision = (int)Math.pow( 10, precision);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.spinner.ISliderFeature#getValue()
   */
  public double getValue()
  {
    SpinnerNumberModel model = getModel();
    double raw = model.getNumber().doubleValue();
    double value = (raw * (y1 - y0) / maximum) + y0;
    return (double)Math.round( value * precision) / precision;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.spinner.ISliderFeature#setValue(double)
   */
  public void setValue( double raw)
  {
    SpinnerNumberModel model = getModel();
    double value = (raw - y0) / (y1 - y0) * maximum;
    model.setValue( (int)Math.round( value * precision) / precision);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.spinner.ISliderFeature#setMaximum(double)
   */
  public void setMaximum( double value)
  {
    y1 = value;
    SpinnerNumberModel model = getModel();
    model.setMaximum( maximum);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.spinner.ISliderFeature#getMaximum()
   */
  public double getMaximum()
  {
    return y1;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.spinner.ISliderFeature#setMinimum(double)
   */
  public void setMinimum( double value)
  {
    y0 = value;
    SpinnerNumberModel model = getModel();
    model.setMinimum( 0);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.spinner.ISliderFeature#getMinimum()
   */
  public double getMinimum()
  {
    return y0;
  }

  /**
   * @return Returns the SpinnerNumberModel, creating it and replacing the existing model if necessary.
   */
  private SpinnerNumberModel getModel()
  {
    JSpinner spinner = xidget.getFeature( JSpinner.class);
    SpinnerModel model = spinner.getModel();
    if ( model instanceof SpinnerNumberModel) return (SpinnerNumberModel)model;
    spinner.setModel( new SpinnerNumberModel());
    return getModel();
  }
  
  private final static int maximum = 1000000;
  
  private IXidget xidget;
  private double y0;
  private double y1;
  private int precision;
}
