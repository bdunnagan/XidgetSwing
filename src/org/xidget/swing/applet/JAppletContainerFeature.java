/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.applet;

import java.awt.BorderLayout;

import javax.swing.JApplet;
import javax.swing.JMenuBar;

import org.xidget.IXidget;
import org.xidget.swing.feature.GenericContainerFeature;
import org.xidget.swing.layout.AdapterLayoutManager;

/**
 * An implementation of IWidgetContainerFeature for the Swing JFrame widget.
 */
public class JAppletContainerFeature extends GenericContainerFeature
{
  public JAppletContainerFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.GenericContainerFeature#addWidget(int, org.xidget.IXidget)
   */
  @Override
  public void addWidget( int index, IXidget child)
  {
    JApplet applet = xidget.getFeature( JApplet.class);
    JMenuBar menuBar = child.getFeature( JMenuBar.class);
    if ( menuBar != null)
    {
      applet.setJMenuBar( menuBar);
    }
    else
    {
      AdapterLayoutManager layoutManager = new AdapterLayoutManager( child, new BorderLayout());
      applet.getContentPane().setLayout( layoutManager);
      super.addWidget( index, child);
    }
  }
}
