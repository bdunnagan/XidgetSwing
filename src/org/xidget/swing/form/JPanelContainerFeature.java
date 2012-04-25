/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.form;

import org.xidget.IXidget;
import org.xidget.swing.feature.GenericContainerFeature;

public class JPanelContainerFeature extends GenericContainerFeature
{
  public JPanelContainerFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#addWidget(org.xidget.IXidget)
   */
  @Override
  public void addWidget( IXidget child)
  {
    super.addWidget( child);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#addWidget(int, org.xidget.IXidget)
   */
  @Override
  public void addWidget( int index, IXidget child)
  {
    super.addWidget( index, child);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#removeWidget(org.xidget.IXidget)
   */
  @Override
  public void removeWidget( IXidget child)
  {
    super.removeWidget( child);
  }
}
