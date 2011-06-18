/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.combo;

import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;

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
   * @see org.xidget.ifeature.ISelectionWidgetFeature#setSelection(java.util.List)
   */
  @Override
  public void setSelection( List<? extends Object> objects)
  {
    JComboBox jCombo = xidget.getFeature( JComboBox.class);
    if ( objects.size() > 0) jCombo.setSelectedItem( objects.get( 0));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#getSelection()
   */
  @Override
  public List<IModelObject> getSelection()
  {
    JComboBox jCombo = xidget.getFeature( JComboBox.class);
    Object selected = jCombo.getSelectedItem();
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
    JComboBox jCombo = xidget.getFeature( JComboBox.class);
    CustomComboModel model = (CustomComboModel)jCombo.getModel();
    model.setSelectedItem( object);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#removeSelected(int, java.lang.Object)
   */
  @Override
  public void removeSelected( int index, Object object)
  {
    JComboBox jCombo = xidget.getFeature( JComboBox.class);
    CustomComboModel model = (CustomComboModel)jCombo.getModel();
    model.setSelectedItem( null);
  }

  private IXidget xidget;
}
