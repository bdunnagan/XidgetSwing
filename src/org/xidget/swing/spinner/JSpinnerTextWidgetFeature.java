/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.spinner;

import javax.swing.JSpinner;

import org.xidget.IXidget;
import org.xidget.config.util.TextTransform;
import org.xidget.feature.text.TextModelFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITextWidgetFeature for the JSpinnerXidget.
 */
public class JSpinnerTextWidgetFeature implements ITextWidgetFeature 
{
  public JSpinnerTextWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.text.ITextWidgetFeature#setEditable(boolean)
   */
  @Override
  public void setEditable( boolean editable) 
  {
    if ( editable)
    {
      String type = Xlate.get( xidget.getConfig(), "type", "numeric");
      JSpinner widget = xidget.getFeature( JSpinner.class);
      if ( type.equals( "numeric"))
      {
        widget.setEditor( new JSpinner.NumberEditor( widget));
      }
    }
    else
    {
      JSpinner widget = xidget.getFeature( JSpinner.class);
      JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor)widget.getEditor();
      widget.removeChangeListener( editor);
      widget.setEditor( null);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.text.ITextWidgetFeature#setText(org.xmodel.xpath.expression.StatefulContext, java.lang.String, java.lang.String)
   */
  @Override
  public void setText( StatefulContext context, String channel, String text) 
  {
    JSpinner widget = xidget.getFeature( JSpinner.class);
    if ( channel.equals( TextModelFeature.allChannel))
    {
      if ( transform != null) text = transform.transform( context, text);
      
      Object value = widget.getValue();
      if ( value instanceof Number)
      {
        try 
        { 
          Double newValue = Double.parseDouble( text);
          if ( newValue != (Number)value) widget.setValue( newValue);
        }
        catch( Exception e)
        {
        }
      }
      else
      {
        if ( value == null || !text.equals( value.toString())) widget.setValue( text);
      }
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
