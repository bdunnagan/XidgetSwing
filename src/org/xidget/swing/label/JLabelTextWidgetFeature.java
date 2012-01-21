/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.label;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.xidget.IXidget;
import org.xidget.swing.feature.SwingTextWidgetFeature;

public class JLabelTextWidgetFeature extends SwingTextWidgetFeature
{
  public JLabelTextWidgetFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setEditable(boolean)
   */
  @Override
  public void setEditable( boolean editable)
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setHAlign(org.xidget.ifeature.ITextWidgetFeature.HAlign)
   */
  @Override
  public void setHAlign( HAlign alignment)
  {
    JLabel widget = xidget.getFeature( JLabel.class);
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
    JLabel widget = xidget.getFeature( JLabel.class);
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
