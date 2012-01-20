/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;

import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.Bounds;

/**
 * A LayoutManager2 that serves as an adapter between the Swing top-level component
 * layout mechanism and the xidget layout mechanism.
 */
public class AdapterLayoutManager implements LayoutManager2
{
  public AdapterLayoutManager( IXidget xidget, LayoutManager delegate)
  {
    this.xidget = xidget;
    this.delegate = delegate;
  }
  
  /* (non-Javadoc)
   * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component, java.lang.Object)
   */
  public void addLayoutComponent( Component comp, Object constraints)
  {
    if ( delegate instanceof LayoutManager2)
      ((LayoutManager2)delegate).addLayoutComponent( comp, constraints);
  }

  /* (non-Javadoc)
   * @see java.awt.LayoutManager2#getLayoutAlignmentX(java.awt.Container)
   */
  public float getLayoutAlignmentX( Container target)
  {
    if ( delegate instanceof LayoutManager2)
      return ((LayoutManager2)delegate).getLayoutAlignmentX( target);
    return 0;
  }

  /* (non-Javadoc)
   * @see java.awt.LayoutManager2#getLayoutAlignmentY(java.awt.Container)
   */
  public float getLayoutAlignmentY( Container target)
  {
    if ( delegate instanceof LayoutManager2)
      return ((LayoutManager2)delegate).getLayoutAlignmentY( target);
    return 0;
  }

  /* (non-Javadoc)
   * @see java.awt.LayoutManager2#invalidateLayout(java.awt.Container)
   */
  public void invalidateLayout( Container target)
  {
    if ( delegate instanceof LayoutManager2)
      ((LayoutManager2)delegate).invalidateLayout( target);
  }

  /* (non-Javadoc)
   * @see java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
   */
  public Dimension maximumLayoutSize( Container target)
  {
    if ( delegate instanceof LayoutManager2)
      return ((LayoutManager2)delegate).maximumLayoutSize( target);
    return new Dimension( 0, 0);
  }

  /* (non-Javadoc)
   * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
   */
  public void addLayoutComponent( String name, Component comp)
  {
    delegate.addLayoutComponent( name, comp);
  }

  /* (non-Javadoc)
   * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
   */
  public Dimension minimumLayoutSize( Container parent)
  {
    return delegate.minimumLayoutSize( parent);
  }

  /* (non-Javadoc)
   * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
   */
  public void removeLayoutComponent( Component comp)
  {
    delegate.removeLayoutComponent( comp);
  }

  /* (non-Javadoc)
   * @see java.awt.FlowLayout#layoutContainer(java.awt.Container)
   */
  public void layoutContainer( Container target)
  {
    delegate.layoutContainer( target);
    
    //
    // Update computed bounds of the xidget in the content pane. This must be done here to insure
    // that the bounds are up-to-date during the layout flow.
    //
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    if ( widgetFeature != null)
    {
      Component widget = xidget.getFeature( Component.class);
      if ( widget != null)
      {
        widgetFeature.setComputedBounds( widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
      }
    }
  }

  /* (non-Javadoc)
   * @see java.awt.FlowLayout#preferredLayoutSize(java.awt.Container)
   */
  public Dimension preferredLayoutSize( Container target)
  {
    //
    // This method will be called if the top-level xidget does not have a default bounds.
    // See JFrameWidgetFeature.setVisible method as an example.
    //
    ILayoutFeature layoutFeature = xidget.getFeature( ILayoutFeature.class);
    if ( layoutFeature != null) 
    {
      Component widget = xidget.getFeature( Component.class);
      IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
      layoutFeature.layout( contextFeature.getContext( widget));
      
      IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
      Bounds bounds = widgetFeature.getComputedBounds();
      return new Dimension( (int)Math.round( bounds.width), (int)Math.round( bounds.height));
    }
    
    return new Dimension( 0, 0);
  }

  private IXidget xidget;
  private LayoutManager delegate;
}
