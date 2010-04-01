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
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#setLogarithmic(double)
   */
  public void setLogarithmic( double log)
  {
    this.log = log;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#getLogarithmic()
   */
  public double getLogarithmic()
  {
    return log;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#setMaximum(int)
   */
  public void setMaximum( int value)
  {
    JSlider slider = xidget.getFeature( JSlider.class);
    slider.setMaximum( value);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#getMaximum()
   */
  public int getMaximum()
  {
    JSlider slider = xidget.getFeature( JSlider.class);
    return slider.getMaximum();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#setMinimum(int)
   */
  public void setMinimum( int value)
  {
    JSlider slider = xidget.getFeature( JSlider.class);
    slider.setMinimum( value);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#getMinimum()
   */
  public int getMinimum()
  {
    JSlider slider = xidget.getFeature( JSlider.class);
    return slider.getMinimum();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#setAutoLabel(boolean)
   */
  public void setAutoLabel( boolean auto)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.slider.ISliderFeature#getAutoLabel()
   */
  public boolean getAutoLabel()
  {
    return false;
  }
  
  private IXidget xidget;
  private double log;
}
