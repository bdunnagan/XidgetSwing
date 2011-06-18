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
import org.xidget.feature.AbstractValueFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;
import org.xidget.swing.combo.CustomComboModel.Item;

/**
 * An implementation of IWidgetTextAdapter for a JComboBox widget 
 * which supports the <i>all</i> channel.
 */
public class JComboBoxTextWidgetFeature extends AbstractValueFeature implements ITextWidgetFeature
{
  public JComboBoxTextWidgetFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IWidgetTextAdapter#setEditable(boolean)
   */
  public void setEditable( boolean editable)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.setEditable( editable);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IWidgetTextAdapter#setText(java.lang.String, java.lang.String)
   */
  public void setText( String text)
  {
    setValue( text);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.AbstractValueFeature#setValue(java.lang.Object)
   */
  @Override
  protected void setValue( Object value)
  {
    JComboBox jCombo = xidget.getFeature( JComboBox.class);
    CustomComboModel model = (CustomComboModel)jCombo.getModel();
    model.setSelectedItem( value);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IValueFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    JComboBox jCombo = xidget.getFeature( JComboBox.class);
    CustomComboModel model = (CustomComboModel)jCombo.getModel();
    Object selected = model.getSelectedItem();
    if ( selected instanceof Item)
    {
      return ((Item)selected).value;
    }
    return selected;
  }
}
