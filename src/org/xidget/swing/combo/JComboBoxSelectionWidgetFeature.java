/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.combo;

import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;

import org.xidget.IXidget;
import org.xidget.ifeature.model.ISelectionWidgetFeature;
import org.xidget.swing.combo.JComboBoxMultiValueWidgetFeature.Item;

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
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#select(java.lang.Object)
   */
  @Override
  public void select( Object object)
  {
    JComboBox jCombo = xidget.getFeature( JComboBox.class);
    CustomComboModel model = (CustomComboModel)jCombo.getModel();
    model.setSelectedItem( object);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#deselect(java.lang.Object)
   */
  @Override
  public void deselect( Object object)
  {
    JComboBox jCombo = xidget.getFeature( JComboBox.class);
    CustomComboModel model = (CustomComboModel)jCombo.getModel();
    Object selected = model.getSelectedItem();
    if ( selected == object) model.setSelectedItem( "");
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#setSelection(java.util.List)
   */
  @Override
  public void setSelection( List<? extends Object> objects)
  {
    JComboBox jCombo = xidget.getFeature( JComboBox.class);
    jCombo.setSelectedItem( (objects.size() > 0)? objects.get( 0): null);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#getSelection()
   */
  @Override
  public List<? extends Object> getSelection()
  {
    JComboBox jCombo = xidget.getFeature( JComboBox.class);
    Object selected = jCombo.getSelectedItem();
    if ( selected != null)
    {
      if ( selected instanceof Item)
      {
        Item item = (Item)selected;
        return Collections.singletonList( item.value);
      }
      else
      {
        for( int i=0; i<jCombo.getItemCount(); i++)
        {
          Item item = (Item)jCombo.getItemAt( i);
          if ( item.equals( selected)) return Collections.singletonList( item.value);
        }
      }
    }
    return Collections.emptyList();
  }

  private IXidget xidget;
}
