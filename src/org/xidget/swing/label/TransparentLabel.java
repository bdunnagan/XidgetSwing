/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.label;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;

/**
 * Implementation of JLabel that supports both foreground and background transparency.
 * The isOpaque() method is overridden to always return false.  Normally, this causes
 * JLabel to not draw its background at all, which is incorrect, since isOpaque is
 * just a hint to the Swing painting algorithm.  Note that JLabel is not opaque by
 * default, and therefore does not draw its background.
 */
public class TransparentLabel extends JLabel
{
  private static final long serialVersionUID = 6931269104922870224L;

  public TransparentLabel()
  {
  }

  /* (non-Javadoc)
   * @see javax.swing.JComponent#isOpaque()
   */
  @Override
  public boolean isOpaque()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see javax.swing.JComponent#setForeground(java.awt.Color)
   */
  @Override
  public void setForeground( Color fg)
  {
    super.setForeground( fg);
    repaint();
    validate();
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
}
