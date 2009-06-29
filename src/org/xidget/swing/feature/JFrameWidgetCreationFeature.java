/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.xidget.IXidget;
import org.xidget.config.util.Pair;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

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

    SwingUtilities.invokeLater( new Runnable() {
      public void run()
      {
        jframe.pack();
        Dimension packedSize = jframe.getSize();
        
        // set size of jframe if child size is set
        IModelObject config = xidget.getConfig();
        Pair size = new Pair( Xlate.get( config, "size", Xlate.childGet( config, "size", "")), 0, 0);
        
        if ( size.x > 0 && size.y <= 0) jframe.setSize( size.x, packedSize.height); 
        if ( size.y > 0 && size.x <= 0) jframe.setSize( packedSize.width, size.y); 
        if ( size.x > 0 && size.y > 0) jframe.setSize( size.x, size.y); 
            
        jframe.setVisible( true);
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
