/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * SwingWidgetCreationFeature.java
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
package org.xidget.swing.feature;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IScriptFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;

/**
 * An abstract implementation of IWidgetCreationFeature which performs the common
 * work necessary to relayout the widget hierarchy after a widget is replaced.
 */
public abstract class SwingWidgetCreationFeature extends ComponentAdapter implements IWidgetCreationFeature
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

    // add to parent
    IWidgetContainerFeature containerFeature = xidget.getParent().getFeature( IWidgetContainerFeature.class);
    if ( containerFeature != null) containerFeature.addWidget( xidget);  
    
    // setup the widget
    Object[] widgets = getLastWidgets();
    if ( widgets.length > 0)
    {
      JComponent component = (JComponent)widgets[ widgets.length - 1];
    
      // use nimbus small components
//      component.putClientProperty("JComponent.sizeVariant", "small");
//      component.updateUI();
      
      // add single click listener
      IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
      if ( scriptFeature != null && (scriptFeature.hasScript( "onClick") || scriptFeature.hasScript( "onDoubleClick")))
      {
        component.addMouseListener( mouseListener);
      }
    }
  }
 
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidgets( IXidget parent)
  {
    // remove from parent
    IWidgetContainerFeature containerFeature = parent.getFeature( IWidgetContainerFeature.class);
    if ( containerFeature != null) containerFeature.removeWidget( xidget);
  }

  /**
   * Create the new swing widget.
   * @return Returns the new swing widget.
   */
  protected abstract JComponent createSwingWidget();
  
  /* (non-Javadoc)
   * @see java.awt.event.ComponentAdapter#componentShown(java.awt.event.ComponentEvent)
   */
  @Override
  public void componentShown( ComponentEvent e)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.event.ComponentAdapter#componentHidden(java.awt.event.ComponentEvent)
   */
  @Override
  public void componentHidden( ComponentEvent e)
  {
  }

  private final MouseListener mouseListener = new MouseAdapter() {
    public void mouseClicked( MouseEvent e)
    {
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
      IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
      if ( e.getClickCount() == 1)
      {
        scriptFeature.runScript( "onClick", bindFeature.getBoundContext());
      }
      else if ( e.getClickCount() == 2)
      {
        scriptFeature.runScript( "onDoubleClick", bindFeature.getBoundContext());
      }
    }
  };
  
  protected IXidget xidget;
}
