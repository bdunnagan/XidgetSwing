/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.menu;

import javax.swing.JComponent;
import javax.swing.JMenuBar;
import org.xidget.IXidget;
import org.xidget.swing.feature.SwingWidgetCreationFeature;

/**
 * An implementation of IWidgetCreationFeature for the Swing JMenuBar widget. 
 */
public class JMenuBarWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public JMenuBarWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    jMenuBar = new JMenuBar();
    return jMenuBar;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jMenuBar};
  }
  
  /**
   * Returns the JMenuBar that was created.
   * @return Returns the JMenuBar that was created.
   */
  public JMenuBar getJMenuBar()
  {
    return jMenuBar;
  }
  
  private JMenuBar jMenuBar;
}
