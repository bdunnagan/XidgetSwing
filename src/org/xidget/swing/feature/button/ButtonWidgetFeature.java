/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.button;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import org.xidget.IXidget;
import org.xidget.ifeature.button.IButtonWidgetFeature;

/**
 * An implementation of IButtonWidgetFeature for Swing AbstractButton widgets.
 */
public class ButtonWidgetFeature implements IButtonWidgetFeature
{
  public ButtonWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.button.IButtonWidgetFeature#setImage(java.lang.Object)
   */
  public void setImage( Object image)
  {
    AbstractButton button = xidget.getFeature( AbstractButton.class);
    if ( button != null) button.setIcon( (Icon)image);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.button.IButtonWidgetFeature#setState(boolean)
   */
  public void setState( boolean state)
  {
    AbstractButton button = xidget.getFeature( AbstractButton.class);
    if ( button != null)
    {
      ButtonModel model = button.getModel();
      model.setPressed( state);
    }
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
