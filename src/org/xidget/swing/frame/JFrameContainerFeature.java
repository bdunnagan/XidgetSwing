/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.frame;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.swing.feature.GenericContainerFeature;
import org.xidget.swing.layout.AdapterLayoutManager;

/**
 * An implementation of IWidgetContainerFeature for the Swing JFrame widget.
 */
public class JFrameContainerFeature extends GenericContainerFeature
{
  public JFrameContainerFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.GenericContainerFeature#addWidget(int, org.xidget.IXidget)
   */
  @Override
  public void addWidget( int index, IXidget child)
  {
    JMenuBar menuBar = child.getFeature( JMenuBar.class);
    if ( menuBar != null)
    {
      JFrame frame = xidget.getFeature( JFrame.class);
      frame.setJMenuBar( menuBar);
    }
    else
    {
      JFrame frame = xidget.getFeature( JFrame.class);

      AdapterLayoutManager layoutManager = new AdapterLayoutManager( child, new BorderLayout());
      frame.getContentPane().setLayout( layoutManager);
      
      IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
      Object[] widgets = creationFeature.getLastWidgets();
      if ( widgets.length > 0) frame.getContentPane().add( (Component)widgets[ 0], index);
    }
  }
  
}
