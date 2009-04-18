/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text.feature;

import javax.swing.text.JTextComponent;
import org.xidget.config.util.TextTransform;
import org.xidget.text.feature.TextModelFeature;
import org.xidget.text.ifeature.ITextWidgetFeature;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of IWidgetTextAdapter for a JTextField or JTextArea widget which
 * supports both the <i>all</i> and the <i>selected</i> channels. It does not support
 * a transform for the <i>selected</i> channel.
 */
public class JTextComponentWidgetFeature implements ITextWidgetFeature
{
  public JTextComponentWidgetFeature( JTextComponent widget)
  {
    this.widget = widget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IWidgetTextAdapter#setEditable(boolean)
   */
  public void setEditable( boolean editable)
  {
    widget.setEditable( editable);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IWidgetTextAdapter#setText(java.lang.String, java.lang.String)
   */
  public void setText( String channel, String text)
  {
    if ( channel.equals( TextModelFeature.allChannel))
    {
      if ( transform != null) text = transform.transform( text);
      widget.setText( text);
    }
    else if ( channel.equals( TextModelFeature.selectedChannel))
    {
      widget.replaceSelection( text);
      String allText = widget.getText();
      widget.setText( transform.transform( allText));
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IWidgetTextAdapter#setTransform(java.lang.String, org.xmodel.xpath.expression.IExpression)
   */
  public void setTransform( String channel, IExpression expression)
  {
    this.transform = new TextTransform( expression);
  }
  
  private JTextComponent widget;
  private TextTransform transform;
}
