/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * CustomTabbedPane.java
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
package org.xidget.swing.tabs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.plaf.TabbedPaneUI;
import org.xidget.Creator;
import org.xidget.IXidget;

/**
 * A custom JTabbedPane that supports a close button on each tab.
 */
public class CustomTabbedPane extends JTabbedPane
{
  public CustomTabbedPane( IXidget xidget)
  {
    super();
    this.xidget = xidget;
    addMouseListener( mouseListener);
    addMouseMotionListener( mouseMotionListener);
  }

  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent( Graphics g)
  {
    super.paintComponent( g);

    if ( currentTabBounds != null) 
    {
      if ( pressed) g.setColor( Color.lightGray);
      Rectangle buttonBounds = getCloseButtonBounds( currentTabBounds);
      int x1 = buttonBounds.x;
      int y1 = buttonBounds.y;
      int x2 = x1 + buttonBounds.width - 1;
      int y2 = y1 + buttonBounds.height;
      g.drawLine( x1, y1, x2, y2);
      g.drawLine( x1+1, y1, x2+1, y2);
      g.drawLine( x2, y1, x1, y2);
      g.drawLine( x2+1, y1, x1+1, y2);
    }
  }

  /**
   * Returns the bounds of the close button given the bounds of the tab.
   * @param tabBounds The tab bounds.
   * @return Returns the bounds of the close button given the bounds of the tab.
   */
  private Rectangle getCloseButtonBounds( Rectangle tabBounds)
  {
    int bw = 5;
    int bh = 5;
    int cy = currentTabBounds.y + (currentTabBounds.height / 2);
    int x1 = currentTabBounds.x + currentTabBounds.width - bw - 8;
    int y1 = cy - (bh / 2);
    return new Rectangle( x1, y1, bw+1, bh);
  }
  
  private MouseListener mouseListener = new MouseAdapter() {
    public void mousePressed( MouseEvent e)
    {
      if ( currentTabBounds != null)
      {
        Rectangle buttonBounds = getCloseButtonBounds( currentTabBounds);
        
        // HACK: trying to get the coverage correct
        buttonBounds.width *= 2; buttonBounds.height *= 2;
        
        if ( buttonBounds.contains( e.getX(), e.getY()))
        {
          pressed = true;
          if ( currentTabBounds != null) repaint( currentTabBounds);
        }
      }
    }
    public void mouseReleased( MouseEvent e)
    {
      pressed = false;
      if ( currentTabBounds != null)
      {
        Rectangle buttonBounds = getCloseButtonBounds( currentTabBounds);
        buttonBounds.width *= 2; buttonBounds.height *= 2;
        if ( buttonBounds.contains( e.getX(), e.getY()))
        {
          Component component = CustomTabbedPane.this.getComponentAt( currentTabIndex);
          for( IXidget child: xidget.getChildren())
          {
            if ( child.getFeature( JComponent.class) == component)
            {
              Creator.getInstance().destroy( child);
              repaint( currentTabBounds);
              currentTabBounds = null;
              break;
            }
          }
        }
      }
    }
    public void mouseExited( MouseEvent e)
    {
      pressed = false;
      if ( currentTabBounds != null) repaint( currentTabBounds);
      currentTabBounds = null;
    }
  };
  
  private MouseMotionListener mouseMotionListener = new MouseMotionAdapter() {
    public void mouseMoved( MouseEvent e)
    {
      if ( currentTabBounds != null) repaint( currentTabBounds);
      
      TabbedPaneUI ui = getUI();
      for( int i=0; i < getTabCount(); i++)
      {
        Rectangle bounds = ui.getTabBounds( CustomTabbedPane.this, i);
        if ( bounds.contains( e.getX(), e.getY()))
        {
          currentTabIndex = i;
          currentTabBounds = bounds;
          repaint( currentTabBounds);
          break;
        }
      }
    }
  };

  private IXidget xidget;
  private Rectangle currentTabBounds;
  private int currentTabIndex;
  private boolean pressed;
}
