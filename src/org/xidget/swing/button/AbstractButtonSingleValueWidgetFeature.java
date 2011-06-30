/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.button;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;

import org.xidget.IXidget;
import org.xidget.ifeature.model.ISingleValueWidgetFeature;

public class AbstractButtonSingleValueWidgetFeature implements ISingleValueWidgetFeature
{
  public AbstractButtonSingleValueWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueWidgetFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    AbstractButton button = xidget.getFeature( AbstractButton.class);
    ButtonModel model = button.getModel();
    return model.isSelected();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueWidgetFeature#setValue(java.lang.Object)
   */
  @Override
  public void setValue( Object value)
  {
    AbstractButton button = xidget.getFeature( AbstractButton.class);
    ButtonModel model = button.getModel();
    if ( value instanceof Boolean)
    {
      model.setSelected( (Boolean)value);
    }
    else if ( value instanceof Number)
    {
      model.setSelected( ((Number)value).doubleValue() != 0d);
    }
    else
    {
      String text = (value != null)? value.toString(): "";
      model.setSelected( text.equals( "true"));
    }
  }

  private IXidget xidget;
}
