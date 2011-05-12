/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.calendar;

import org.xidget.IXidget;
import org.xidget.ifeature.text.ITextWidgetFeature;

/**
 * An implementation of ITextWidgetFeature for the CalendarPanel widget.
 */
public class CalendarTextWidgetFeature implements ITextWidgetFeature
{
  public CalendarTextWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.text.ITextWidgetFeature#setEditable(boolean)
   */
  public void setEditable( boolean editable)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.text.ITextWidgetFeature#setText(org.xmodel.xpath.expression.StatefulContext, java.lang.String)
   */
  public void setText( String text)
  {
    CalendarPanel widget = xidget.getFeature( CalendarPanel.class);
    try
    {
      long time = (long)Double.parseDouble( text);
      if ( widget.getTime() != time) widget.setTime( time);
    }
    catch( NumberFormatException e)
    {
    }
  }

  private IXidget xidget;
}
