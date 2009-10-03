/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.tabs;

import java.util.Collections;
import java.util.List;
import javax.swing.JTabbedPane;
import org.xidget.IXidget;
import org.xidget.ifeature.IDynamicContainerFeature;
import org.xidget.ifeature.ISelectionWidgetFeature;
import org.xmodel.IModelObject;

/**
 * @author bdunnagan
 *
 */
public class JTabbedPaneSelectionWidgetFeature implements ISelectionWidgetFeature
{
  public JTabbedPaneSelectionWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#getSelection()
   */
  public List<IModelObject> getSelection()
  {
    return Collections.singletonList( selection);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#insertSelected(int, org.xmodel.IModelObject)
   */
  public void insertSelected( int index, IModelObject element)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#removeSelected(int, org.xmodel.IModelObject)
   */
  public void removeSelected( int index, IModelObject element)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#setSelection(java.util.List)
   */
  public void setSelection( List<IModelObject> nodes)
  {
    if ( nodes == null || nodes.size() == 0) return;
    
    selection = nodes.get( 0);
    
    IDynamicContainerFeature containerFeature = xidget.getFeature( IDynamicContainerFeature.class);
    List<IModelObject> children = containerFeature.getChildren();
    int index = children.indexOf( selection);
    
    if ( index >= 0)
    {
      JTabbedPane jTabbedPane = xidget.getFeature( JTabbedPane.class);
      jTabbedPane.setSelectedIndex( index);
    }
  }

  private IXidget xidget;
  private IModelObject selection;
}
