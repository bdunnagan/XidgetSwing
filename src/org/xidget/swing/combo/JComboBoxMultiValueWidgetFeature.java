/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.combo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import org.xidget.IXidget;
import org.xidget.ifeature.model.IMultiValueWidgetFeature;

/**
 * An implementation of IMultiValueWidgetFeature for JComboBox.
 */
public class JComboBoxMultiValueWidgetFeature implements IMultiValueWidgetFeature
{
  public JComboBoxMultiValueWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueWidgetFeature#insertValue(int, java.lang.Object)
   */
  @Override
  public void insertValue( int index, Object value)
  {
    Item item = new Item( value);
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.insertItemAt( item, index);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueWidgetFeature#updateValue(int, java.lang.Object)
   */
  @Override
  public void updateValue( int index, Object value)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    CustomComboModel model = (CustomComboModel)widget.getModel();
    model.updateElementAt( index);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueWidgetFeature#removeValue(int)
   */
  @Override
  public void removeValue( int index)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.removeItemAt( index);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueWidgetFeature#setValues(java.util.List)
   */
  @Override
  public void setValues( List<? extends Object> list)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.removeAllItems();
    for( Object value: list)
    {
      Item item = new Item( value);
      widget.addItem( item);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueWidgetFeature#getValues()
   */
  @Override
  public List<? extends Object> getValues()
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    List<Object> values = new ArrayList<Object>( widget.getItemCount());
    for( int i=0; i<widget.getItemCount(); i++)
    {
      Item item = (Item)widget.getItemAt( i);
      values.add( item.value);
    }
    return values;
  }

  protected IXidget xidget;
}
