/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.combo;

import org.xmodel.IModelObject;
import org.xmodel.Xlate;

class Item
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