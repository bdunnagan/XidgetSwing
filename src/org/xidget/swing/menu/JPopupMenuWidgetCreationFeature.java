/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JPopupMenuWidgetCreationFeature.java
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

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;

/**
 * An implementation of IWidgetCreationFeature for the Swing JPopupMenu widget. 
 */
public class JPopupMenuWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public JPopupMenuWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    jPopupMenu = new JPopupMenu();
    
    IWidgetCreationFeature creationFeature = xidget.getParent().getFeature( IWidgetCreationFeature.class);
    
    // add popup to inner-most component
    Object[] objects = creationFeature.getLastWidgets();
    for( Object object: objects)
    {
      JComponent component = (JComponent)object;
      component.add( jPopupMenu);
      component.addMouseListener( new PopupMouseListener( xidget));
    }
    
    return jPopupMenu;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jPopupMenu};
  }
  
  /**
   * Returns the JPopupMenu that was created.
   * @return Returns the JPopupMenu that was created.
   */
  public JPopupMenu getJPopupMenu()
  {
    return jPopupMenu;
  }
  
  private JPopupMenu jPopupMenu;
}
