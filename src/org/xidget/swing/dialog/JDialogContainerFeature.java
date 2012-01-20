/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JMenuBar;

import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.swing.feature.GenericContainerFeature;
import org.xidget.swing.layout.AdapterLayoutManager;

/**
 * An implementation of IWidgetContainerFeature for the Swing JFrame widget.
 */
public class JDialogContainerFeature extends GenericContainerFeature
{
  public JDialogContainerFeature( IXidget xidget)
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
      JDialog dialog = xidget.getFeature( JDialog.class);
      dialog.setJMenuBar( menuBar);
    }
    else
    {
      JDialog dialog = xidget.getFeature( JDialog.class);

      AdapterLayoutManager layoutManager = new AdapterLayoutManager( child, new BorderLayout());
      dialog.getContentPane().setLayout( layoutManager);
      
      IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
      Object[] widgets = creationFeature.getLastWidgets();
      if ( widgets.length > 0) dialog.getContentPane().add( (Component)widgets[ 0], index);
    }
  }
  
}
