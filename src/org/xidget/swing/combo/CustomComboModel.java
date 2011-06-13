/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.combo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.xidget.IXidget;
import org.xidget.ifeature.IValueFeature;
import org.xmodel.IModelObject;
import org.xmodel.ModelListener;

/**
 * An implementation of MutableComboBoxModel backed by IModelObject elements. 
 */
public class CustomComboModel implements MutableComboBoxModel
{
  public CustomComboModel( JComboBox widget, IXidget xidget)
  {
    this.widget = widget;
    this.xidget = xidget;
    items = new ArrayList<Item>();
    listeners = new ArrayList<ListDataListener>( 3);
  }
  
  /**
   * Uninstall model listeners.
   */
  public void uninstall()
  {
    for( Item item: items)
    {
      item.node.removeModelListener( listener);
    }
  }
  
  /**
   * Create an item for the specified node.
   * @param object The node.
   * @return Returns the new item.
   */
  private Item createItem( Object object)
  {
    IValueFeature feature = xidget.getFeature( IValueFeature.class);
    Item item = new Item();
    item.node = (IModelObject)object;
    item.value = feature.toDisplay( item.node.getValue());
    return item;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.MutableComboBoxModel#addElement(java.lang.Object)
   */
  @Override
  public void addElement( Object object)
  {
    Item item = createItem( object);
    item.node.addModelListener( listener);
    items.add( item);

    // notify listeners
    int index = items.size() - 1;
    ListDataEvent event = new ListDataEvent( widget, ListDataEvent.INTERVAL_ADDED, index, index);
    ListDataListener[] array = listeners.toArray( new ListDataListener[ 0]);
    for( ListDataListener listener: array) listener.intervalAdded( event);
  }

  /* (non-Javadoc)
   * @see javax.swing.MutableComboBoxModel#insertElementAt(java.lang.Object, int)
   */
  @Override
  public void insertElementAt( Object object, int index)
  {
    Item item = createItem( object);
    item.node.addModelListener( listener);
    items.add( index, item);
    
    // notify listeners
    ListDataEvent event = new ListDataEvent( widget, ListDataEvent.INTERVAL_ADDED, index, index);
    ListDataListener[] array = listeners.toArray( new ListDataListener[ 0]);
    for( ListDataListener listener: array) listener.intervalAdded( event);
  }

  /* (non-Javadoc)
   * @see javax.swing.MutableComboBoxModel#removeElement(java.lang.Object)
   */
  @Override
  public void removeElement( Object object)
  {
    int index = items.indexOf( (IModelObject)object);
    if ( index >= 0) removeElement( index);
  }

  /* (non-Javadoc)
   * @see javax.swing.MutableComboBoxModel#removeElementAt(int)
   */
  @Override
  public void removeElementAt( int index)
  {
    Item item = items.remove( index);
    item.node.removeModelListener( listener);
    
    // notify listeners
    ListDataEvent event = new ListDataEvent( widget, ListDataEvent.INTERVAL_REMOVED, index, index);
    ListDataListener[] array = listeners.toArray( new ListDataListener[ 0]);
    for( ListDataListener listener: array) listener.intervalRemoved( event);
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
    selected = (Item)object;
  }

  /* (non-Javadoc)
   * @see javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener)
   */
  @Override
  public void addListDataListener( ListDataListener listener)
  {
    listeners.add( listener);
  }

  /* (non-Javadoc)
   * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
   */
  @Override
  public void removeListDataListener( ListDataListener listener)
  {
    listeners.remove( listener);
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

  /**
   * Notify listeners that the value of an item has changed.
   */
  private void updateListeners( int index)
  {
    ListDataEvent event = new ListDataEvent( widget, ListDataEvent.CONTENTS_CHANGED, index, index);
    ListDataListener[] array = listeners.toArray( new ListDataListener[ 0]);
    for( ListDataListener listener: array) listener.contentsChanged( event);
  }
  
  private final ModelListener listener = new ModelListener() {
    public void notifyChange( IModelObject object, String attrName, Object newValue, Object oldValue)
    {
      int index = items.indexOf( object);
      if ( index >= 0) updateListeners( index);
    }
    public void notifyClear( IModelObject object, String attrName, Object oldValue)
    {
      int index = items.indexOf( object);
      if ( index >= 0) updateListeners( index);
    }
  };
  
  private final static class Item
  {
    public IModelObject node;
    public Object value;

    public boolean equals( Object object)
    {
      return object == node;
    }
    
    public String toString()
    {
      return (value != null)? value.toString(): ""; 
    }
  }
  
  private JComboBox widget;
  private IXidget xidget;
  private List<Item> items;
  private Item selected;
  private List<ListDataListener> listeners;
}
