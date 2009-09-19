/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.combo;

import javax.swing.JComboBox;
import org.xidget.IXidget;
import org.xidget.config.util.TextTransform;
import org.xidget.feature.text.TextModelFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;
import org.xmodel.xpath.expression.IExpression;

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
  public void setText( String channel, String text)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
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

  private IXidget xidget;
  private TextTransform transform;
}
