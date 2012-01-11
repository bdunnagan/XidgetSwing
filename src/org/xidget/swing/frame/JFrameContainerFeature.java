/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.frame;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.xidget.IXidget;
import org.xidget.swing.feature.GenericContainerFeature;

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
      super.addWidget( index, child);
    }
  }
}
