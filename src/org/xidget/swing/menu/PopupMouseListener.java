/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.menu;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import org.xidget.IXidget;

/**
 * A mouse listener for popping up a menu.
 */
public class PopupMouseListener extends MouseAdapter
{
  public PopupMouseListener( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
   */
  @Override
  public void mousePressed( MouseEvent e)
  {
    JPopupMenu widget = xidget.getFeature( JPopupMenu.class);
    if ( e.getButton() == MouseEvent.BUTTON3) widget.show( e.getComponent(), e.getX(), e.getY());
  }

  private IXidget xidget;
}
