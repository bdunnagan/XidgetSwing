/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.label;

import java.awt.Graphics;
import javax.swing.JLabel;

/**
 * A JLabel that handles both foreground and background translucent colors.
 */
public class TransparentLabel extends JLabel
{
  public TransparentLabel()
  {
    setOpaque( false);
  }

  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent( Graphics g)
  {
    g.setColor( getBackground());
    g.fillRect( 0, 0, getWidth(), getHeight());
    super.paintComponent( g);
  }
  
  private static final long serialVersionUID = 1L;
}
