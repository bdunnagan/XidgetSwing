/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text;

import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.text.TextModelFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.ISourceFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.text.ITextModelFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingWidgetFeature;

/**
 * A text xidget implemented with a Swing JTextComponent widget.
 */
public class JTextXidget extends Xidget
{
  public void createFeatures()
  {
    bindFeature = new BindFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    textModelFeature = new TextModelFeature( this);
    textWidgetFeature = new JTextComponentTextWidgetFeature( this);
    labelFeature = new JTextComponentLabelFeature( this);
    creationFeature = new JTextComponentWidgetCreationFeature( this);
    basicFeatureSet = new BasicFeatureSet( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == ISourceFeature.class) return (T)textModelFeature;
    if ( clss == ITextModelFeature.class) return (T)textModelFeature;
    if ( clss == ITextWidgetFeature.class) return (T)textWidgetFeature;
    if ( clss == ILabelFeature.class) return (T)labelFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IBindFeature.class) return (T)bindFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getContainer();
    if ( clss == JComponent.class) return (T)creationFeature.getContainer();
    if ( clss == JTextComponent.class) return (T)creationFeature.getTextWidget();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private ITextModelFeature textModelFeature;
  private ITextWidgetFeature textWidgetFeature;
  private JTextComponentLabelFeature labelFeature;
  private JTextComponentWidgetCreationFeature creationFeature;
  private IFeatured basicFeatureSet;
}
