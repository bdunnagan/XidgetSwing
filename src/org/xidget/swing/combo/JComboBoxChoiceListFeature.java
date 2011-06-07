/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JComboBoxChoiceListFeature.java
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

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import org.xidget.IXidget;
import org.xidget.ifeature.combo.IChoiceListFeature;

/**
 * An implementation of IChoiceListFeature which is backed by a JComboBox.
 */
public class JComboBoxChoiceListFeature implements IChoiceListFeature
{
  public JComboBoxChoiceListFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#getChoices()
   */
  public List<Object> getChoices()
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    int count = widget.getItemCount();
    List<Object> result = new ArrayList<Object>( count);
    for( int i=0; i<count; i++) result.add( widget.getItemAt( i).toString());
    return result;
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#addChoice(java.lang.Object)
   */
  public void addChoice( Object choice)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.addItem( choice);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#insertChoice(int, java.lang.Object)
   */
  public void insertChoice( int index, Object choice)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.insertItemAt( choice, index);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeAllChoices()
   */
  public void removeAllChoices()
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.removeAllItems();
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeChoice(int)
   */
  public void removeChoice( int index)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.removeItemAt( index);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeChoice(java.lang.Object)
   */
  public void removeChoice( Object choice)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.removeItem( choice);
  }

  private IXidget xidget;
}
