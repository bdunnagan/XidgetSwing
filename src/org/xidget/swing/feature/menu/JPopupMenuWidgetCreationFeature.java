/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.menu;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xidget.swing.menu.PopupMouseListener;

/**
 * An implementation of IWidgetCreationFeature for the Swing JPopupMenu widget. 
 */
public class JPopupMenuWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public JPopupMenuWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    jPopupMenu = new JPopupMenu();
    
    IWidgetCreationFeature creationFeature = xidget.getParent().getFeature( IWidgetCreationFeature.class);
    
    // add popup to inner-most component
    Object[] objects = creationFeature.getLastWidgets();
    for( Object object: objects)
    {
      JComponent component = (JComponent)object;
      component.add( jPopupMenu);
      component.addMouseListener( new PopupMouseListener( xidget));
    }
    
    return jPopupMenu;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jPopupMenu};
  }
  
  /**
   * Returns the JPopupMenu that was created.
   * @return Returns the JPopupMenu that was created.
   */
  public JPopupMenu getJPopupMenu()
  {
    return jPopupMenu;
  }
  
  private JPopupMenu jPopupMenu;
}
