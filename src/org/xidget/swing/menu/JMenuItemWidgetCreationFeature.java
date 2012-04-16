/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JMenuItemWidgetCreationFeature.java
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
package org.xidget.swing.menu;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.xidget.IXidget;
import org.xidget.ifeature.IImageFeature;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.IScriptFeature;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IWidgetCreationFeature for the Swing JMenuItem widget.
 */
public class JMenuItemWidgetCreationFeature extends SwingWidgetCreationFeature implements IImageFeature, ILabelFeature
{
  public JMenuItemWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    String spec = Xlate.get( xidget.getConfig(), "type", "push");
    if ( !spec.equals( "push")) throw new UnsupportedOperationException( "Only push menu items are currently supported.");

    IModelObject config = xidget.getConfig();
    if ( config.isType( "separator"))
    {
      IXidget parent = xidget.getParent();
      
      JMenu menu = parent.getFeature( JMenu.class);
      if ( menu != null) menu.addSeparator();
      
      JPopupMenu popupMenu = parent.getFeature( JPopupMenu.class);
      if ( popupMenu != null) popupMenu.addSeparator();
    }
    else
    {
      jMenuItem = new JMenuItem();
      jMenuItem.addActionListener( actionListener);
    }
    
    return jMenuItem;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    if ( jMenuItem == null) return new Object[ 0];
    return new Object[] { jMenuItem};
  }
  
  /**
   * Returns the JMenuItem that was created.
   * @return Returns the JMenuItem that was created.
   */
  public JMenuItem getJMenuItem()
  {
    return jMenuItem;
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IImageFeature#setIcon(java.lang.Object)
   */
  public void setImage( Object image)
  {
    jMenuItem.setIcon( new ImageIcon( (Image)image));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#getLabelWidth()
   */
  public int getLabelWidth()
  {
    return 0;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#setWidth(int)
   */
  public void setLabelWidth( int width)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#setLabelText(java.lang.String)
   */
  public void setLabelText( String text)
  {
    jMenuItem.setText( text);
  }

  private ActionListener actionListener = new ActionListener() {
    public void actionPerformed( ActionEvent e)
    {
      IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
      if ( scriptFeature != null) 
      {
        IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
        StatefulContext context = contextFeature.getContext( e.getSource());
        if ( context != null) scriptFeature.runScript( "onPress", context);
      }
    }
  };
  
  private JMenuItem jMenuItem;
}
