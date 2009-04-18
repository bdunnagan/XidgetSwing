/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.ComputeNodeFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IErrorFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.swing.feature.SwingWidgetFeature;
import org.xidget.swing.feature.TooltipErrorFeature;
import org.xidget.swing.text.feature.JComboBoxChoiceListFeature;
import org.xidget.swing.text.feature.JComboBoxTextWidgetFeature;
import org.xidget.swing.text.feature.JComboBoxWidgetCreationFeature;
import org.xidget.text.feature.TextModelFeature;
import org.xidget.text.ifeature.IChoiceListFeature;
import org.xidget.text.ifeature.ITextModelFeature;
import org.xidget.text.ifeature.ITextWidgetFeature;

/**
 * @author bdunnagan
 *
 */
public class JComboBoxXidget extends Xidget
{
  public JComboBoxXidget()
  {
    bindFeature = new BindFeature( this);
    errorFeature = new TooltipErrorFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    textModelFeature = new TextModelFeature( this);
    textWidgetFeature = new JComboBoxTextWidgetFeature( this);
    choiceListFeature = new JComboBoxChoiceListFeature( this);
    computeNodeFeature = new ComputeNodeFeature( this);
    creationFeature = new JComboBoxWidgetCreationFeature( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == IErrorFeature.class) return (T)errorFeature;
    if ( clss == ITextModelFeature.class) return (T)textModelFeature;
    if ( clss == ITextWidgetFeature.class) return (T)textWidgetFeature;
    if ( clss == IComputeNodeFeature.class) return (T)computeNodeFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IBindFeature.class) return (T)bindFeature;
    if ( clss == IChoiceListFeature.class) return (T)choiceListFeature;
    
    if ( clss == JComponent.class) return (T)creationFeature.getComboBox();
    if ( clss == JComboBox.class) return (T)creationFeature.getComboBox();
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private IErrorFeature errorFeature;
  private ITextModelFeature textModelFeature;
  private ITextWidgetFeature textWidgetFeature;
  private IComputeNodeFeature computeNodeFeature;
  private JComboBoxChoiceListFeature choiceListFeature;
  private JComboBoxWidgetCreationFeature creationFeature;
}
