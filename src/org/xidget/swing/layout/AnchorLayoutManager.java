/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IWidgetContextFeature;

/**
 * An implementation of LayoutManager that uses the AnchorLayoutFeature.
 */
public class AnchorLayoutManager implements LayoutManager
{
  public AnchorLayoutManager( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
   */
  public void addLayoutComponent( String name, Component component)
  {
    preferred = null;
  }

  /* (non-Javadoc)
   * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
   */
  public void removeLayoutComponent( Component component)
  {
    preferred = null;
  }

  /* (non-Javadoc)
   * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
   */
  public void layoutContainer( Container parent)
  {
    synchronized( parent.getTreeLock()) 
    {
      ILayoutFeature feature = xidget.getFeature( ILayoutFeature.class);
      if ( feature != null) 
      {
        IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
        feature.layout( contextFeature.getContext( parent));
      }
    }
  }
   
  /* (non-Javadoc)
   * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
   */
  public Dimension minimumLayoutSize( Container parent)
  {
    return new Dimension( 5, 5);
  }

  /* (non-Javadoc)
   * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
   */
  public Dimension preferredLayoutSize( Container parent)
  {
    if ( preferred == null)
    {
      layoutContainer( parent);
  
      // see if size already computed
      Rectangle bounds = new Rectangle();
      parent.getBounds( bounds);
      if ( bounds.width > 0 || bounds.height > 0)
      {
        preferred = new Dimension( bounds.width, bounds.height);
        return preferred;
      }
      
      // compute size
      bounds = new Rectangle();
      Rectangle childBounds = new Rectangle();
      for( Component child: parent.getComponents())
      {
        child.getBounds( childBounds);
        bounds.add( childBounds);
      }
      
      Insets insets = parent.getInsets();
      preferred = new Dimension( bounds.width + insets.left + insets.right, bounds.height + insets.top + insets.bottom);
    }
    
    return preferred;
  }
  
  private IXidget xidget;
  private Dimension preferred;
}
