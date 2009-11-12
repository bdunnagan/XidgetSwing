/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JTabbedPaneBindFeature.java
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
package org.xidget.swing.tabs;

import org.xidget.IXidget;
import org.xidget.feature.BindFeature;
import org.xmodel.IModelObject;

/**
 * A BindFeature for JTabbedPaneXidget that does not bind forms if it behaves like a dynamic container.
 */
public class JTabbedPaneBindFeature extends BindFeature
{
  public JTabbedPaneBindFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  public JTabbedPaneBindFeature( IXidget xidget, String[] ignore)
  {
    super( xidget, ignore);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.BindFeature#shouldIgnore(org.xmodel.IModelObject)
   */
  @Override
  protected boolean shouldIgnore( IModelObject config)
  {
    if ( super.shouldIgnore( config)) return true;
    
    IModelObject parentConfig = xidget.getConfig();
    if ( parentConfig.getAttribute( "children") != null) return true;
    if ( parentConfig.getFirstChild( "children") != null) return true;
    
    return false;
  }
}
