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
import org.xidget.config.util.TextTransform;
import org.xidget.feature.text.TextModelFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IWidgetTextAdapter for a JTextField or JTextArea widget which
 * supports both the <i>all</i> and the <i>selected</i> channels. It does not support
 * a transform for the <i>selected</i> channel.
 */
public class JTextComponentTextWidgetFeature implements ITextWidgetFeature
{
  public JTextComponentTextWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
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
  public void setText( StatefulContext context, String channel, String text)
  {
    JTextComponent widget = xidget.getFeature( JTextComponent.class);
    if ( channel.equals( TextModelFeature.allChannel))
    {
      if ( transform != null) text = transform.transform( context, text);
      if ( !widget.getText().equals( text)) widget.setText( text);
    }
    else if ( channel.equals( TextModelFeature.selectedChannel))
    {
      widget.replaceSelection( text);
      String allText = transform.transform( context, widget.getText());
      if ( !widget.getText().equals( allText)) widget.setText( allText);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IWidgetTextAdapter#setTransform(java.lang.String, org.xmodel.xpath.expression.IExpression)
   */
  public void setTransform( String channel, IExpression expression)
  {
    this.transform = new TextTransform( expression);
  }

  private IXidget xidget;
  private TextTransform transform;
}
