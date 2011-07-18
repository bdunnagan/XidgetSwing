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
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

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
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.insertItemAt( new Item( value), index);
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
      widget.addItem( new Item( value));
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueWidgetFeature#getValues()
   */
  @Override
  public List<? extends Object> getValues()
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    int count = widget.getItemCount();
    
    List<Object> values = new ArrayList<Object>( count);
    for( int i=0; i<count; i++)
    {
      Item item = (Item)widget.getItemAt( i);
      values.add( item.value);
    }
    
    return values;
  }

  static class Item
  {
    public Item( Object value)
    {
      this.value = value;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object object)
    {
      if ( object == null) return false;
      
      if ( object instanceof IModelObject)
      {
        String other = Xlate.get( (IModelObject)value, "");
        return other.equals( toString());
      }
      else
      {
        return object.toString().equals( toString());
      }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      if ( value instanceof IModelObject) return Xlate.get( (IModelObject)value, "");
      return value.toString();
    }
    
    public Object value;
  }
  
  protected IXidget xidget;
}
