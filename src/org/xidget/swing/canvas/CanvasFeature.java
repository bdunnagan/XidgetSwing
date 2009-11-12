/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * CanvasFeature.java
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
package org.xidget.swing.canvas;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.xidget.IXidget;
import org.xidget.ifeature.canvas.ICanvasFeature;
import org.xidget.ifeature.canvas.IPaintFeature;
import org.xmodel.util.HashMultiMap;
import org.xmodel.util.MultiMap;

/**
 * An implementation of ICanvasFeature that uses the Swing JPanel.
 */
public class CanvasFeature implements ICanvasFeature, IPaintFeature
{
  public CanvasFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.ICanvasFeature#addLayer(java.lang.String, int)
   */
  public void addLayer( String layer, int zorder)
  {
    if ( layers == null) layers = new ArrayList<String>();
    if ( layers.size() <= zorder)
    {
      for( int i=zorder; i<=layers.size(); i++)
        layers.add( null);
    }
    layers.set( zorder, layer);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.ICanvasFeature#removeLayer(java.lang.String)
   */
  public void removeLayer( String layer)
  {
    for( int i=0; i<layers.size(); i++)
    {
      if ( layers.get( i).equals( layer))
      {
        layers.set( i, null);
        break;
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.ICanvasFeature#addChild(org.xidget.IXidget)
   */
  public void addChild( IXidget child)
  {
    if ( children == null) children = new HashMultiMap<String, IXidget>();
    IPaintFeature paintFeature = child.getFeature( IPaintFeature.class);
    if ( paintFeature != null)
    {
      String layer = paintFeature.getLayer();
      children.put( layer, child);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.ICanvasFeature#removeChild(org.xidget.IXidget)
   */
  public void removeChild( IXidget child)
  {
    if ( children == null) return;
    
    IPaintFeature paintFeature = child.getFeature( IPaintFeature.class);
    if ( paintFeature != null)
    {
      String layer = paintFeature.getLayer();
      children.remove( layer, child);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.ICanvasFeature#getHeight()
   */
  public int getHeight()
  {
    JPanel jPanel = xidget.getFeature( JPanel.class);
    return jPanel.getWidth();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.ICanvasFeature#getWidth()
   */
  public int getWidth()
  {
    JPanel jPanel = xidget.getFeature( JPanel.class);
    return jPanel.getHeight();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.ICanvasFeature#repaint()
   */
  public void repaint()
  {
    JPanel jPanel = xidget.getFeature( JPanel.class);
    jPanel.repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.ICanvasFeature#repaint(int, int, int, int)
   */
  public void repaint( int x, int y, int width, int height)
  {
    JPanel jPanel = xidget.getFeature( JPanel.class);
    jPanel.repaint( x, y, width, height);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.IPaintFeature#setLayer(java.lang.String)
   */
  public void setLayer( String layer)
  {
    this.layer = layer;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.IPaintFeature#getLayer()
   */
  public String getLayer()
  {
    return layer;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.IPaintFeature#paint(java.lang.Object)
   */
  public void paint( Object graphics)
  {
    for( String layer: layers)
    {
      for( IXidget child: children.get( layer))
      {
        if ( child != null)
        {
          IPaintFeature paintFeature = child.getFeature( IPaintFeature.class);
          if ( paintFeature != null) paintFeature.paint( graphics);
        }
      }
    }
  }

  private IXidget xidget;
  private String layer;
  private List<String> layers;
  private MultiMap<String, IXidget> children;
}
