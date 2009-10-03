/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.label;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.xidget.IXidget;
import org.xidget.ifeature.IIconFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;

/**
 * An implementation of IWidgetCreationFeature for the Swing JLabel widget.
 */
public class JLabelWidgetCreationFeature extends SwingWidgetCreationFeature implements IIconFeature
{
  public JLabelWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    jLabel.setIcon( (Icon)icon);
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    jLabel = new JLabel();
    return jLabel;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jLabel};
  }

  /**
   * Returns the JLabel.
   * @return Returns the JLabel.
   */
  public JLabel getJLabel()
  {
    return jLabel;
  }
  
  private JLabel jLabel;
}
