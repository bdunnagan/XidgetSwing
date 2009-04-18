/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text.feature;

import javax.swing.JComboBox;
import org.xidget.config.util.TextTransform;
import org.xidget.text.feature.TextModelFeature;
import org.xidget.text.ifeature.ITextWidgetFeature;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of IWidgetTextAdapter for a JComboBox widget 
 * which supports the <i>all</i> channel.
 */
public class JComboBoxWidgetFeature implements ITextWidgetFeature
{
  public JComboBoxWidgetFeature( JComboBox widget)
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
      widget.setSelectedItem( text);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IWidgetTextAdapter#setTransform(java.lang.String, org.xmodel.xpath.expression.IExpression)
   */
  public void setTransform( String channel, IExpression expression)
  {
    this.transform = new TextTransform( expression);
  }
  
  private JComboBox widget;
  private TextTransform transform;
}
