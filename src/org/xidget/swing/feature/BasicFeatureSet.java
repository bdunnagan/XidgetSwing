/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * BasicFeatureSet.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.swing.feature;

import org.xidget.IFeatured;
import org.xidget.IXidget;
import org.xidget.feature.DragAndDropFeature;
import org.xidget.feature.ScriptFeature;
import org.xidget.feature.WidgetContextFeature;
import org.xidget.ifeature.IDragAndDropFeature;
import org.xidget.ifeature.IKeyFeature;
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
    scriptFeature = new ScriptFeature( xidget);
    contextFeature = new WidgetContextFeature();
    dndFeature = new DragAndDropFeature( xidget);
    keyFeature = new KeyFeature( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IFeatured#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IScriptFeature.class) return (T)scriptFeature;
    if ( clss == IWidgetContextFeature.class) return (T)contextFeature;
    if ( clss == IDragAndDropFeature.class) return (T)dndFeature;
    if ( clss == IKeyFeature.class) return (T)keyFeature;
    return null;
  }
  
  private IScriptFeature scriptFeature;
  private IWidgetContextFeature contextFeature;
  private IDragAndDropFeature dndFeature;
  private IKeyFeature keyFeature;
}
