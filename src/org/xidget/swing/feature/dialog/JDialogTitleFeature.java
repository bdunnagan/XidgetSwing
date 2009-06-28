/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.dialog;

import javax.swing.JDialog;
import org.xidget.IXidget;
import org.xidget.ifeature.ITitleFeature;

/**
 * An implementation of ITitleFeature for setting the title of a Swing JDialog.
 */
public class JDialogTitleFeature implements ITitleFeature
{
  public JDialogTitleFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITitleFeature#setTitle(java.lang.String)
   */
  public void setTitle( String title)
  {
    JDialog widget = xidget.getFeature( JDialog.class);
    if ( widget != null) widget.setTitle( title);
  }
  
  private IXidget xidget;
}

