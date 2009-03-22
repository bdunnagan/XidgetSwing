/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import java.awt.Container;
import javax.swing.JComponent;
import org.xidget.IXidget;
import org.xidget.feature.IWidgetHierarchyFeature;
import org.xmodel.IModelObject;

/**
 * An abstract implementation of IWidgetHierarchyFeature which performs the common
 * work necessary to relayout the widget hierarchy after a widget is replaced.
 */
public abstract class SwingWidgetHierarchyFeature implements IWidgetHierarchyFeature
{
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetHierarchyFeature#createWidget(org.xidget.IXidget, java.lang.String, org.xmodel.IModelObject)
   */
  public void createWidget( IXidget xidget, String label, IModelObject element)
  {
    JComponent container = xidget.getParent().getFeature( ISwingWidgetFeature.class).getWidget();
    JComponent oldWidget = xidget.getFeature( ISwingWidgetFeature.class).getWidget();
    createSwingWidget( container, label, element);
    if ( oldWidget != null)
    {
      container.remove( oldWidget);
      container.revalidate();
    }
  }
 
  /**
   * Create the new swing widget in the specified container.
   * @param container The container.
   * @param label The xidget label.
   * @param element The configuration element.
   * @return Returns the new swing widget.
   */
  protected abstract JComponent createSwingWidget( Container container, String label, IModelObject element);
}
