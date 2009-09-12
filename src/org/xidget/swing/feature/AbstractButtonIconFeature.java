/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Insets;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import org.xidget.IXidget;
import org.xidget.ifeature.IIconFeature;

/**
 * An implementation of IIconFeature which updates the icon of an AbstractButton. 
 */
public class AbstractButtonIconFeature implements IIconFeature
{
  public AbstractButtonIconFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.feature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    AbstractButton button = xidget.getFeature( AbstractButton.class);
    if ( button != null) button.setIcon( (Icon)icon);
    
    String text = button.getText();
    if ( text == null || text.length() == 0)
    {
      button.setMargin( new Insets( 0, 0, 0, 0));
    }
  }

  private IXidget xidget;
}
