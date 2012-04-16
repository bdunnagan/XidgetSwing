/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.shape;

import java.awt.Graphics2D;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.canvas.ICanvasFeature;
import org.xidget.ifeature.canvas.IPaintFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingWidgetFeature;

/**
 */
public class ShapeXidget extends Xidget
{
  /* (non-Javadoc)
   * @see org.xidget.Xidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    bindFeature = new BindFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    paintFeature = new ShapePaintFeature( this);
    basicFeatureSet = new BasicFeatureSet( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IBindFeature.class) return (T)bindFeature;
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == IPaintFeature.class) return (T)paintFeature;
    if ( clss == ICanvasFeature.class) return (T)getParent().getFeature( ICanvasFeature.class);
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }

  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private IPaintFeature<Graphics2D> paintFeature;
  private IFeatured basicFeatureSet;
}
