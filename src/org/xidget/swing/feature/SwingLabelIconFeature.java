package org.xidget.swing.feature;

import javax.swing.Icon;
import javax.swing.JLabel;
import org.xidget.text.feature.IIconFeature;

/**
 * An implementation of IIconFeature which updates the icon of a JLabel. 
 */
public class SwingLabelIconFeature implements IIconFeature
{
  public SwingLabelIconFeature( JLabel jlabel)
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
