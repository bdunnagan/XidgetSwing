/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.line;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Convenience class to layout multiple lines of text delimited by newline.  Internally, the class
 * creates one or TextLayout instances.  A single method is provided to render the paragraph in a
 * graphics context.  Methods are also provided to get the bounds of the paragraph.
 */
public class ParagraphLayout
{
  public enum Justify { left, center, right};
  
  /**
   * Create a paragraph layout for the specified text.
   * @param text Text, possibly containing newline characters.
   * @param font The font.
   * @param justify The justification. 
   */
  public ParagraphLayout( final String text, final Font font, final Justify justify, FontRenderContext frc)
  {
    this.text = text;
    this.font = font;
    this.justify = justify;
    this.separators = new char[] {'\n', '\r'};
    this.frc = frc;
    
    format();
  }

  /**
   * @return Returns the width of the paragraph in pixels.
   */
  public final float getWidth()
  {
    return width;
  }
  
  /**
   * @return Returns the height of the paragraph in pixels.
   */
  public final float getHeight()
  {
    return height;
  }
  
  /**
   * Draw paragraph on the specified graphics context.
   * @param g The graphics context.
   * @param x The x-coordinate.
   * @param y The y-coordinate.
   */
  public void draw( Graphics2D g, float x, float y)
  {
    float width = 0;
    for( TextLayout layout: layouts)
    {
      if ( width < layout.getAdvance())
        width = layout.getAdvance();
    }
    
    float cx = width / 2;
    for( TextLayout layout: layouts)
    {
      float jx = x;
      switch( justify)
      {
        case center: jx = cx - (layout.getAdvance() / 2); break;
        case right:  jx = width - layout.getAdvance(); break;
      }
      
      layout.draw( g, jx, y + layout.getAdvance());
      y += layout.getAdvance() + layout.getDescent() + layout.getLeading();
    }
  }

  /**
   * Initialize the TextLayout instances.
   */
  private final void format()
  {
    layouts = new ArrayList<TextLayout>();
    
    int start = 0;
    int index = indexOf( text, start, separators); 
    while( index < text.length())
    {
      TextLayout layout = new TextLayout( text.substring( start, index), font, frc);
      layouts.add( layout);
      
      start = index + 1;
      index = indexOf( text, start, separators);
    }
    
    // compute dimensions
    width = 0;
    height = 0;
    float leading = 0;
    for( TextLayout layout: layouts)
    {
      height += leading;
      
      if ( width < layout.getAdvance())
        width = layout.getAdvance();
      
      height += layout.getAdvance() + layout.getDescent();
      leading = layout.getLeading();
    }
  }
  
  /**
   * Find the first occurrence of one of the specified characters
   * @param text The text to search.
   * @param start The starting index.
   * @param chars The array of characters to find.
   * @return Returns length of text or the index of the character that was found.
   */
  private final int indexOf( final String text, final int start, final char[] chars)
  {
    for( int i=start; i<text.length(); i++)
    {
      final char c = text.charAt( i);
      for( int j=0; j<chars.length; j++)
      {
        if ( c == chars[ j]) return i;
        if ( c < chars[ j]) break;
      }
    }
    return text.length();
  }

  private final String text;
  private final Font font;
  private final Justify justify;
  private final FontRenderContext frc;
  private List<TextLayout> layouts;
  private char[] separators;
  private float width;
  private float height;
}
