/**
 * Xidget - WYSIWYG Xidget Builder
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */

package org.xidget.swing.xmleditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.text.JTextComponent;
import javax.swing.text.Highlighter.HighlightPainter;

/**
 * An implementation of HighlightPainter for highlighting parse errors.
 */
public class ErrorHighlightPainter implements HighlightPainter
{
  /* (non-Javadoc)
   * @see javax.swing.text.Highlighter.HighlightPainter#paint(java.awt.Graphics, int, int, java.awt.Shape, javax.swing.text.JTextComponent)
   */
  public void paint( Graphics g, int p0, int p1, Shape shape, JTextComponent c)
  {
    try
    {
      Rectangle r0 = c.modelToView( p0);
      Rectangle r1 = c.modelToView( p1);
      Rectangle r = r0.union( r1);
      
      Graphics2D g2d = (Graphics2D)g;
      Color color = g2d.getColor();
      g2d.setColor( Color.pink);
      g2d.fillRect( r.x, r.y, r.width, r.height);
      g2d.setColor( color);
    }
    catch( Exception e)
    {
    }
  }
}
