/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.combo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.MutableComboBoxModel;

import org.xidget.IXidget;

/**
 * An implementation of MutableComboBoxModel backed by IModelObject elements. 
 */
@SuppressWarnings("serial")
public class CustomComboModel extends AbstractListModel implements MutableComboBoxModel
{
  public CustomComboModel( IXidget xidget)
  {
    this.xidget = xidget;
    this.items = new ArrayList<Object>();
  }
    
  /* (non-Javadoc)
   * @see javax.swing.MutableComboBoxModel#addElement(java.lang.Object)
   */
  @Override
  public void addElement( Object object)
  {
    items.add( object);

    JComboBox widget = xidget.getFeature( JComboBox.class);
    int index = items.size() - 1;
    fireIntervalAdded( widget, index, index);
  }

  /* (non-Javadoc)
   * @see javax.swing.MutableComboBoxModel#insertElementAt(java.lang.Object, int)
   */
  @Override
  public void insertElementAt( Object object, int index)
  {
    items.add( index, object);
    
    JComboBox widget = xidget.getFeature( JComboBox.class);
    fireIntervalAdded( widget, index, index);
  }

  /**
   * Update the element at the specified index.
   * @param index
   */
  public void updateElementAt( int index)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    fireContentsChanged( widget, index, index);
  }
  
  /* (non-Javadoc)
   * @see javax.swing.MutableComboBoxModel#removeElement(java.lang.Object)
   */
  @Override
  public void removeElement( Object object)
  {
    int index = items.indexOf( object);
    if ( index >= 0) removeElement( index);
  }

  /* (non-Javadoc)
   * @see javax.swing.MutableComboBoxModel#removeElementAt(int)
   */
  @Override
  public void removeElementAt( int index)
  {
    items.remove( index);
    
    JComboBox widget = xidget.getFeature( JComboBox.class);
    fireIntervalRemoved( widget, index, index);
  }

  /* (non-Javadoc)
   * @see javax.swing.ComboBoxModel#getSelectedItem()
   */
  @Override
  public Object getSelectedItem()
  {
    return selected;
  }

  /* (non-Javadoc)
   * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
   */
  @Override
  public void setSelectedItem( Object object)
  {
    int index = items.indexOf( object);
    if ( index >= 0)
    {
      selected = items.get( index);
      JComboBox widget = xidget.getFeature( JComboBox.class);
      fireContentsChanged( widget, index, index);
    }
    else
    {
      selected = object;
    }
  }

  /* (non-Javadoc)
   * @see javax.swing.ListModel#getElementAt(int)
   */
  @Override
  public Object getElementAt( int index)
  {
    return items.get( index);
  }

  /* (non-Javadoc)
   * @see javax.swing.ListModel#getSize()
   */
  @Override
  public int getSize()
  {
    return items.size();
  }
  
  private IXidget xidget;
  private List<Object> items;
  private Object selected;
}
