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
      Object value = node.getValue();
      if ( value == null) value = new Bounds();

      Bounds bounds = null;
      if ( value instanceof Bounds)
      {
        bounds = (Bounds)value;
      }
      else
      {
        bounds = new Bounds();
        bounds.parse( value.toString());
      }
      
      Component component = e.getComponent();
      if ( component.getX() != bounds.x || component.getY() != bounds.y)
      {
        node.setValue( new Bounds( component.getX(), component.getY(), bounds.width, bounds.height));
      }
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
      Object value = node.getValue();
      if ( value == null) value = new Bounds();

      Bounds bounds = null;
      if ( value instanceof Bounds)
      {
        bounds = (Bounds)value;
      }
      else
      {
        bounds = new Bounds();
        bounds.parse( value.toString());
      }
      
      Component component = e.getComponent();
      if ( component.getWidth() != bounds.width || component.getHeight() != bounds.height)
      {
        node.setValue( new Bounds( bounds.x, bounds.y, component.getWidth(), component.getHeight()));
      }
    }
  }
  
  private IXidget xidget;
}
