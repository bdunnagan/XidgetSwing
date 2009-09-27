/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import org.xidget.IFeatured;
import org.xidget.IXidget;
import org.xidget.feature.DragAndDropFeature;
import org.xidget.feature.ScriptFeature;
import org.xidget.feature.WidgetContextFeature;
import org.xidget.ifeature.IAsyncFeature;
import org.xidget.ifeature.IDragAndDropFeature;
import org.xidget.ifeature.IErrorFeature;
import org.xidget.ifeature.IScriptFeature;
import org.xidget.ifeature.IWidgetContextFeature;

/**
 * A convenience class which exports the common feature set for all xidgets.
 */
public class BasicFeatureSet implements IFeatured
{
  /**
   * Create the BasicFeatureSet. This should be called from within 
   * the <code>createFeatures</code> method of the xidget.
   * @param xidget The xidget.
   */
  public BasicFeatureSet( IXidget xidget)
  {
    asyncFeature = new AsyncFeature();
    errorFeature = new AdjacentErrorFeature( xidget);
    scriptFeature = new ScriptFeature( xidget);
    contextFeature = new WidgetContextFeature();
    dndFeature = new DragAndDropFeature( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IFeatured#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IAsyncFeature.class) return (T)asyncFeature;
    if ( clss == IErrorFeature.class) return (T)errorFeature;
    if ( clss == IScriptFeature.class) return (T)scriptFeature;
    if ( clss == IWidgetContextFeature.class) return (T)contextFeature;
    if ( clss == IDragAndDropFeature.class) return (T)dndFeature;
    return null;
  }
  
  private IAsyncFeature asyncFeature;
  private IErrorFeature errorFeature;
  private IScriptFeature scriptFeature;
  private IWidgetContextFeature contextFeature;
  private IDragAndDropFeature dndFeature;
}
