/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JPanelWidgetCreationFeature.java
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

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import org.xidget.IXidget;
import org.xidget.feature.AnchorLayoutFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.layout.AnchorNode;
import org.xidget.layout.IComputeNode;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of IWidgetCreationFeature which creates a Swing JPanel.
 */
public class JPanelWidgetCreationFeature implements IWidgetCreationFeature
{
  public JPanelWidgetCreationFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#createWidget()
   */
  public void createWidgets()
  {
    jPanel = new Canvas( xidget);
    jPanel.addMouseListener( mouseListener);
    jPanel.addMouseMotionListener( mouseListener);

    // create titled border if necessary (but not for tab entries)
    IXidget parent = xidget.getParent();
    if ( parent != null && hasTitle())
    {
      if ( !parent.getConfig().isType( "tabs"))
        jPanel.setBorder( new TitledBorder( getTitle()));
    }

    component = jPanel;
    
    // create scrollpane if requested
    if ( Xlate.get( xidget.getConfig(), "scroll", false))
    {
      component = new JScrollPane( jPanel);
    }
    
    // add panel to parent container
    IXidget xidgetParent = xidget.getParent();
    if ( xidgetParent != null)
    {
      IWidgetContainerFeature containerFeature = xidget.getParent().getFeature( IWidgetContainerFeature.class);
      if ( containerFeature != null) 
      {
        int index = xidgetParent.getChildren().indexOf( xidget); 
        containerFeature.addWidget( index, xidget);
      }
    }
  }
  
  /**
   * Returns true if the widget has a title.
   * @return Returns true if the widget has a title.
   */
  private boolean hasTitle()
  {
    IModelObject element = xidget.getConfig();
    IExpression titleExpr = Xlate.childGet( element, "title", Xlate.get( element, "title", (IExpression)null));
    return titleExpr != null;
  }

  /**
   * Evaluates the title expression in the null context and returns the result.
   * @return Returns an empty string or the title expression.
   */
  private String getTitle()
  {
    IModelObject element = xidget.getConfig();
    IExpression titleExpr = Xlate.childGet( element, "title", Xlate.get( element, "title", (IExpression)null));
    if ( titleExpr != null) return titleExpr.evaluateString();
    return "";
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidgets(IXidget parent)
  {
    // remove from parent
    IWidgetContainerFeature containerFeature = parent.getFeature( IWidgetContainerFeature.class);
    if ( containerFeature != null) containerFeature.removeWidget( xidget);

    // clear references
    jPanel = null;
    component = null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    if ( component != jPanel) return new Object[] { component, jPanel};
    return new Object[] { jPanel};
  }

  /**
   * @return Returns the inner widget.
   */
  public JPanel getInnerWidget()
  {
    return jPanel;
  }
  
  /**
   * @return Returns the outer widget.
   */
  public Component getOuterWidget()
  {
    return component;
  }
  
  /**
   * Returns the IComputeNode under the specified mouse position.
   * @param x The x of the mouse.
   * @param y The y of the mouse.
   * @return Returns the IComputeNode under the specified mouse position.
   */
  private AnchorNode mouseGrab( int x, int y)
  {
    AnchorNode grabbed = null;
    AnchorLayoutFeature feature = (AnchorLayoutFeature)xidget.getFeature( ILayoutFeature.class);
    
    List<IComputeNode> nodes = feature.getAllNodes();
    if ( nodes != null)
    {
      for( IComputeNode node: nodes)
      {
        if ( node.hasXHandle())
        {
          float nx = node.getValue();
          if ( Math.abs( nx - x) < 4) 
          {
            grabbed = (AnchorNode)node;
            break;
          }
        }
        else if ( node.hasYHandle())
        {
          float ny = node.getValue();
          if ( Math.abs( ny - y) < 4)
          {
            grabbed = (AnchorNode)node;
            break;
          }
        }
      }
    }
    
    return grabbed;
  }
  
  /**
   * Update the cursor based on the current grab state.
   * @param grabbed The grab state.
   */
  private void updateCursor( boolean grabbed)
  {
    if ( grabbed)
    {
      if ( cursor == null) 
      {
        cursor = jPanel.getCursor();
        jPanel.setCursor( Cursor.getPredefinedCursor( Cursor.E_RESIZE_CURSOR));
      }
    }
    else
    {
      if ( cursor != null) 
      {
        jPanel.setCursor( cursor);
        cursor = null;
      }
    }
  }
  
  private MouseInputListener mouseListener = new MouseInputAdapter() {
    public void mousePressed( MouseEvent e)
    {
      if ( e.getButton() == MouseEvent.BUTTON1)
      {
        grabbed = mouseGrab( e.getX(), e.getY());
        updateCursor( grabbed != null);
      }
    }
    public void mouseReleased( MouseEvent e)
    {
      grabbed = null;
      mouseGrab( e.getX(), e.getY());
      updateCursor( false);
    }
    public void mouseExited( MouseEvent e)
    {
      if ( grabbed != null) jPanel.grabFocus();
      updateCursor( grabbed != null);
    }
    public void mouseDragged( MouseEvent e)
    {
      if ( grabbed != null)
      {
        Rectangle bounds = jPanel.getBounds();
        if ( grabbed.hasXHandle())
        {
          float px = (float)e.getX() / bounds.width;
          grabbed.setFraction( px);
        }
        else
        {
          float py = (float)e.getY() / bounds.height;
          grabbed.setFraction( py);
        }
        
        jPanel.revalidate();
      }
    }
    public void mouseMoved( MouseEvent e)
    {
      updateCursor( mouseGrab( e.getX(), e.getY()) != null);
    }
  };

  private IXidget xidget;
  private JPanel jPanel;
  private Component component;
  private AnchorNode grabbed;
  private Cursor cursor;
}
