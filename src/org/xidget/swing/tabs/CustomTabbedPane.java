/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
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
      int x2 = x1 + buttonBounds.width;
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
    return new Rectangle( x1, y1, bw, bh);
  }
  
  private MouseListener mouseListener = new MouseAdapter() {
    public void mousePressed( MouseEvent e)
    {
      Rectangle buttonBounds = getCloseButtonBounds( currentTabBounds);
      if ( buttonBounds.contains( e.getX(), e.getY()))
      {
        pressed = true;
        if ( currentTabBounds != null) repaint( currentTabBounds);
      }
    }
    public void mouseReleased( MouseEvent e)
    {
      if ( currentTabBounds != null)
      {
        Rectangle buttonBounds = getCloseButtonBounds( currentTabBounds);
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
