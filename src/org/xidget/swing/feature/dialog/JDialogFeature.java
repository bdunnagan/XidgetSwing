/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.dialog;

import javax.swing.JDialog;
import org.xidget.IXidget;
import org.xidget.ifeature.dialog.IDialogFeature;

/**
 * An implementation of IDialogFeature for Swing JDialog.
 */
public class JDialogFeature implements IDialogFeature
{
  public JDialogFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.dialog.IDialogFeature#open()
   */
  public void open()
  {
    JDialog widget = xidget.getFeature( JDialog.class);
    widget.setModal( true);
    widget.setVisible( true);
  }
  
  private IXidget xidget;
}
