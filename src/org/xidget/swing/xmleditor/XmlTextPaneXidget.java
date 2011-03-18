/**
 * Xidget - WYSIWYG Xidget Builder
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */

package org.xidget.swing.xmleditor;

import java.awt.Component;
import javax.swing.JComponent;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ISourceFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingWidgetFeature;
import org.xidget.swing.xmleditor.XmlTextPane;

/**
 * An implementation of IXidget for the net.boplicity.xmleditor.
 */
public class XmlTextPaneXidget extends Xidget
{
  public void createFeatures()
  {
    bindFeature = new BindFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    sourceFeature = new XmlTextPaneSourceFeature( this);
    creationFeature = new XmlTextPaneWidgetCreationFeature( this);
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
    if ( clss == ISourceFeature.class) return (T)sourceFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IBindFeature.class) return (T)bindFeature;
    
    if ( clss == Component.class) return (T)creationFeature.getJScrollPane();
    if ( clss == JComponent.class) return (T)creationFeature.getJScrollPane();
    if ( clss == XmlTextPane.class) return (T)creationFeature.getXmlTextPane();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }
  
  private IBindFeature bindFeature;
  private IWidgetFeature widgetFeature;
  private ISourceFeature sourceFeature;
  private XmlTextPaneWidgetCreationFeature creationFeature;
  private IFeatured basicFeatureSet;
}
