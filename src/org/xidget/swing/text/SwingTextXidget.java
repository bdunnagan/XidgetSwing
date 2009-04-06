/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text;

import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetCreationFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.swing.ISwingWidgetFeature;
import org.xidget.swing.SwingWidgetFeature;
import org.xidget.swing.feature.SwingTooltipErrorFeature;
import org.xidget.swing.text.feature.TextWidgetCreationFeature;
import org.xidget.swing.text.feature.TextWidgetFeature;
import org.xidget.text.TextXidget;
import org.xidget.text.feature.ITextWidgetFeature;

/**
 * An implementation of TextXidget for a JTextField or JTextArea.
 */
public class SwingTextXidget extends TextXidget
{  
  public SwingTextXidget()
  {
    creationFeature = new TextWidgetCreationFeature( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.TextXidget#getErrorFeature()
   */
  @Override
  protected IErrorFeature getErrorFeature()
  {
    ISwingWidgetFeature widgetFeature = getFeature( ISwingWidgetFeature.class);
    return new SwingTooltipErrorFeature( widgetFeature.getWidget());
  }

  /* (non-Javadoc)
   * @see org.xidget.text.TextXidget#getWidgetFeature()
   */
  @Override
  protected IWidgetFeature getWidgetFeature()
  {
    ISwingWidgetFeature widgetFeature = getFeature( ISwingWidgetFeature.class);
    return new SwingWidgetFeature( widgetFeature.getWidget());
  }

  /* (non-Javadoc)
   * @see org.xidget.text.TextXidget#getWidgetTextFeature()
   */
  @Override
  protected ITextWidgetFeature getWidgetTextFeature()
  {
    return new TextWidgetFeature( creationFeature.getTextWidget());
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss.equals( ISwingWidgetFeature.class)) return (T)creationFeature;
    if ( clss.equals( IWidgetCreationFeature.class)) return (T)creationFeature;
    return super.getFeature( clss);
  }

  private TextWidgetCreationFeature creationFeature;
}
