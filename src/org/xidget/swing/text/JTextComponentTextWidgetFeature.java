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
import org.xidget.feature.AbstractValueFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;

/**
 * An implementation of IWidgetTextAdapter for a JTextField or JTextArea widget which
 * supports both the <i>all</i> and the <i>selected</i> channels. It does not support
 * a transform for the <i>selected</i> channel.
 */
public class JTextComponentTextWidgetFeature extends AbstractValueFeature implements ITextWidgetFeature
{
  public JTextComponentTextWidgetFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IWidgetTextAdapter#setEditable(boolean)
   */
  public void setEditable( boolean editable)
  {
    JTextComponent widget = xidget.getFeature( JTextComponent.class);
    widget.setEditable( editable);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IWidgetTextAdapter#setText(java.lang.String, java.lang.String)
   */
  public void setText( String text)
  {
    JTextComponent widget = xidget.getFeature( JTextComponent.class);
    if ( !widget.getText().equals( text)) widget.setText( text);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.AbstractValueFeature#setValue(java.lang.Object)
   */
  @Override
  protected void setValue( Object value)
  {
    setText( (value != null)? value.toString(): "");
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IValueFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    JTextComponent jText = xidget.getFeature( JTextComponent.class);
    return jText.getText();
  }
}
