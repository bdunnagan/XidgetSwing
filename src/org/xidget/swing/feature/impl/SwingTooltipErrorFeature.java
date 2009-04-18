/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.impl;

import javax.swing.JComponent;
import org.xidget.IXidget;
import org.xidget.ifeature.IErrorFeature;
import org.xidget.swing.feature.ISwingWidgetFeature;

/**
 * An IErrorAdapter which displays the error in a tooltip on a Swing component.
 * The adapter will restore the original tooltip text when the error is cleared.
 * This adapter is not suitable if the tooltip text is dynamic.
 */
public class SwingTooltipErrorFeature implements IErrorFeature
{
  public SwingTooltipErrorFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.adapter.IErrorAdapter#structureError(java.lang.String)
   */
  public void structureError( String message)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.adapter.IErrorAdapter#valueError(java.lang.String)
   */
  public void valueError( String message)
  {
    ISwingWidgetFeature widgetFeature = xidget.getFeature( ISwingWidgetFeature.class);
    JComponent widget = widgetFeature.getWidget();
    if ( message == null || message.length() == 0)
    {
      widget.setToolTipText( tooltip);
      tooltip = null;
    }
    else
    {
      if ( tooltip == null) tooltip = widget.getToolTipText();
      widget.setToolTipText( "Error: "+message);
    }
  }
  
  private IXidget xidget;
  private String tooltip;
}
