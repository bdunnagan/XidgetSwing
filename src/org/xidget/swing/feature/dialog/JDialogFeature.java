/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.dialog;

import javax.swing.JDialog;
import org.xidget.IXidget;
import org.xidget.ifeature.dialog.IDialogFeature;
import org.xmodel.xpath.expression.StatefulContext;

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
   * @see org.xidget.ifeature.dialog.IDialogFeature#open(org.xmodel.xpath.expression.StatefulContext)
   */
  public void open( StatefulContext context)
  {
    JDialog widget = xidget.getFeature( JDialog.class);
    widget.setModal( true);
    widget.setVisible( true);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.dialog.IDialogFeature#close(org.xmodel.xpath.expression.StatefulContext)
   */
  public void close( StatefulContext context)
  {
    JDialog widget = xidget.getFeature( JDialog.class);
    widget.setVisible( false);
  }

  private IXidget xidget;
}
