/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.spinner;

import java.util.Collections;
import java.util.List;

import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

import org.xidget.IXidget;
import org.xidget.ifeature.ISelectionWidgetFeature;
import org.xidget.swing.combo.CustomComboModel;
import org.xmodel.IModelObject;

/**
 * An implementation of ISelectionWidgetFeature for use with JSpinner widgets.
 */
public class JSpinnerSelectionWidgetFeature implements ISelectionWidgetFeature
{
  public JSpinnerSelectionWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#setSelection(java.util.List)
   */
  @Override
  public void setSelection( List<? extends Object> objects)
  {
    JSpinner jSpinner = xidget.getFeature( JSpinner.class);
    if ( objects.size() > 0) jSpinner.setValue( objects.get( 0));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#getSelection()
   */
  @Override
  public List<IModelObject> getSelection()
  {
    JSpinner jSpinner = xidget.getFeature( JSpinner.class);
    Object selected = jSpinner.getValue();
    if ( selected != null && selected instanceof IModelObject)
    {
      return Collections.singletonList( (IModelObject)selected);
    }
    return Collections.emptyList();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#insertSelected(int, java.lang.Object)
   */
  @Override
  public void insertSelected( int index, Object object)
  {
    JSpinner jSpinner = xidget.getFeature( JSpinner.class);
    SpinnerListModel model = (SpinnerListModel)jSpinner.getModel();
    model.setValue( object);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#removeSelected(int, java.lang.Object)
   */
  @Override
  public void removeSelected( Object object)
  {
    JSpinner jSpinner = xidget.getFeature( JSpinner.class);
    CustomComboModel model = (CustomComboModel)jSpinner.getModel();
    model.setSelectedItem( null);
  }

  private IXidget xidget;
}
