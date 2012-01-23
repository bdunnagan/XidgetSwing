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

/**
 * An implementation of MutableComboBoxModel backed by IModelObject elements. 
 */
@SuppressWarnings("serial")
public class CustomComboModel extends AbstractListModel implements MutableComboBoxModel
{
  public CustomComboModel( JComboBox widget)
  {
    this.widget = widget;
    this.items = new ArrayList<Item>();
  }
    
  /* (non-Javadoc)
   * @see javax.swing.MutableComboBoxModel#addElement(java.lang.Object)
   */
  @Override
  public void addElement( Object object)
  {
    items.add( (Item)object);
    int index = items.size() - 1;
    fireIntervalAdded( widget, index, index);
  }

  /* (non-Javadoc)
   * @see javax.swing.MutableComboBoxModel#insertElementAt(java.lang.Object, int)
   */
  @Override
  public void insertElementAt( Object object, int index)
  {
    items.add( index, (Item)object);
    fireIntervalAdded( widget, index, index);
  }

  /**
   * Update the element at the specified index.
   * @param index
   */
  public void updateElementAt( int index)
  {
    fireContentsChanged( widget, index, index);
  }
  
  /* (non-Javadoc)
   * @see javax.swing.MutableComboBoxModel#removeElement(java.lang.Object)
   */
  @Override
  public void removeElement( Object object)
  {
    int index = items.indexOf( (Item)object);
    if ( index >= 0) removeElement( index);
  }

  /* (non-Javadoc)
   * @see javax.swing.MutableComboBoxModel#removeElementAt(int)
   */
  @Override
  public void removeElementAt( int index)
  {
    items.remove( index);
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
    edit = null;
    
    //
    // When the widget is editable, this method can be called with the string entered in the editor.
    //
    if ( object instanceof Item)
    {
      Item item = (Item)object;
      int index = items.indexOf( item);
      if ( index >= 0)
      {
        selected = items.get( index);
        fireContentsChanged( widget, index, index);
      }
      else
      {
        selected = item;
      }
    }
    else
    {
      edit = object;
      
      if ( selected == null)
      {
        // find matching item
        for( int i=0; i<items.size(); i++)
        {
          Item item = items.get( i);
          if ( item.toString().equals( object))
          {
            selected = item;
            fireContentsChanged( widget, i, i);
            break;
          }
        }
  
        // create item
        if ( selected == null)
        {
          selected = new Item( edit);
          addElement( selected);
        }
      }
    }
  }

  /**
   * @return Returns the value of the last edit.
   */
  public Object getEditorValue()
  {
    return edit;
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
  
  private JComboBox widget;
  private List<Item> items;
  private Item selected;
  private Object edit;
}
