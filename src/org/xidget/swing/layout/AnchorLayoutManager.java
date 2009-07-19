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
import org.xidget.Log;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.IComputeNodeFeature.Type;
import org.xidget.layout.Bounds;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.Size;

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
  }

  /* (non-Javadoc)
   * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
   */
  public void removeLayoutComponent( Component component)
  {
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
        initContainerSize( xidget);
        
        // initialize the preferred size of each child
        for( IXidget child: xidget.getChildren())
          initPreferredSize( child);
        
        // layout
        IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
        feature.layout( contextFeature.getContext( parent));
        
        // set bounds of children
        for( IXidget child: xidget.getChildren())
          setChildBounds( child);
        
        // inside dimensions of container may have been specified so update container size
        setContainerSize( xidget);
      }
    }
  }
   
  /**
   * Initialize the outside width and height nodes based on the current size.
   * @param xidget The xidget.
   */
  private static void initContainerSize( IXidget xidget)
  {
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = new Bounds(); widgetFeature.getBounds( bounds);
    
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    IComputeNode width = computeNodeFeature.getComputeNode( Type.width, true);
    if ( bounds.width > 0) width.setDefaultValue( bounds.width);
    
    IComputeNode height = computeNodeFeature.getComputeNode( Type.height, true);
    if ( bounds.height > 0) height.setDefaultValue( bounds.height);
  }
  
  /**
   * Initialize the outside width and height nodes based on the preferred size.
   * @param xidget The xidget.
   */
  private static void initPreferredSize( IXidget xidget)
  {
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Size size = new Size();
    widgetFeature.getPreferredSize( size);
    
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    IComputeNode width = computeNodeFeature.getComputeNode( Type.width, false);
    if ( size.width > 0) width.setDefaultValue( (float)size.width);
    
    IComputeNode height = computeNodeFeature.getComputeNode( Type.height, false);
    if ( size.height > 0) height.setDefaultValue( (float)size.height);
    
    Log.printf( "layout", "Initalize preferred size of %s to %s\n", xidget, size);
  }
  
  /**
   * Set the bounds of the specified child based on its compute nodes.
   * @param xidget The xidget.
   */
  private static void setChildBounds( IXidget xidget)
  {
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    IComputeNode top = computeNodeFeature.getComputeNode( Type.top, false);
    IComputeNode left = computeNodeFeature.getComputeNode( Type.left, false);
    IComputeNode width = computeNodeFeature.getComputeNode( Type.width, false);
    IComputeNode height = computeNodeFeature.getComputeNode( Type.height, false);
    
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);    
    Bounds bounds = new Bounds(); widgetFeature.getBounds( bounds);

    if ( top != null && top.hasValue()) bounds.y = top.getValue(); 
    if ( left != null && left.hasValue()) bounds.x = left.getValue();
    if ( width != null && width.hasValue()) bounds.width = width.getValue();
    if ( height != null && height.hasValue()) bounds.height = height.getValue();
    
    widgetFeature.setBounds( bounds.x, bounds.y, bounds.width, bounds.height);
  }
  
  /**
   * Set the size of the container based on the inside width and height nodes. These values will
   * be specified if the layout contains attachments for the containers width, height, right or
   * bottom nodes.
   * @param xidget The container xidget.
   */
  private static void setContainerSize( IXidget xidget)
  {
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    IComputeNode insideWidth = computeNodeFeature.getComputeNode( Type.width, true);
    IComputeNode insideHeight = computeNodeFeature.getComputeNode( Type.height, true);
    
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = new Bounds(); widgetFeature.getBounds( bounds);
    
    if ( insideWidth.hasValue()) 
    {
      bounds.width = insideWidth.getValue();
      computeNodeFeature.getComputeNode( Type.width, false).setDefaultValue( bounds.width);
    }
    
    if ( insideHeight.hasValue()) 
    {
      bounds.height = insideHeight.getValue();
      computeNodeFeature.getComputeNode( Type.height, false).setDefaultValue( bounds.height);
    }
    
    // set widget size
    widgetFeature.setBounds( bounds.x, bounds.y, bounds.width, bounds.height);
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
    
    Insets insets = parent.getInsets();
    return new Dimension( bounds.width + insets.left + insets.right, bounds.height + insets.top + insets.bottom);
  }
  
  private IXidget xidget;
}
