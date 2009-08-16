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
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.IComputeNodeFeature.Type;
import org.xidget.layout.Bounds;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.Margins;
import org.xidget.layout.OffsetNode;
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
        // get inside margins of container
        IWidgetContainerFeature containerFeature = xidget.getFeature( IWidgetContainerFeature.class);
        Margins margins = containerFeature.getInsideMargins();
        
        // initialize container size
        initContainerSize( margins, xidget);
        
        // initialize the preferred size of each child
        for( IXidget child: xidget.getChildren())
          initPreferredSize( child);
        
        // layout
        IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
        feature.layout( contextFeature.getContext( parent));
        
        // set bounds of children
        for( IXidget child: xidget.getChildren())
          setChildBounds( margins, child);
        
        // inside dimensions of container may have been specified so update container size
        setContainerSize( margins, xidget);
      }
    }
  }
   
  /**
   * Initialize the container right and bottom nodes with the default size of the container.
   * @param margins The inside margins of the container.
   * @param xidget The xidget.
   */
  private static void initContainerSize( Margins margins, IXidget xidget)
  {
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = new Bounds(); widgetFeature.getBounds( bounds);
    
    if ( xidget.getConfig().getAttribute( "debug") != null)
      System.out.println( "Stopping.");
    
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    if ( bounds.width > 0) 
    {
      IComputeNode right = computeNodeFeature.getComputeNode( Type.right, true);
      right.setDefaultValue( bounds.width - margins.x0 - margins.x1);
      Log.printf( "layout", "Initialize container width of %s to %3.1f\n", xidget, bounds.width);
    }
    
    if ( bounds.height > 0) 
    {
      IComputeNode bottom = computeNodeFeature.getComputeNode( Type.bottom, true);
      bottom.setDefaultValue( bounds.height - margins.y0 - margins.y1);
      Log.printf( "layout", "Initialize container height of %s to %3.1f\n", xidget, bounds.height);
    }
  }
  
  /**
   * Create dependencies to implement the preferred size of the specified xidget.
   * @param xidget The xidget.
   */
  private static void initPreferredSize( IXidget xidget)
  {
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    if ( computeNodeFeature == null) return;
    
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Size size = new Size();
    widgetFeature.getPreferredSize( size);
    
    if ( size.width > 0)
    {
      //
      // Create dependencies in both directions so that nodes will be updated correctly
      // regardless of whether the left or the right node is anchored. Note that this
      // creates a cycle, but the sorting algorithm should insure that the these nodes
      // appear twice in the computation list.
      //
      IComputeNode left = computeNodeFeature.getComputeNode( Type.left, false);
      IComputeNode right = computeNodeFeature.getComputeNode( Type.right, false);
      right.addDependency( new OffsetNode( left, size.width));
      left.addDependency( new OffsetNode( right, -size.width));
    }
    
    if ( size.height > 0)
    {
      //
      // Create dependencies in both directions so that nodes will be updated correctly
      // regardless of whether the top or the bottom node is anchored. Note that this
      // creates a cycle, but the sorting algorithm should insure that the these nodes
      // appear twice in the computation list.
      //
      IComputeNode top = computeNodeFeature.getComputeNode( Type.top, false);
      IComputeNode bottom = computeNodeFeature.getComputeNode( Type.bottom, false);
      bottom.addDependency( new OffsetNode( top, size.height));
      top.addDependency( new OffsetNode( bottom, -size.height));
    }
    
    Log.printf( "layout", "Initalize preferred size of %s to %s\n", xidget, size);
  }
  
  /**
   * Set the bounds of the specified child based on its compute nodes.
   * @param margins The inside margins of the container.
   * @param xidget The xidget.
   */
  private static void setChildBounds( Margins margins, IXidget xidget)
  {
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    if ( computeNodeFeature == null) return; 

    IComputeNode top = computeNodeFeature.getComputeNode( Type.top, false);
    IComputeNode left = computeNodeFeature.getComputeNode( Type.left, false);
    IComputeNode right = computeNodeFeature.getComputeNode( Type.right, false);
    IComputeNode bottom = computeNodeFeature.getComputeNode( Type.bottom, false);
    
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);    
    Bounds bounds = new Bounds(); widgetFeature.getBounds( bounds);

    if ( xidget.getConfig().getAttribute( "debug") != null)
      System.out.println( "Stopping.");
    
    if ( top != null && top.hasValue()) bounds.y = top.getValue() + margins.y0; 
    if ( left != null && left.hasValue()) bounds.x = left.getValue() + margins.x0;
    
    if ( right != null && right.hasValue()) 
    {
      if ( left == null || !left.hasValue()) Log.printf( "layout", "Width of child not constrained: %s\n", xidget);
      bounds.width = right.getValue() - left.getValue();
    }
    
    if ( bottom != null && bottom.hasValue()) 
    {
      if ( top == null || !top.hasValue()) Log.printf( "layout", "Height of child not constrained: %s\n", xidget);
      bounds.height = bottom.getValue() - top.getValue();
    }
    
    widgetFeature.setBounds( bounds.x, bounds.y, bounds.width, bounds.height);
  }
  
  /**
   * Set the size of the container based on the inside width and height nodes. These values will
   * be specified if the layout contains attachments for the containers width, height, right or
   * bottom nodes.
   * @param margins The container inside margins.
   * @param xidget The container xidget.
   */
  private static void setContainerSize( Margins margins, IXidget xidget)
  {
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    if ( computeNodeFeature == null) return;
    
    if ( xidget.getConfig().getAttribute( "debug") != null)
      System.out.println( "Stopping.");
    
    IComputeNode right = computeNodeFeature.getComputeNode( Type.right, true);
    IComputeNode bottom = computeNodeFeature.getComputeNode( Type.bottom, true);
    
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = new Bounds(); widgetFeature.getBounds( bounds);
    
    if ( right.hasValue()) bounds.width = right.getValue() + margins.x0 + margins.x1;
    if ( bottom.hasValue()) bounds.height = bottom.getValue() + margins.y0 + margins.y1;
    
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
