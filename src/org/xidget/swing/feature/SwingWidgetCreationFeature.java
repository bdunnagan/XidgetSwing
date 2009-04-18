/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Container;
import javax.swing.JComponent;
import org.xidget.IXidget;
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
  public void createWidget()
  {
    JComponent component = xidget.getParent().getFeature( JComponent.class);
    createSwingWidget( component);
  }
 
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidget()
  {
    JComponent widget = xidget.getFeature( JComponent.class);
    Container container = widget.getParent();
    container.remove( widget);
    container.invalidate();
  }

  /**
   * Create the new swing widget in the specified container.
   * @param container The container.
   * @return Returns the new swing widget.
   */
  protected abstract JComponent createSwingWidget( Container container);
  
  protected IXidget xidget;
}
