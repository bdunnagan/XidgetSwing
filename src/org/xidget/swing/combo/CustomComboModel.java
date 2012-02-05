/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.combo;

import javax.swing.DefaultComboBoxModel;
import org.xmodel.IModelObject;

/**
 * A custom model that return the result of <code>getValue()</code> on nodes instead of <code>toString</code>.
 */
@SuppressWarnings("serial")
public class CustomComboModel extends DefaultComboBoxModel
{
  /* (non-Javadoc)
   * @see javax.swing.DefaultComboBoxModel#addElement(java.lang.Object)
   */
  @Override
  public void addElement( Object value)
  {
    super.addElement( new Item( value));
  }

  /* (non-Javadoc)
   * @see javax.swing.DefaultComboBoxModel#insertElementAt(java.lang.Object, int)
   */
  @Override
  public void insertElementAt( Object value, int index)
  {
    super.insertElementAt( new Item( value), index);
  }

  protected final class Item
  {
    public Item( Object content)
    {
      this.content = content;
    }
    
    /**
     * @return Returns the content.
     */
    public Object getContent()
    {
      return content;
    }
    
    /**
     * @return Returns the value of the content.
     */
    public Object getValue()
    {
      if ( content instanceof IModelObject)
      {
        return ((IModelObject)content).getValue();
      }
      return content;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object object)
    {
      if ( object == null) return false;
      
      if ( object instanceof Item)
      {
        Item item = (Item)object;
        return item.getValue().equals( getValue());
      }
      
      Object value = getValue();
      if ( value != null && object != null) return value.equals( object);
      
      return value == object;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
      Object value = getValue();
      if ( value == null) return 0;
      return value.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      if ( content instanceof IModelObject) 
      {
        IModelObject node = (IModelObject)content;
        Object value = node.getValue();
        return (value != null)? value.toString(): "";
      }
      return content.toString();
    }
    
    private Object content;
  }
}
