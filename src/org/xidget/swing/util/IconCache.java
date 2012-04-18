/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.util;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * A convenience class that maps images in the model to icons.
 */
public class IconCache
{
  protected IconCache()
  {
    map = new HashMap<Object, Icon>();
  }

  /**
   * Clear cached icons.
   */
  public void reset()
  {
    map.clear();
  }
  
  /**
   * Get/create the icon for the specified image.
   * @param image The image.
   * @return Returns the icon.
   */
  public Icon getIcon( Object image)
  {
    if ( image == null) return null;
    
    Icon icon = map.get( image);
    if ( icon == null)
    {
      icon = new ImageIcon( (Image)image);
      map.put( image, icon);
    }
    return icon;
  }

  /**
   * @return Returns an instance of IconCache.
   */
  public static IconCache getInstance()
  {
    IconCache instance = instances.get();
    if ( instance == null)
    {
      instance = new IconCache();
      instances.set( instance);
    }
    return instance;
  }
  
  private static ThreadLocal<IconCache> instances = new ThreadLocal<IconCache>();
  private Map<Object, Icon> map;
}
