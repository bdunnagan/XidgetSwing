/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.progress;

import org.xidget.IXidget;
import org.xidget.ifeature.model.ISingleValueWidgetFeature;
import org.xidget.ifeature.slider.ISliderWidgetFeature;
import org.xmodel.log.SLog;

public class JProgressBarValueWidgetFeature implements ISingleValueWidgetFeature
{
  public JProgressBarValueWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueWidgetFeature#setValue(java.lang.Object)
   */
  @Override
  public void setValue( Object value)
  {    ISliderWidgetFeature feature = xidget.getFeature( ISliderWidgetFeature.class);
    if ( value instanceof Number)
    {
      feature.setValue( ((Number)value).doubleValue());
    }
    else
    {
      try
      {
        feature.setValue( (int)Double.parseDouble( value.toString()));
      }
      catch( NumberFormatException e)
      {
        SLog.warnf( this, "Progress bar value is not a number: '%s'", value);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueWidgetFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    ISliderWidgetFeature feature = xidget.getFeature( ISliderWidgetFeature.class);
    return feature.getValue();
  }
  
  private IXidget xidget;
}
