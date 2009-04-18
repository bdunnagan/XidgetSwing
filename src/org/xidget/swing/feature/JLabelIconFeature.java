/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import javax.swing.Icon;
import javax.swing.JLabel;
import org.xidget.ifeature.IIconFeature;

/**
 * An implementation of IIconFeature which updates the icon of a JLabel. 
 */
public class JLabelIconFeature implements IIconFeature
{
  public JLabelIconFeature( JLabel jlabel)
  {
    this.jlabel = jlabel;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.feature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    jlabel.setIcon( (Icon)icon);
  }

  private JLabel jlabel;
}
