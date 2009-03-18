package org.xidget.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import org.xidget.layout.ILayoutFeature;

/**
 * An implementation of LayoutManager that uses the AnchorLayoutFeature.
 */
public class AnchorLayoutManager implements LayoutManager
{
  public AnchorLayoutManager( ILayoutFeature layout)
  {
    this.layout = layout;
  }
  
  /* (non-Javadoc)
   * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
   */
  public void addLayoutComponent( String name, Component comp)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
   */
  public void removeLayoutComponent( Component comp)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
   */
  public void layoutContainer( Container parent)
  {
    for( Component child: parent.getComponents())
    {
      Dimension size = child.getPreferredSize();
      child.setSize( size);
    }
    
    synchronized( parent.getTreeLock()) 
    {
      layout.layout();
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
    layoutContainer( parent);
    
    // see if size already computed
    Rectangle bounds = new Rectangle();
    parent.getBounds( bounds);
    if ( bounds.width > 0 || bounds.height > 0)
      return new Dimension( bounds.width, bounds.height);
    
    // compute size
    bounds = new Rectangle();
    Rectangle childBounds = new Rectangle();
    for( Component child: parent.getComponents())
    {
      child.getBounds( childBounds);
      bounds.add( childBounds);
    }
    
    return bounds.getSize();
  }

  private ILayoutFeature layout;
}
