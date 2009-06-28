/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.dialog;

import java.awt.Dimension;
import javax.swing.JDialog;
import org.xidget.IXidget;
import org.xidget.config.util.Pair;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetCreationFeature which creates a Swing JFrame for the application.
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
    jDialog.setModal( Xlate.get( config, "modal", true));
    
    // set size of jDialog if child size is set
    Pair size = new Pair( Xlate.get( config, "size", Xlate.childGet( config, "size", "")), 0, 0);
    if ( size.x > 0 || size.y > 0)
    {
      jDialog.setSize( new Dimension( size.x, size.y));
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

  private IXidget xidget;
  private JDialog jDialog;
}
