/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JTabbedPaneSelectionWidgetFeature.java
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
import java.util.Collections;
import java.util.List;

import javax.swing.JTabbedPane;

import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.model.ISelectionWidgetFeature;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * Implementation of ISelectionWidgetFeature for JTabbedPane widget.
 */
public class JTabbedPaneSelectionWidgetFeature implements ISelectionWidgetFeature
{
  public JTabbedPaneSelectionWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#select(java.lang.Object)
   */
  @Override
  public void select( Object object)
  {
    for( IXidget child: xidget.getChildren())
    {
      IBindFeature bindFeature = child.getFeature( IBindFeature.class);
      StatefulContext context = bindFeature.getBoundContext();
      if ( context.getObject().equals( object))
      {
        IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
        Object[] widgets = creationFeature.getLastWidgets();
        
        JTabbedPane jTabbedPane = xidget.getFeature( JTabbedPane.class);
        jTabbedPane.setSelectedComponent( (Component)widgets[ 0]);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#deselect(java.lang.Object)
   */
  @Override
  public void deselect( Object object)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#setSelection(java.util.List)
   */
  @Override
  public void setSelection( List<? extends Object> objects)
  {
    if ( objects.size() == 0) return;
    
    Object selection = objects.get( 0);
    for( IXidget child: xidget.getChildren())
    {
      IBindFeature bindFeature = child.getFeature( IBindFeature.class);
      StatefulContext context = bindFeature.getBoundContext();
      if ( context.getObject().equals( selection))
      {
        IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
        Object[] widgets = creationFeature.getLastWidgets();
        
        JTabbedPane jTabbedPane = xidget.getFeature( JTabbedPane.class);
        jTabbedPane.setSelectedComponent( (Component)widgets[ 0]);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionWidgetFeature#getSelection()
   */
  public List<? extends Object> getSelection()
  {
    JTabbedPane jTabbedPane = xidget.getFeature( JTabbedPane.class);
    Component selected = jTabbedPane.getSelectedComponent();
    for( IXidget child: xidget.getChildren())
    {
      IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
      Object[] widgets = creationFeature.getLastWidgets();
      if ( widgets[ 0] == selected)
      {
        IBindFeature bindFeature = child.getFeature( IBindFeature.class);
        StatefulContext context = bindFeature.getBoundContext();
        return Collections.singletonList( context.getObject());
      }
    }    
    return Collections.emptyList();
  }

  private IXidget xidget;
}
