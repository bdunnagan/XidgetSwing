/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.frame;

import java.awt.Component;
import javax.swing.JFrame;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;
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
    JFrame frame = xidget.getFeature( JFrame.class);
    
    IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
    Object[] widgets = creationFeature.getLastWidgets();
    frame.getContentPane().add( (Component)widgets[ 0], index);
    
//    JMenuBar menuBar = child.getFeature( JMenuBar.class);
//    if ( menuBar != null)
//    {
//      frame.setJMenuBar( menuBar);
//      
//      //
//      // Since JMenuBar layout is handled by JFrame, zero its default height for our layout algorithm.
//      //
//      IWidgetFeature widgetFeature = child.getFeature( IWidgetFeature.class);
//      widgetFeature.setDefaultBounds( 0, 0, 0, 0, false);
//    }
//    else
//    {
//      IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
//      Object[] widgets = creationFeature.getLastWidgets();
//      if ( widgets.length > 0) 
//      {
//        // menubar skews the insertion index
//        if ( frame.getJMenuBar() != null) index--;
//        frame.getContentPane().add( (Component)widgets[ 0], index);
//      }
//    }
  }
}
