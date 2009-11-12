/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * PopupMouseListener.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
