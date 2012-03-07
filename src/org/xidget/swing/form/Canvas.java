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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JPanel;
import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.ifeature.IColorFeature;
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
    IXidget parent = xidget.getParent();
    if ( parent != null && parent.getConfig().isType( "tabs"))
    {
      Rectangle bounds = getBounds();
      IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
      widgetFeature.setDefaultBounds( bounds.x, bounds.y, bounds.width, bounds.height, true);
    }
    
    super.doLayout();
  }

  /**
   * Set the background color or gradient to the specified xidget color specification.
   * @param color A xidget color/gradient specification.
   */
  public void setBackground( Object bcolor)
  {
    this.bcolor = bcolor;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @SuppressWarnings("unchecked")
  @Override
  protected void paintComponent( Graphics graphics)
  {
    if ( bcolor == null)
    {
      super.paintComponent( graphics);
    }
    else if ( isOpaque())
    {
      Graphics2D g2d = (Graphics2D)graphics.create();
      IColorFeature<Color, Graphics2D> colorFeature = Creator.getToolkit().getFeature( IColorFeature.class);
      colorFeature.applyColor( bcolor, g2d, getWidth(), getHeight());
      g2d.fillRect( 0, 0, getWidth(), getHeight());
    }
    
    IPaintFeature<Graphics> paintFeature = xidget.getFeature( IPaintFeature.class);
    if ( paintFeature != null) paintFeature.paint( graphics);
  }
  
  private IXidget xidget;
  private Object bcolor;
}
