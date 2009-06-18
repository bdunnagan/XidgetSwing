/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.menu;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import org.xidget.IXidget;
import org.xidget.ifeature.IIconFeature;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;

/**
 * An implementation of IWidgetCreationFeature for the Swing JPopupMenu widget. 
 */
public class JMenuWidgetCreationFeature extends SwingWidgetCreationFeature implements IIconFeature, ILabelFeature
{
  public JMenuWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    jMenu = new JMenu();    
    return jMenu;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jMenu};
  }
  
  /**
   * Returns the JPopupMenu that was created.
   * @return Returns the JPopupMenu that was created.
   */
  public JMenu getJMenu()
  {
    return jMenu;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    jMenu.setIcon( (Icon)icon);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#setText(java.lang.String)
   */
  public void setText( String text)
  {
    jMenu.setText( text);
  }

  private JMenu jMenu;
}
