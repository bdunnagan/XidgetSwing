/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.slider;

import org.xidget.IXidget;
import org.xidget.feature.AbstractValueFeature;
import org.xidget.ifeature.slider.ISliderWidgetFeature;

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
    ISliderWidgetFeature feature = xidget.getFeature( ISliderWidgetFeature.class);
    if ( value instanceof Number)
    {
      feature.setValue( ((Number)value).doubleValue());
    }
    else
    {
      feature.setValue( (int)Double.parseDouble( value.toString()));
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IValueFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    ISliderWidgetFeature feature = xidget.getFeature( ISliderWidgetFeature.class);
    return feature.getValue();
  }
}
