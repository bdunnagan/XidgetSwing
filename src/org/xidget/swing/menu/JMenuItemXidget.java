/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.menu;

import java.awt.Component;
import java.awt.Container;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.button.ButtonModelFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IIconFeature;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.ISourceFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.button.IButtonModelFeature;
import org.xidget.ifeature.button.IButtonWidgetFeature;
import org.xidget.swing.feature.AbstractButtonIconFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.GenericContainerFeature;
import org.xidget.swing.feature.SwingWidgetFeature;
import org.xidget.swing.feature.menu.JMenuItemButtonWidgetFeature;
import org.xidget.swing.feature.menu.JMenuItemWidgetCreationFeature;

/**
 * A xidget implementation for Swing AbstactButton widgets.
 */
public class JMenuItemXidget extends Xidget
{
  /* (non-Javadoc)
   * @see org.xidget.Xidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    bindFeature = new BindFeature( this);
    iconFeature = new AbstractButtonIconFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    containerFeature = new GenericContainerFeature( this);
    buttonWidgetFeature = new JMenuItemButtonWidgetFeature( this);
    buttonModelFeature = new ButtonModelFeature();
    creationFeature = new JMenuItemWidgetCreationFeature( this);
    basicFeatureSet = new BasicFeatureSet( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IIconFeature.class) return (T)iconFeature;
    if ( clss == ILabelFeature.class) return (T)creationFeature;
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == ISourceFeature.class) return (T)buttonModelFeature;
    if ( clss == IButtonWidgetFeature.class) return (T)buttonWidgetFeature;
    if ( clss == IButtonModelFeature.class) return (T)buttonModelFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IWidgetContainerFeature.class) return (T)containerFeature;
    if ( clss == IBindFeature.class) return (T)bindFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getJMenuItem();
    if ( clss == JComponent.class) return (T)creationFeature.getJMenuItem();
    if ( clss == JMenuItem.class) return (T)creationFeature.getJMenuItem();
    if ( clss == Container.class) return (T)creationFeature.getJMenuItem();
    if ( clss == AbstractButton.class) return (T)creationFeature.getJMenuItem();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }

  private IBindFeature bindFeature;
  private IIconFeature iconFeature;
  private IWidgetFeature widgetFeature;
  private IWidgetContainerFeature containerFeature;
  private IButtonWidgetFeature buttonWidgetFeature;
  private IButtonModelFeature buttonModelFeature;
  private JMenuItemWidgetCreationFeature creationFeature;  
  private IFeatured basicFeatureSet;
}
