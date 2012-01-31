/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import org.xidget.ifeature.IColorFeature;
import org.xidget.util.XidgetUtil;

/**
 * An implementation of IColorFeature for the Swing platform.
 */
public class SwingColorFeature implements IColorFeature<Color>
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
  
  private Map<Object, Color> colors;
}
