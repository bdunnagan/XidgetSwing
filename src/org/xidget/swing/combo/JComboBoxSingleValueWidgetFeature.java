/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JComboBoxTextWidgetFeature.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.swing.combo;

import javax.swing.JComboBox;
import org.xidget.IXidget;
import org.xidget.ifeature.model.ISingleValueWidgetFeature;
import org.xidget.swing.combo.CustomComboModel.Item;

/**
 * An implementation of ISingleValueWidgetFeature for JComboBox.
 */
public class JComboBoxSingleValueWidgetFeature implements ISingleValueWidgetFeature
{
  public JComboBoxSingleValueWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueWidgetFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    Object selected = widget.getSelectedItem();
    if ( selected instanceof Item)
    {
      Item item = (Item)selected;
      return (item != null)? item.getContent(): null;
    }
    return selected;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueWidgetFeature#setValue(java.lang.Object)
   */
  @Override
  public void setValue( Object value)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.setSelectedItem( value);
  }

  private IXidget xidget;
}
