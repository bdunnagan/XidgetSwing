/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JTabbedPaneWidgetCreationFeature.java
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

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Collections;
import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xidget.IXidget;
import org.xidget.binding.IXidgetBinding;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.model.ISelectionUpdateFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IWidgetCreationFeature which creates a Swing JTabbedPane widget.
 */
public class JTabbedPaneWidgetCreationFeature implements IWidgetCreationFeature
{
  public JTabbedPaneWidgetCreationFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#createWidget()
   */
  public void createWidgets()
  {
    jtabbedPane = new JTabbedPane();
    jtabbedPane.addChangeListener( selectionListener);
    jtabbedPane.addComponentListener( componentListener);

    // create titled border if necessary (but not for tab entries)
    IXidget parent = xidget.getParent();
    String title = getTitle();
    if ( title != null && title.length() > 0 && parent != null)
    {
      if ( !parent.getConfig().isType( "tabs"))
        jtabbedPane.setBorder( new TitledBorder( title));
    }

    // add panel to parent container
    IWidgetContainerFeature containerFeature = xidget.getParent().getFeature( IWidgetContainerFeature.class);
    if ( containerFeature != null) containerFeature.addWidget( xidget);
  }

  /**
   * Returns the title of the form.
   * @return Returns null or the title of the form.
   */
  private String getTitle()
  {
    IModelObject element = xidget.getConfig();
    IExpression titleExpr = Xlate.childGet( element, "title", Xlate.get( element, "title", (IExpression)null));
    if ( titleExpr != null) return titleExpr.evaluateString();
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidgets()
  {
    jtabbedPane.getParent().remove( jtabbedPane);
    jtabbedPane = null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jtabbedPane};
  }

  /**
   * Returns the JTabbedPane created for this form.
   * @return Returns the JTabbedPane created for this form.
   */
  public JTabbedPane getJTabbedPane()
  {
    return jtabbedPane;
  }

  private ComponentListener componentListener = new ComponentAdapter() {
    public void componentResized( ComponentEvent e)
    {
      Dimension contentSize = null;
      for( Component child: jtabbedPane.getComponents())
      {
        if ( child instanceof Canvas)
        {
          Container container = (Container)child;
          contentSize = container.getSize();
          break;
        }
      }
      
      if ( contentSize != null)
      {
        for( IXidget child: xidget.getChildren())
        {
          IWidgetCreationFeature feature = child.getFeature( IWidgetCreationFeature.class);
          if ( feature != null)
          {
            Object[] widgets = feature.getLastWidgets();
            if ( widgets.length > 0)
            {
              Component content = (Component)widgets[ 0];
              content.setSize( contentSize);
            }
          }
        }
      }
    }
  };
  
  private ChangeListener selectionListener = new ChangeListener() {
    public void stateChanged( ChangeEvent e)
    {
      List<IXidget> children = xidget.getChildren();
      int index = jtabbedPane.getSelectedIndex();
      if ( index >= 0 && index < children.size())
      { 
        IXidget child = children.get( index);
        IBindFeature bindFeature = child.getFeature( IBindFeature.class);
        StatefulContext childContext = bindFeature.getBoundContext();
        if ( childContext != null)
        {
          jtabbedPane.removeChangeListener( this);
          try
          {
            ISelectionUpdateFeature selectionFeature = xidget.getFeature( ISelectionUpdateFeature.class);
            selectionFeature.modelSelect( Collections.singletonList( childContext.getObject()));
          }
          finally
          {
            jtabbedPane.addChangeListener( this);
          }
        }
        else
        {
          //
          // The initial selection notification happens before the child xidget has been bound. So we need
          // to add an IXidgetBinding temporarily which will update the selection after the xidget is bound. 
          //
          //IXidgetBinding binding = new InitialNotificationBinding( child);
          //bindFeature.addBindingAfterChildren( binding);
        }
      }
    }
  };

  private class InitialNotificationBinding implements IXidgetBinding
  {
    public InitialNotificationBinding( IXidget xidget)
    {
      this.xidget = xidget;
    }
    
    /* (non-Javadoc)
     * @see org.xidget.binding.IXidgetBinding#bind(org.xmodel.xpath.expression.StatefulContext)
     */
    public void bind( StatefulContext context)
    {
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    
      // update selection
      StatefulContext childContext = bindFeature.getBoundContext();
      jtabbedPane.removeChangeListener( selectionListener);
      try
      {
        ISelectionUpdateFeature selectionFeature = xidget.getFeature( ISelectionUpdateFeature.class);
        selectionFeature.modelSelect( Collections.singletonList( childContext.getObject()));
      }
      finally
      {
        jtabbedPane.addChangeListener( selectionListener);
      }
      
      // remove binding later
      SwingUtilities.invokeLater( removeInitialNotificationBindingRunnable);
    }
    
    /* (non-Javadoc)
     * @see org.xidget.binding.IXidgetBinding#unbind(org.xmodel.xpath.expression.StatefulContext)
     */
    public void unbind( StatefulContext context)
    {
    }
    
    private Runnable removeInitialNotificationBindingRunnable = new Runnable() {
      public void run()
      {
        IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
        bindFeature.remove( InitialNotificationBinding.this);
      }
    };
    
    private IXidget xidget;
  };
  
  private IXidget xidget;
  private JTabbedPane jtabbedPane;
}
