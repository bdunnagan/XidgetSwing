/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.swing.ifeature.ISwingContainerFeature;
import org.xidget.swing.ifeature.ISwingFrameFeature;

/**
 * An implementation of IWidgetCreationFeature which creates a Swing JFrame for the application.
 */
public class ApplicationWidgetCreationFeature implements IWidgetCreationFeature, ISwingFrameFeature, ISwingContainerFeature
{
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#createWidget()
   */
  public void createWidget()
  {
    jframe = new JFrame();

    SwingUtilities.invokeLater( new Runnable() {
      public void run()
      {
        jframe.pack();
        jframe.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
        jframe.setVisible( true);
      }
    });
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidget()
  {
    jframe.dispose();
    jframe = null;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.ISwingContainerFeature#getContainer()
   */
  public Container getContainer()
  {
    return jframe;
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.ISwingFrameFeature#getFrame()
   */
  public JFrame getFrame()
  {
    return jframe;
  }

  private JFrame jframe;
}
