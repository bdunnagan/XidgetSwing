/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.menu;

import org.xidget.binding.XidgetTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagProcessor;
import org.xmodel.IModelObject;

/**
 * An implementation of ITagHandler for top-level popup menus.
 */
public class MenuTagHandler extends XidgetTagHandler
{
  public MenuTagHandler()
  {
    super( JPopupMenuXidget.class);
  }

  /* (non-Javadoc)
   * @see org.xidget.config.AbstractTagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  @Override
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    return !element.getParent().isType( "menu") && !element.getParent().isType( "menubar"); 
  }
}
