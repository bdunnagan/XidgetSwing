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
    Item item = new Item( object);
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.setSelectedItem( item);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#deselect(java.lang.Object)
   */
  @Override
  public void deselect( Object object)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    Item item = (Item)widget.getSelectedItem();
    if ( item.equals( object)) widget.setSelectedItem( null);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#setSelection(java.util.List)
   */
  @Override
  public void setSelection( List<? extends Object> objects)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.setSelectedItem( (objects.size() > 0)? new Item( objects.get( 0)): null);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#getSelection()
   */
  @Override
  public List<? extends Object> getSelection()
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    Item selected = (Item)widget.getSelectedItem();
    return (selected != null)? Collections.singletonList( selected.value): Collections.emptyList();
  }

  private IXidget xidget;
}
