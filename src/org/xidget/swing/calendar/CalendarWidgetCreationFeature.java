/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.calendar;

import javax.swing.JComponent;
import org.xidget.IXidget;
import org.xidget.swing.feature.SwingWidgetCreationFeature;

/**
 * An implementation of IWidgetCreationFeature to create a CalendarPanel.
 */
public class CalendarWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public CalendarWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    widget = new CalendarPanel( xidget);
    return widget;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { widget};
  }
  
  /**
   * Returns the CalendarPanel widget.
   * @return Returns the CalendarPanel widget.
   */
  public CalendarPanel getCalendarPanel()
  {
    return widget;
  }
  
  private CalendarPanel widget;
}
