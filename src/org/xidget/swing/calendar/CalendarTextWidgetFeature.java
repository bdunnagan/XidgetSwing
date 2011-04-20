/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.calendar;

import org.xidget.IXidget;
import org.xidget.config.util.TextTransform;
import org.xidget.feature.text.TextModelFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

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
   * @see org.xidget.ifeature.text.ITextWidgetFeature#setText(org.xmodel.xpath.expression.StatefulContext, java.lang.String, java.lang.String)
   */
  public void setText( StatefulContext context, String channel, String text)
  {
    CalendarPanel widget = xidget.getFeature( CalendarPanel.class);
    if ( channel.equals( TextModelFeature.allChannel))
    {
      if ( transform != null) text = transform.transform( context, text);
      try
      {
        long time = (long)Double.parseDouble( text);
        if ( widget.getTime() != time) widget.setTime( time);
      }
      catch( NumberFormatException e)
      {
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.text.ITextWidgetFeature#setTransform(java.lang.String, org.xmodel.xpath.expression.IExpression)
   */
  public void setTransform( String channel, IExpression expression)
  {
    this.transform = new TextTransform( expression);
  }
  
  private IXidget xidget;
  private TextTransform transform;
}
