/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Container;
import javax.swing.JComponent;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.swing.ifeature.ISwingWidgetFeature;

/**
 * An abstract implementation of IWidgetCreationFeature which performs the common
 * work necessary to relayout the widget hierarchy after a widget is replaced.
 */
public abstract class SwingCreationFeature implements IWidgetCreationFeature
{
  protected SwingCreationFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#createWidget()
   */
  public void createWidget()
  {
    JComponent container = xidget.getParent().getFeature( ISwingWidgetFeature.class).getWidget();
    createSwingWidget( container);
  }
 
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidget()
  {
    JComponent widget = xidget.getFeature( ISwingWidgetFeature.class).getWidget();
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
