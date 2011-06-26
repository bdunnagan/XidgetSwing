/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.calendar;

import org.xidget.IXidget;
import org.xidget.feature.AbstractValueFeature;

/**
 * An implementation of IValueFeature for the CalendarPanel widget.
 */
public class CalendarValueFeature extends AbstractValueFeature
{
  public CalendarValueFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.AbstractValueFeature#setValue(java.lang.Object)
   */
  @Override
  protected void setValue( Object value)
  {
    CalendarPanel widget = xidget.getFeature( CalendarPanel.class);
    if ( value instanceof Number)
    {
      long time = ((Number)value).longValue();
      if ( widget.getTime() != time) widget.setTime( time);
    }
    else
    {
      try
      {
        String text = (value != null)? value.toString(): "";
        long time = (long)Double.parseDouble( text);
        if ( widget.getTime() != time) widget.setTime( time);
      }
      catch( NumberFormatException e)
      {
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IValueFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    CalendarPanel widget = xidget.getFeature( CalendarPanel.class);
    return widget.getTime();
  }
}
