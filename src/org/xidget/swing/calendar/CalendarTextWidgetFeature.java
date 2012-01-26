/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.calendar;

import org.xidget.IXidget;
import org.xidget.swing.feature.SwingTextWidgetFeature;

/**
 * An implementation of ITextWidgetFeature for the Calendar component.
 */
public class CalendarTextWidgetFeature extends SwingTextWidgetFeature
{
  public CalendarTextWidgetFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setHAlign(org.xidget.ifeature.ITextWidgetFeature.HAlign)
   */
  @Override
  public void setHAlign( HAlign alignment)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setVAlign(org.xidget.ifeature.ITextWidgetFeature.VAlign)
   */
  @Override
  public void setVAlign( VAlign alignment)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setEditable(boolean)
   */
  @Override
  public void setEditable( boolean editable)
  {
  }
}
