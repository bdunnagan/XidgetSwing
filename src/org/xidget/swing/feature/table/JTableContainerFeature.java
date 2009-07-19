/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.table;

import org.xidget.IXidget;
import org.xidget.swing.feature.GenericContainerFeature;
import org.xmodel.IModelObject;

/**
 * An custom container feature for tables that handles the case of table cell editors being
 * added to a table.  Table cell editors are not parented with the table when they are 
 * created.  Instead, they are added temporarily when they are used for editing a cell.
 */
public class JTableContainerFeature extends GenericContainerFeature
{
  public JTableContainerFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#addWidget(org.xidget.IXidget)
   */
  public void addWidget( IXidget child)
  {
    if ( !isEditor( child)) super.addWidget( child);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#removeWidget(org.xidget.IXidget)
   */
  public void removeWidget( IXidget child)
  {
    if ( !isEditor( child)) super.removeWidget( child);
  }
  
  /**
   * Returns true if the specified child xidget is a cell editor.
   * @param child The child.
   * @return Returns true if the specified child xidget is a cell editor.
   */
  private boolean isEditor( IXidget child)
  {
    IModelObject config = child.getConfig();
    return config.getParent().isType( "editor");
  }
}
