/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import javax.swing.JFrame;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;

/**
 * An implementation of IWidgetCreationFeature which creates a Swing JFrame for the application.
 */
public class JFrameWidgetCreationFeature implements IWidgetCreationFeature
{
  public JFrameWidgetCreationFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#createWidget()
   */
  public void createWidgets()
  {
    jframe = new JFrame();
    jframe.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidgets()
  {
    jframe.dispose();
    jframe = null;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jframe};
  }

  /**
   * Returns the JFrame created for the xidget.
   * @return Returns the JFrame created for the xidget.
   */
  public JFrame getFrame()
  {
    return jframe;
  }

  @SuppressWarnings("unused")
  private IXidget xidget;
  private JFrame jframe;
}
