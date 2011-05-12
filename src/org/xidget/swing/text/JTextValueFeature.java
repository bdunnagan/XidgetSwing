/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text;

import javax.swing.text.JTextComponent;

import org.xidget.IXidget;
import org.xidget.feature.AbstractValueFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;

/**
 * An implementation of IValueFeature for a Swing text component.
 */
public class JTextValueFeature extends AbstractValueFeature
{
  public JTextValueFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.AbstractValueFeature#setValue(java.lang.Object)
   */
  @Override
  protected void setValue( Object value)
  {
    ITextWidgetFeature feature = xidget.getFeature( ITextWidgetFeature.class);
    feature.setText( value.toString());
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
