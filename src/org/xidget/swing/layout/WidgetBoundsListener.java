/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.layout;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.Bounds;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

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
    Bounds bounds = widgetFeature.getComputedBounds();
    
    // update bounds node
    IModelObject boundsNode = widgetFeature.getBoundsNode();
    if ( boundsNode != null)
    {
      Xlate.set( boundsNode, bounds.toString());
    }
    
    // update computed bounds
    widgetFeature.setComputedBounds( e.getComponent().getX(), e.getComponent().getY(), bounds.width, bounds.height);
  }

  /* (non-Javadoc)
   * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
   */
  @Override
  public void componentResized( ComponentEvent e)
  {
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = widgetFeature.getComputedBounds();
    
    // update bounds node
    IModelObject boundsNode = widgetFeature.getBoundsNode();
    if ( boundsNode != null)
    {
      Xlate.set( boundsNode, bounds.toString());
    }
    
    // update computed bounds
    widgetFeature.setComputedBounds( bounds.x, bounds.y, e.getComponent().getWidth(), e.getComponent().getHeight());
  }
  
  private IXidget xidget;
}
