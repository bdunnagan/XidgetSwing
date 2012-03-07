/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;
import org.xidget.ifeature.IColorFeature;
import org.xidget.util.XidgetUtil;

/**
 * An implementation of IColorFeature for the Swing platform.
 */
public class SwingColorFeature implements IColorFeature<Color, Graphics2D>
{
  public SwingColorFeature()
  {
    colors = new HashMap<Object, Color>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IColorFeature#getColor(java.lang.String)
   */
  @Override
  public Color getColor( Object color)
  {
    if ( color == null) return null;
    
    Color result = colors.get( color);
    if ( result != null) return result;
    
    if ( color instanceof Color) 
    {
      result = (Color)color;
    }
    else if ( color instanceof Number)
    {
      int rgba = ((Number)color).intValue();
      result = new Color( rgba, true);
      colors.put( color, result);
    }
    else
    {
      float[] channels = XidgetUtil.parseColor( color.toString());
      if ( channels.length == 3)
      {
        result = new Color( channels[ 0], channels[ 1], channels[ 2]);
        colors.put( color, result);
      }
      else
      {
        result = new Color( channels[ 0], channels[ 1], channels[ 2], channels[ 3]);
        colors.put( color, result);
      }
    }
    
    return result;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IColorFeature#applyColor(java.lang.Object, java.lang.Object, int, int)
   */
  @Override
  public void applyColor( Object spec, Graphics2D graphics, int width, int height)
  {
    if ( spec == null) return;

    String[] parts = spec.toString().split( "[,-]");
    if ( parts.length == 1)
    {
      Color color = getColor( spec);
      if ( color != null) graphics.setColor( color);
    }
    else
    {
      Color color1 = getColor( parts[ 0]);
      Color color2 = getColor( parts[ 1]);
      
      float x1 = 0;
      float y1 = 0;
          
      float angle = (parts.length == 3)? Float.parseFloat( parts[ 2]): 90;
      if ( angle > 90 && angle <= 270) x1 = width;
      if ( angle > 180) y1 = height;

      angle = angle * (float)Math.PI / 180;
      float x2 = (float)(width * Math.cos( angle) + x1);
      float y2 = (float)(height * Math.sin( angle) + y1);
      
      GradientPaint paint = new GradientPaint( x1, y1, color1, x2, y2, color2);
      graphics.setPaint( paint);
    }
  }

  private Map<Object, Color> colors;
}
