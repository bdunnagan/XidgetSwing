/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JTextComponentTextWidgetFeature.java
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
package org.xidget.swing.text;

import javax.swing.text.JTextComponent;

import org.xidget.IXidget;
import org.xidget.ifeature.model.ISingleValueWidgetFeature;

public class JTextComponentSingleValueWidgetFeature implements ISingleValueWidgetFeature
{
  public JTextComponentSingleValueWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueWidgetFeature#setValue(java.lang.Object)
   */
  @Override
  public void setValue( Object value)
  {
    String text = (value != null)? value.toString(): "";
    JTextComponent widget = xidget.getFeature( JTextComponent.class);
    if ( !widget.getText().equals( text)) widget.setText( text);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueWidgetFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    JTextComponent jText = xidget.getFeature( JTextComponent.class);
    return jText.getText();
  }
  
  private IXidget xidget;
}
