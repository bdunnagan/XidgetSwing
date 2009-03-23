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
import org.xidget.swing.text.feature.ComboChoiceListFeature;
import org.xidget.swing.text.feature.ComboFeature;
import org.xidget.swing.text.feature.ComboWidgetCreationFeature;
import org.xidget.text.feature.IChoiceListFeature;
import org.xidget.text.feature.IWidgetTextFeature;

/**
 * An implementation of TextXidget for a JComboBox.
 */
public class SwingComboXidget extends SwingTextXidget
{
  public SwingComboXidget()
  {
    creationFeature = new ComboWidgetCreationFeature( this);
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
  protected IWidgetTextFeature getWidgetTextFeature()
  {
    return new ComboFeature( creationFeature.getComboBox());
  }
  
  /**
   * Returns the IChoiceListFeature.
   * @return Returns the IChoiceListFeature.
   */
  private IChoiceListFeature getChoiceListFeature()
  {
    if ( choiceListFeature == null) choiceListFeature = new ComboChoiceListFeature( creationFeature.getComboBox());
    return choiceListFeature;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss.equals( ISwingWidgetFeature.class)) return (T)creationFeature;
    if ( clss.equals( IWidgetCreationFeature.class)) return (T)creationFeature;
    if ( clss.equals( IChoiceListFeature.class)) return (T)getChoiceListFeature(); 
    return super.getFeature( clss);
  }

  private ComboWidgetCreationFeature creationFeature;
  private ComboChoiceListFeature choiceListFeature;
}
