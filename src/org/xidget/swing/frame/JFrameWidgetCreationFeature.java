/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JFrameWidgetCreationFeature.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.swing.frame;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.swing.form.JPanelXidget;
import org.xidget.swing.layout.AdapterLayoutManager;

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
    jframe.setLocationByPlatform(true);    
    jframe.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE);
    jframe.addWindowListener( windowListener);

    for( IXidget child: xidget.getChildren())
    {
      if ( child instanceof JPanelXidget)
      {
        AdapterLayoutManager layoutManager = new AdapterLayoutManager( child, new BorderLayout());
        jframe.getContentPane().setLayout( layoutManager);
      }
    }
    
    jframe.addWindowListener( new WindowAdapter() {
      public void windowClosed( WindowEvent e)
      {
        Creator.getInstance().destroy( xidget);
      }
    });
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidgets( IXidget parent)
  {
    if ( jframe != null) jframe.dispose();
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

  private WindowListener windowListener = new WindowAdapter() {
    @Override
    public void windowClosing( WindowEvent event)
    {
      Creator.getInstance().destroy( xidget);
    }
  };
  
  private IXidget xidget;
  private JFrame jframe;
}
