/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import org.xidget.IXidget;
import org.xidget.Log;
import org.xidget.ifeature.IAsyncFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.layout.Margins;

/**
 * An implementation of IWidgetContainerFeature that assumes that the parent xidget exports
 * and instance of a Swing Container.
 */
public class GenericContainerFeature implements IWidgetContainerFeature
{
  public final static int validationDelay = 0;
  
  public GenericContainerFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#addWidget(org.xidget.IXidget)
   */
  public void addWidget( IXidget child)
  {
    Container container = xidget.getFeature( Container.class);
    if ( container != null)
    {
      IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
      Object[] widgets = creationFeature.getLastWidgets();
      if ( widgets.length > 0) 
      {
        Log.printf( "xidget", "GenericContainerFeature.addWidget: %s <- %s\n", xidget, child);
        container.add( (Component)widgets[ 0]);
        
        // validate the container later to improve performance
        if ( container.isShowing())
        {
          IAsyncFeature asyncFeature = xidget.getFeature( IAsyncFeature.class);
          asyncFeature.schedule( this, validationDelay, false, validateRunnable);
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#removeWidget(org.xidget.IXidget)
   */
  public void removeWidget( IXidget child)
  {
    Container container = xidget.getFeature( Container.class);
    if ( container != null)
    {
      IWidgetCreationFeature creationFeature = child.getFeature( IWidgetCreationFeature.class);
      Object[] widgets = creationFeature.getLastWidgets();
      if ( widgets.length > 0) container.remove( (Component)widgets[ 0]);
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#relayout()
   */
  public void relayout()
  {
    ILayoutFeature layoutFeature = xidget.getFeature( ILayoutFeature.class);
    if ( layoutFeature != null) layoutFeature.configure();
    
    Container container = xidget.getFeature( Container.class);
    if ( container != null && container.isShowing()) container.validate();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContainerFeature#getInsideMargins()
   */
  public Margins getInsideMargins()
  {
    Container container = xidget.getFeature( Container.class);
    Insets insets = container.getInsets();
    Margins margins = new Margins();
    margins.x0 = insets.left;
    margins.y0 = insets.top;
    margins.x1 = insets.right;
    margins.y1 = insets.bottom;
    return margins;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return xidget.toString();
  }

  private Runnable validateRunnable = new Runnable() {
    public void run()
    {
      Container container = xidget.getFeature( Container.class);
      container.validate();
    }
  };
  
  private IXidget xidget;
}
