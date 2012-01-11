/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JDialogWidgetCreationFeature.java
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
package org.xidget.swing.dialog;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JDialog;

import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.swing.layout.AdapterLayoutManager;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetCreationFeature which creates a Swing JDialog.
 */
public class JDialogWidgetCreationFeature implements IWidgetCreationFeature
{
  public JDialogWidgetCreationFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#createWidget()
   */
  public void createWidgets()
  {
    IModelObject config = xidget.getConfig();
    
    jDialog = new JDialog();
    jDialog.setLocationByPlatform( true);
    jDialog.setModal( Xlate.get( config, "modal", true));
    jDialog.addComponentListener( moveListener);
    
    if ( xidget.getChildren().size() > 0)
    {
      IXidget form = xidget.getChildren().get( 0);
      AdapterLayoutManager layoutManager = new AdapterLayoutManager( form, new BorderLayout());
      jDialog.getContentPane().setLayout( layoutManager);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidgets()
  {
    jDialog.dispose();
    jDialog = null;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jDialog};
  }

  /**
   * Returns the JFrame created for the xidget.
   * @return Returns the JFrame created for the xidget.
   */
  public JDialog getJDialog()
  {
    return jDialog;
  }

  private final ComponentListener moveListener = new ComponentAdapter() {
    public void componentMoved( ComponentEvent e)
    {
    }
    public void componentResized( ComponentEvent e)
    {
      jDialog.getContentPane();
    }
  };
  
  private IXidget xidget;
  private JDialog jDialog;
}
