/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JLabelTagHandler.java
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
