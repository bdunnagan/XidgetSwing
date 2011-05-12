/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.slider;

import javax.swing.JSlider;
import javax.swing.JSpinner;

import org.xidget.IXidget;
import org.xidget.feature.AbstractValueFeature;

/**
 * An implementation of IValueFeature for a Swing text component.
 */
public class JSliderValueFeature extends AbstractValueFeature
{
  public JSliderValueFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.AbstractValueFeature#setValue(java.lang.Object)
   */
  @Override
  protected void setValue( Object value)
  {
    JSlider widget = xidget.getFeature( JSlider.class);
    if ( value instanceof Number)
    {
      widget.setValue( ((Number)value).intValue());
    }
    else
    {
      widget.setValue( (int)Double.parseDouble( value.toString()));
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IValueFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    JSpinner widget = xidget.getFeature( JSpinner.class);
    return widget.getValue();
  }
}
