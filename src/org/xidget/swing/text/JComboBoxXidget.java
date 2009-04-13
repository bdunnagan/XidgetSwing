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
import org.xidget.swing.text.feature.JComboBoxChoiceListFeature;
import org.xidget.swing.text.feature.JComboBoxWidgetCreationFeature;
import org.xidget.swing.text.feature.JComboBoxWidgetFeature;
import org.xidget.text.feature.IChoiceListFeature;
import org.xidget.text.feature.ITextWidgetFeature;

/**
 * An implementation of TextXidget for a JComboBox.
 */
public class JComboBoxXidget extends JTextComponentXidget
{
  /* (non-Javadoc)
   * @see org.xidget.swing.text.SwingTextXidget#getWidgetCreationFeature()
   */
  @Override
  protected IWidgetCreationFeature getWidgetCreationFeature()
  {
    creationFeature = new JComboBoxWidgetCreationFeature( this);
    return creationFeature;
  }

  /* (non-Javadoc)
   * @see org.xidget.text.TextXidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    super.createFeatures();
    choiceListFeature = new JComboBoxChoiceListFeature( creationFeature.getComboBox());
  }

  /* (non-Javadoc)
   * @see org.xidget.text.TextXidget#getErrorFeature()
   */
  @Override
  protected IErrorFeature getErrorFeature()
  {
    return new SwingTooltipErrorFeature( this);
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
  protected ITextWidgetFeature getTextWidgetFeature()
  {
    return new JComboBoxWidgetFeature( creationFeature.getComboBox());
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss.equals( ISwingWidgetFeature.class)) return (T)creationFeature;
    if ( clss.equals( IWidgetCreationFeature.class)) return (T)creationFeature;
    if ( clss.equals( IChoiceListFeature.class)) return (T)choiceListFeature; 
    return super.getFeature( clss);
  }

  private JComboBoxWidgetCreationFeature creationFeature;
  private JComboBoxChoiceListFeature choiceListFeature;
}
