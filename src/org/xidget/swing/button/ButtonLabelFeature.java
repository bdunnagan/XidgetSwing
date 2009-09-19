/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.button;

import javax.swing.AbstractButton;
import org.xidget.IXidget;
import org.xidget.ifeature.ILabelFeature;

/**
 * An implementation of ILabelFeature for Swing AbstractButton widgets.
 */
public class ButtonLabelFeature implements ILabelFeature
{
  public ButtonLabelFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.button.IButtonWidgetFeature#setText(java.lang.String)
   */
  public void setText( String text)
  {
    AbstractButton button = xidget.getFeature( AbstractButton.class);
    if ( button != null) button.setText( text);
  }
  
  private IXidget xidget;
}
