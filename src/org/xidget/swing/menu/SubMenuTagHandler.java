/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * SubMenuTagHandler.java
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

import org.xidget.binding.XidgetTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagProcessor;
import org.xmodel.IModelObject;

/**
 * An implementation of ITagHandler for top-level popup menus.
 */
public class SubMenuTagHandler extends XidgetTagHandler
{
  public SubMenuTagHandler()
  {
    super( JMenuXidget.class);
  }

  /* (non-Javadoc)
   * @see org.xidget.config.AbstractTagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  @Override
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    return element.getParent().isType( "menu") || element.getParent().isType( "menubar"); 
  }
}
