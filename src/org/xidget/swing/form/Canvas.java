/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * Canvas.java
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
package org.xidget.swing.form;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;

import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.canvas.IPaintFeature;

/**
 * A custom JPanel that paints children with the IPaintFeature.
 */
@SuppressWarnings("serial")
public class Canvas extends JPanel
{
  public Canvas( IXidget xidget, LayoutManager layout)
  {
    super( layout);
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see java.awt.Container#doLayout()
   */
  @Override
  public void doLayout()
  {
    if ( xidget.getParent().getConfig().isType( "tabs"))
    {
      Rectangle bounds = getBounds();
      IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
      widgetFeature.setDefaultBounds( bounds.x, bounds.y, bounds.width, bounds.height, true);
    }
    
    super.doLayout();
  }

  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @SuppressWarnings("unchecked")
  @Override
  protected void paintComponent( Graphics graphics)
  {
    super.paintComponent( graphics);
    
    IPaintFeature<Graphics> paintFeature = xidget.getFeature( IPaintFeature.class);
    if ( paintFeature != null) paintFeature.paint( graphics);
  }
  
  private IXidget xidget;
}
