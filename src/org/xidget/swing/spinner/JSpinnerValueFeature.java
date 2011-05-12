/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.spinner;

import javax.swing.JSpinner;

import org.xidget.IXidget;
import org.xidget.feature.AbstractValueFeature;

/**
 * An implementation of IValueFeature for a Swing text component.
 */
public class JSpinnerValueFeature extends AbstractValueFeature
{
  public JSpinnerValueFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.AbstractValueFeature#setValue(java.lang.Object)
   */
  @Override
  protected void setValue( Object value)
  {
    JSpinner widget = xidget.getFeature( JSpinner.class);
    widget.setValue(  value);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IValueFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    JSpinner widget = xidget.getFeature( JSpinner.class);
    return widget.getValue();
  }
}
