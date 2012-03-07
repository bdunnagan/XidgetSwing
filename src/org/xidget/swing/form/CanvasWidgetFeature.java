/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.form;

import javax.swing.JPanel;
import org.xidget.IXidget;
import org.xidget.swing.feature.SwingContainerWidgetFeature;

public class CanvasWidgetFeature extends SwingContainerWidgetFeature
{
  public CanvasWidgetFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetFeature#setBackground(java.lang.Object)
   */
  @Override
  public void setBackground( Object color)
  {
    Canvas canvas = (Canvas)xidget.getFeature( JPanel.class);
    canvas.setBackground( color);
  }
}
