/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import org.xidget.IXidget;
import org.xidget.ifeature.IIconFeature;

/**
 * An implementation of ITitleFeature for suitable for all Swing containers.
 */
public class SwingIconFeature implements IIconFeature
{
  public SwingIconFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    IXidget parent = xidget.getParent();
    if ( parent == null) return;
    
    if ( parent.getConfig().isType( "tabs"))
    {
      JTabbedPane jtabbedPane = parent.getFeature( JTabbedPane.class);
      int index = parent.getChildren().indexOf( xidget);
      jtabbedPane.setIconAt( index, (Icon)icon);
    }
  }

  private IXidget xidget;
}
