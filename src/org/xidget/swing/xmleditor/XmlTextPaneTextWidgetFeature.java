/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.xmleditor;

import org.xidget.IXidget;
import org.xidget.swing.feature.SwingTextWidgetFeature;

public class XmlTextPaneTextWidgetFeature extends SwingTextWidgetFeature
{
  public XmlTextPaneTextWidgetFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setEditable(boolean)
   */
  @Override
  public void setEditable( boolean editable)
  {
    XmlTextPane widget = xidget.getFeature( XmlTextPane.class);
    widget.setEditable( editable);
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
}
