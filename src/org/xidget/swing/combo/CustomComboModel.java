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
import org.xidget.ifeature.IBindFeature;
import org.xmodel.IModelObject;
import org.xmodel.ModelListener;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of MutableComboBoxModel backed by IModelObject elements. 
 */
@SuppressWarnings("serial")
public class CustomComboModel extends AbstractListModel implements MutableComboBoxModel
{
  public CustomComboModel( IXidget xidget)
  {
    this.xidget = xidget;
    this.display = Xlate.childGet( xidget.getConfig(), "display", (IExpression)null);
    this.items = new ArrayList<Item>();
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
    Item item = new Item();
    if ( object instanceof IModelObject) 
    {
      item.node = (IModelObject)object;
      item.value = item.node.getValue();
    
      if ( display != null)
      {
        IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
        StatefulContext parent = bindFeature.getBoundContext();
        StatefulContext context = new StatefulContext( parent, item.node); 
        item.value = display.evaluateString( context);
      }
    }
    else
    {
      item.value = object;
    }
    
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
    Item item = createItem( object);
    item.node.addModelListener( listener);
    items.add( index, item);
    
    JComboBox widget = xidget.getFeature( JComboBox.class);
    fireIntervalAdded( widget, index, index);
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
    if ( !(object instanceof Item))
    {
      Item item = createItem( object);
      object = item;
    }
    
    selected = object;
    
    int index = items.indexOf( object);
    if ( index < 0) return;
    
    JComboBox widget = xidget.getFeature( JComboBox.class);
    fireContentsChanged( widget, index, index);
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
    JComboBox widget = xidget.getFeature( JComboBox.class);
    fireContentsChanged( widget, index, index);
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
  
  public final static class Item
  {
    public IModelObject node;
    public Object value;

    public boolean equals( Object object)
    {
      if ( object instanceof IModelObject)
      {
        return object.equals( node);
      }
     
      if ( object instanceof Item) 
      {
        Item item = (Item)object;
        if ( item.node == null || node == null)
        {
          if ( item.value == null || value == null) return item == value;
          return item.value.equals( value);
        }
        return item.node.equals( node);
      }
      
      return super.equals( object);
    }
    
    public String toString()
    {
      return (value != null)? value.toString(): ""; 
    }
  }
  
  private IXidget xidget;
  private List<Item> items;
  private IExpression display;
  private Object selected;
}
