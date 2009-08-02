/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import org.xidget.Creator;
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
    jframe.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE);
    jframe.addComponentListener( new ComponentAdapter() {
      public void componentHidden( ComponentEvent e)
      {
        Creator.getInstance().destroy( xidget);
        System.exit( 1);
      }
    });
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

  private IXidget xidget;
  private JFrame jframe;
}
