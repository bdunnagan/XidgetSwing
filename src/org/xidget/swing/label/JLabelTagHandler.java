/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.label;

import org.xidget.IXidget;
import org.xidget.binding.XidgetTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagProcessor;
import org.xmodel.IModelObject;

/**
 * A filtered XidgetTagHandler since <i>label</i> is an overloaded element name.
 */
public class JLabelTagHandler extends XidgetTagHandler
{
  public JLabelTagHandler( Class<? extends IXidget> xidgetClass)
  {
    super( xidgetClass);
  }

  /* (non-Javadoc)
   * @see org.xidget.config.AbstractTagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  @Override
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    if ( !super.filter( processor, parent, element)) return false;
    return element.getParent().isType( "form");
  }
}
