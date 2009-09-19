/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.dialog;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JDialog;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ITitleFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.dialog.IDialogFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.GenericContainerFeature;
import org.xidget.swing.feature.SwingWidgetFeature;

/**
 * An implementation of IXidget for a Swing JDialog.
 */
public class JDialogXidget extends Xidget
{
  public void createFeatures()
  {
    bindFeature = new BindFeature( this);
    dialogFeature = new JDialogFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    titleFeature = new JDialogTitleFeature( this);
    creationFeature = new JDialogWidgetCreationFeature( this);
    containerFeature = new GenericContainerFeature( this);
    basicFeatureSet = new BasicFeatureSet( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IBindFeature.class) return (T)bindFeature;
    if ( clss == IDialogFeature.class) return (T)dialogFeature;
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == ITitleFeature.class) return (T)titleFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IWidgetContainerFeature.class) return (T)containerFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getJDialog();
    if ( clss == JComponent.class) return (T)creationFeature.getJDialog();
    if ( clss == Container.class) return (T)creationFeature.getJDialog();
    if ( clss == JDialog.class) return (T)creationFeature.getJDialog();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IDialogFeature dialogFeature;
  private IWidgetFeature widgetFeature;
  private ITitleFeature titleFeature;
  private JDialogWidgetCreationFeature creationFeature;
  private IWidgetContainerFeature containerFeature;
  private IFeatured basicFeatureSet;
}
