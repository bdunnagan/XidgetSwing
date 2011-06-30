/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text;

import javax.swing.text.JTextComponent;

import org.xidget.IXidget;
import org.xidget.swing.feature.SwingWidgetFeature;

public class JTextComponentWidgetFeature extends SwingWidgetFeature
{
  public JTextComponentWidgetFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setEditable(boolean)
   */
  @Override
  public void setEditable( boolean editable)
  {
    JTextComponent widget = xidget.getFeature( JTextComponent.class);
    widget.setEditable( editable);
  }
}
