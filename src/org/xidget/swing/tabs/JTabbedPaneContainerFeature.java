/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JTabbedPaneContainerFeature.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.swing.tabs;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;

import javax.swing.JTabbedPane;

import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.layout.Margins;
import org.xmodel.Xlate;

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
    addWidget( -1, child);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#addWidget(int, org.xidget.IXidget)
   */
  public void addWidget( int index, IXidget child)
  {
    JTabbedPane container = xidget.getFeature( JTabbedPane.class);
    if ( container != null)
    {
      boolean removable = Xlate.get( xidget.getConfig(), "removable", false) ||
        Xlate.get( child.getConfig(), "removable", false);
      
      IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
      Object[] widgets = creationFeature.getLastWidgets();
      if ( widgets.length > 0) 
      {
        CustomTab tab = new CustomTab( child);
        tab.setCloseButton( removable);
        
        Component component = (Component)widgets[ 0];
        if ( index < 0) 
        {
          container.addTab( "", component);
          container.setTabComponentAt( container.getTabCount()-1, tab);
        }
        else 
        {
          container.insertTab( "", null, component, null, index);
          container.setTabComponentAt( index, tab);
        }
        
        //
        // Workaround: Java 6.0 build 17 exhibits refresh problem when opening multiple tabs.
        //
        container.setSelectedComponent( component);
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
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#relayout()
   */
  public void relayout()
  {
    ILayoutFeature layoutFeature = xidget.getFeature( ILayoutFeature.class);
    if ( layoutFeature != null) layoutFeature.invalidate();
    
    Container container = xidget.getFeature( Container.class);
    if ( container != null && container.isShowing()) container.validate();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#setInsideMargins(org.xidget.layout.Margins)
   */
  @Override
  public void setInsideMargins( Margins margins)
  {
    this.margins = margins;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#getInsideMargins()
   */
  public Margins getInsideMargins()
  {
    if ( margins == null)
    {
      Container container = xidget.getFeature( Container.class);
      Insets insets = container.getInsets();
      margins = new Margins();
      margins.x0 = insets.left;
      margins.y0 = insets.top;
      margins.x1 = insets.right;
      margins.y1 = insets.bottom;
    }
    return margins;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#setSpacing(int)
   */
  @Override
  public void setSpacing( int spacing)
  {
    this.spacing = spacing;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#getSpacing()
   */
  @Override
  public int getSpacing()
  {
    return spacing;
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
  private Margins margins;
  private int spacing;
}
