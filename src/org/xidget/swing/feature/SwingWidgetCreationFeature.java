/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Container;
import javax.swing.JComponent;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;

/**
 * An abstract implementation of IWidgetCreationFeature which performs the common
 * work necessary to relayout the widget hierarchy after a widget is replaced.
 */
public abstract class SwingWidgetCreationFeature implements IWidgetCreationFeature
{
  protected SwingWidgetCreationFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#createWidget()
   */
  public void createWidgets()
  {
    // create widget(s)
    createSwingWidget();
    
    // attach to parent
    IWidgetContainerFeature containerFeature = xidget.getParent().getFeature( IWidgetContainerFeature.class);
    if ( containerFeature != null) containerFeature.addWidget( xidget);    
  }
 
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidgets()
  {
    JComponent widget = xidget.getFeature( JComponent.class);
    Container container = widget.getParent();
    container.remove( widget);
    container.invalidate();
  }

  /**
   * Create the new swing widget.
   * @return Returns the new swing widget.
   */
  protected abstract JComponent createSwingWidget();
  
  protected IXidget xidget;
}
