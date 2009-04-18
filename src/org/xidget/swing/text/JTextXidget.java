/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.ComputeNodeFeature;
import org.xidget.feature.text.TextModelFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IErrorFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.text.ITextModelFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;
import org.xidget.swing.feature.SwingWidgetFeature;
import org.xidget.swing.feature.TooltipErrorFeature;
import org.xidget.swing.text.feature.JTextComponentTextWidgetFeature;
import org.xidget.swing.text.feature.JTextComponentWidgetCreationFeature;

/**
 * A text xidget implemented with a Swing JTextComponent widget.
 */
public class JTextXidget extends Xidget
{
  public JTextXidget()
  {
    bindFeature = new BindFeature( this);
    errorFeature = new TooltipErrorFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    textModelFeature = new TextModelFeature( this);
    textWidgetFeature = new JTextComponentTextWidgetFeature( this);
    computeNodeFeature = new ComputeNodeFeature( this);
    creationFeature = new JTextComponentWidgetCreationFeature( this);
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
    
    if ( clss == JComponent.class) return (T)creationFeature.getContainer();
    if ( clss == JTextComponent.class) return (T)creationFeature.getTextWidget();
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private IErrorFeature errorFeature;
  private ITextModelFeature textModelFeature;
  private ITextWidgetFeature textWidgetFeature;
  private IComputeNodeFeature computeNodeFeature;
  private JTextComponentWidgetCreationFeature creationFeature;
}
