/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;

import org.xidget.IXidget;
import org.xidget.swing.feature.SwingTextWidgetFeature;

public class JTextComponentTextWidgetFeature extends SwingTextWidgetFeature
{
  public JTextComponentTextWidgetFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setEditable(boolean)
   */
  @Override
  public void setEditable( boolean editable)
  {
    JTextComponent widget = xidget.getFeature( JTextComponent.class);
    widget.setEditable( editable);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setHAlign(org.xidget.ifeature.ITextWidgetFeature.HAlign)
   */
  @Override
  public void setHAlign( HAlign alignment)
  {
    JTextComponent widget = xidget.getFeature( JTextComponent.class);
    if ( widget instanceof JTextField)
    {
      int value = 0;
      switch( alignment)
      {
        case left:   value = SwingConstants.LEFT; break;
        case center: value = SwingConstants.CENTER; break;
        case right:  value = SwingConstants.RIGHT; break;
      }
      
      ((JTextField)widget).setHorizontalAlignment( value);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setVAlign(org.xidget.ifeature.ITextWidgetFeature.VAlign)
   */
  @Override
  public void setVAlign( VAlign alignment)
  {
  }
}
