/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.ifeature.IFocusFeature;
import org.xidget.ifeature.IWidgetCreationFeature;

/**
 * An implementation of IFocusFeature for the Swing platform.
 */
public class SwingFocusFeature implements IFocusFeature
{
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IFocusFeature#setFocus(org.xidget.IXidget)
   */
  @Override
  public void setFocus( IXidget xidget)
  {
    IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    if ( creationFeature != null)
    {
      Object[] widgets = creationFeature.getLastWidgets();
      if ( widgets.length > 0)
      {
        Component component = (Component)(widgets[ widgets.length - 1]);
        component.requestFocus();
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IFocusFeature#getFocus()
   */
  @Override
  public IXidget getFocus()
  {
    Component component = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
    while( component != null)
    {
      IXidget xidget = Creator.getInstance().getXidget( component);
      if ( xidget != null) return xidget;
      component = component.getParent();
    }
    
    List<IXidget> active = Creator.getInstance().getActiveHierarchies();
    if ( active.size() > 0) return active.get( 0);
    
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IFocusFeature#addFocusListener(org.xidget.ifeature.IFocusFeature.IFocusListener)
   */
  @Override
  public void addFocusListener( IFocusListener listener)
  {
    KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    focusManager.addPropertyChangeListener( "focusOwner", new GlobalFocusListener( listener));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IFocusFeature#removeFocusListener(org.xidget.ifeature.IFocusFeature.IFocusListener)
   */
  @Override
  public void removeFocusListener( IFocusListener listener)
  {
    KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    focusManager.removePropertyChangeListener( "focusOwner", new GlobalFocusListener( listener));
  }
  
  private static class GlobalFocusListener implements PropertyChangeListener
  {
    public GlobalFocusListener( IFocusListener focusListener)
    {
      this.focusListener = focusListener;
    }
    
    public void propertyChange( PropertyChangeEvent e) 
    {
      Component oldWidget = (Component)e.getOldValue();
      Component newWidget = (Component)e.getNewValue();
      
      IXidget oldXidget = Creator.getInstance().getXidget( oldWidget);
      IXidget newXidget = Creator.getInstance().getXidget( newWidget);
      
      focusListener.notifyFocus( newXidget, oldXidget);
    }    
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object object)
    {
      if ( object instanceof GlobalFocusListener)
      {
        return ((GlobalFocusListener)object).focusListener == focusListener;
      }
      return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
      return focusListener.hashCode();
    }

    private IFocusListener focusListener;
  };
}
