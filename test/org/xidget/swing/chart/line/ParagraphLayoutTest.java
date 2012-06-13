/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.line;

import static org.junit.Assert.assertTrue;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;

import org.junit.Before;
import org.junit.Test;
import org.xidget.swing.chart.line.ParagraphLayout.Justify;

public class ParagraphLayoutTest
{
  static double width;
  
  @Before public void setup()
  {
    width = 0;
  }
  
  @Test public void leftJustifyText()
  {
    TestGraphics g = new TestGraphics() {
      public void drawGlyphVector( GlyphVector gv, float x, float y)
      {
        assertTrue( "Text not left justified.", x == 0);
        assertTrue( "Illegal vertical position.", y >= 0);
      }
    };
    
    String text = "One\nOne\nOne";
    FontRenderContext frc = new FontRenderContext( AffineTransform.getScaleInstance( 1, 1), false, false);
    ParagraphLayout para = new ParagraphLayout( text, new Font( "Times", Font.PLAIN, 12), Justify.left, frc);
    para.draw( g, 0, 0);
  }
  
  @Test public void centerJustifyText()
  {
    TestGraphics g = new TestGraphics() {
      public void drawGlyphVector( GlyphVector gv, float x, float y)
      {
        assertTrue( "Illegal vertical position.", y >= 0);
      }
    };
    
    String text = "One\nOne\nOne";
    FontRenderContext frc = new FontRenderContext( AffineTransform.getScaleInstance( 1, 1), false, false);
    ParagraphLayout para = new ParagraphLayout( text, new Font( "Times", Font.PLAIN, 12), Justify.left, frc);
    para.draw( g, 0, 0);
  }
}
