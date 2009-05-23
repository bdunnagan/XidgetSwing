/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Component;
import javax.swing.JTabbedPane;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;

/**
 * An implementation of IWidgetContainerFeature for use with JTabbedPane containers.
 */
public class JTabbedPaneContainerFeature implements IWidgetContainerFeature
{
  public JTabbedPaneContainerFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#addWidget(org.xidget.IXidget)
   */
  public void addWidget( IXidget child)
  {
    JTabbedPane container = xidget.getFeature( JTabbedPane.class);
    if ( container != null)
    {
      IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
      Object[] widgets = creationFeature.getLastWidgets();
      if ( widgets.length > 0) 
      {
        container.addTab( "", (Component)widgets[ 0]);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#removeWidget(org.xidget.IXidget)
   */
  public void removeWidget( IXidget child)
  {
    JTabbedPane container = xidget.getFeature( JTabbedPane.class);
    if ( container != null)
    {
      IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
      Object[] widgets = creationFeature.getLastWidgets();
      if ( widgets.length > 0) 
      {
        int index = getTabIndex( container, (Component)widgets[ 0]);
        if ( index >= 0) container.removeTabAt( index);
      }
    }
  }
  
  /**
   * Returns the tab index of the specified tab.
   * @param container The container.
   * @param component The tab component.
   * @return Returns -1 or the tab index of the specified tab.
   */
  private int getTabIndex( JTabbedPane container, Component component)
  {
    for( int i=0; i<container.getTabCount(); i++)
    {
      if ( container.getComponentAt( i) == component)
        return i;
    }
    return -1;
  }
  
  private IXidget xidget;
}
