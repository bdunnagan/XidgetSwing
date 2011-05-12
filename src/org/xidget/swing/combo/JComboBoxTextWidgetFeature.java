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
import org.xidget.ifeature.text.ITextWidgetFeature;

/**
 * An implementation of IWidgetTextAdapter for a JComboBox widget 
 * which supports the <i>all</i> channel.
 */
public class JComboBoxTextWidgetFeature implements ITextWidgetFeature
{
  public JComboBoxTextWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
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
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.setSelectedItem( text);
    this.text = text;
  }

  /**
   * Returns the current text. This method is provided because JComboBox does not store the
   * selected item if it is not a valid choice when the widget is non-editable.
   * @return Returns the current text.
   */
  public String getText()
  {
    return text;
  }
  
  private IXidget xidget;
  private String text;
}
