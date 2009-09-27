/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
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
