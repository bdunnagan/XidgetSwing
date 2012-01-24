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
import org.xidget.swing.combo.CustomComboModel.Item;

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
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.setSelectedItem( object);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#deselect(java.lang.Object)
   */
  @Override
  public void deselect( Object object)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#setSelection(java.util.List)
   */
  @Override
  public void setSelection( List<? extends Object> objects)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.setSelectedItem( (objects.size() > 0)? objects.get( 0): null);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#getSelection()
   */
  @Override
  public List<? extends Object> getSelection()
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    Object selected = widget.getSelectedItem();
    if ( selected instanceof Item)
    {
      Item item = (Item)selected;
      return (selected != null)? Collections.singletonList( item.getContent()): Collections.emptyList();
    }
    return Collections.singletonList( selected);
  }

  private IXidget xidget;
}
