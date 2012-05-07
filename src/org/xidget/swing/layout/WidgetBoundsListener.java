/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.layout;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.Bounds;
import org.xmodel.IModelObject;

/**
 * A ComponentListener that keeps synchronizes the computed bounds of a xidget
 * with the bounds of its implementing widget.
 */
public class WidgetBoundsListener extends ComponentAdapter
{
  public WidgetBoundsListener( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
   */
  @Override
  public void componentMoved( ComponentEvent e)
  {
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    IModelObject node = widgetFeature.getBoundsNode();
    if ( node != null)
    {
      Bounds bounds = getBoundsFromNode( node);
      Component component = e.getComponent();
      if ( bounds.isXDefined() && bounds.x != component.getX()) bounds.x = component.getX();
      if ( bounds.isYDefined() && bounds.y != component.getX()) bounds.y = component.getY();
      if ( bounds.isXDefined() || bounds.isYDefined()) node.setValue( bounds);
    }
  }

  /* (non-Javadoc)
   * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
   */
  @Override
  public void componentResized( ComponentEvent e)
  {
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    IModelObject node = widgetFeature.getBoundsNode();
    if ( node != null)
    {
      Bounds bounds = getBoundsFromNode( node);
      Component component = e.getComponent();
      if ( bounds.isWidthDefined() && bounds.width != component.getWidth()) bounds.width = component.getWidth();
      if ( bounds.isHeightDefined() && bounds.height != component.getHeight()) bounds.height = component.getHeight();
      if ( bounds.isWidthDefined() || bounds.isHeightDefined()) node.setValue( bounds);
    }
  }
  
  private Bounds getBoundsFromNode( IModelObject node)
  {
    Object value = node.getValue();
    if ( value == null) return new Bounds();

    if ( value instanceof Bounds) return new Bounds( (Bounds)value);
    
    Bounds bounds = new Bounds();
    bounds.parse( value.toString());
    return bounds;
  }
  
  private IXidget xidget;
}
