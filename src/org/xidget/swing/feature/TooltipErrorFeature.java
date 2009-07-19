/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Color;
import javax.swing.JComponent;
import org.xidget.IXidget;
import org.xidget.ifeature.IErrorFeature;

/**
 * An IErrorAdapter which displays the error in a tooltip on a Swing component.
 * The adapter will restore the original tooltip text when the error is cleared.
 * This adapter is not suitable if the tooltip text is dynamic.
 */
public class TooltipErrorFeature implements IErrorFeature
{
  public TooltipErrorFeature( IXidget xidget)
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
    JComponent widget = xidget.getFeature( JComponent.class);
    if ( message == null || message.length() == 0)
    {
      widget.setBackground( background);
      widget.setToolTipText( tooltip);
      tooltip = null;
    }
    else
    {
      if ( tooltip == null) tooltip = widget.getToolTipText();
      if ( background == null) background = widget.getBackground();
      widget.setToolTipText( "Error: "+message);
      widget.setBackground( Color.YELLOW);
    }
  }
  
  private IXidget xidget;
  private String tooltip;
  private Color background;
}
