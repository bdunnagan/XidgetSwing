/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import javax.swing.AbstractButton;
import javax.swing.SwingConstants;

import org.xidget.IXidget;

/**
 * An implementation of ITextWidgetFeature for Swing AbstractButton components.
 */
public class AbstractButtonTextWidgetFeature extends SwingTextWidgetFeature
{
  public AbstractButtonTextWidgetFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setHAlign(org.xidget.ifeature.ITextWidgetFeature.HAlign)
   */
  @Override
  public void setHAlign( HAlign alignment)
  {
    AbstractButton widget = xidget.getFeature( AbstractButton.class);
    int value = 0;
    switch( alignment)
    {
      case left:   value = SwingConstants.LEFT; break;
      case center: value = SwingConstants.CENTER; break;
      case right:  value = SwingConstants.RIGHT; break;
    }
    widget.setHorizontalAlignment( value);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setVAlign(org.xidget.ifeature.ITextWidgetFeature.VAlign)
   */
  @Override
  public void setVAlign( VAlign alignment)
  {
    AbstractButton widget = xidget.getFeature( AbstractButton.class);
    int value = 0;
    switch( alignment)
    {
      case top:   value = SwingConstants.TOP; break;
      case center: value = SwingConstants.CENTER; break;
      case bottom:  value = SwingConstants.BOTTOM; break;
    }
    widget.setVerticalAlignment( value);
  }
}
