/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.text;

import javax.swing.JLabel;
import org.xidget.IXidget;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.IWidgetCreationFeature;

/**
 * An implementation of ILabelFeature that populates the label associated with a JTextComponent.
 */
public class JTextComponentLabelFeature implements ILabelFeature
{
  public JTextComponentLabelFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#setText(java.lang.String)
   */
  public void setText( String text)
  {
    JTextComponentWidgetCreationFeature feature = (JTextComponentWidgetCreationFeature)xidget.getFeature( IWidgetCreationFeature.class);
    JLabel jLabel = feature.getLabelWidget();
    if ( jLabel != null) jLabel.setText( text);
  }
  
  private IXidget xidget;
}
