/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.combo;

import java.util.List;

import org.xidget.IXidget;
import org.xidget.ifeature.ISelectionWidgetFeature;
import org.xmodel.IModelObject;

/**
 * An implementation of ISelectionWidgetFeature for use with JComboBox widgets.
 */
public class JComboBoxSelectionWidgetFeature implements ISelectionWidgetFeature
{
  public JComboBoxSelectionWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#getSelection()
   */
  @Override
  public List<IModelObject> getSelection()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#insertSelected(int, org.xmodel.IModelObject)
   */
  @Override
  public void insertSelected( int index, IModelObject element)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#removeSelected(int, org.xmodel.IModelObject)
   */
  @Override
  public void removeSelected( int index, IModelObject element)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#setSelection(java.util.List)
   */
  @Override
  public void setSelection( List<IModelObject> nodes)
  {
  }

  private IXidget xidget;
}
