/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text;

import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.feature.IWidgetHierarchyFeature;
import org.xidget.swing.ISwingWidgetFeature;
import org.xidget.swing.SwingWidgetFeature;
import org.xidget.swing.feature.SwingTooltipErrorFeature;
import org.xidget.swing.text.feature.SwingTextWidgetHierarchyFeature;
import org.xidget.swing.text.feature.TextFeature;
import org.xidget.text.TextXidget;
import org.xidget.text.feature.IWidgetTextFeature;

/**
 * An implementation of TextXidget for a Swing text widget.
 */
public class SwingTextXidget extends TextXidget
{  
  public SwingTextXidget()
  {
    hierarchyFeature = new SwingTextWidgetHierarchyFeature( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.TextXidget#getErrorFeature()
   */
  @Override
  protected IErrorFeature getErrorFeature()
  {
    return new SwingTooltipErrorFeature( hierarchyFeature.getWidget());
  }

  /* (non-Javadoc)
   * @see org.xidget.text.TextXidget#getWidgetFeature()
   */
  @Override
  protected IWidgetFeature getWidgetFeature()
  {
    return new SwingWidgetFeature( hierarchyFeature.getWidget());
  }

  /* (non-Javadoc)
   * @see org.xidget.text.TextXidget#getWidgetTextFeature()
   */
  @Override
  protected IWidgetTextFeature getWidgetTextFeature()
  {
    return new TextFeature( hierarchyFeature.getTextWidget());
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss.equals( ISwingWidgetFeature.class)) return (T)hierarchyFeature;
    if ( clss.equals( IWidgetHierarchyFeature.class)) return (T)hierarchyFeature;
    return super.getFeature( clss);
  }

  private SwingTextWidgetHierarchyFeature hierarchyFeature;
}
