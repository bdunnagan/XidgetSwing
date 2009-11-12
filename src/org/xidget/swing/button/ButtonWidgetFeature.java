/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * ButtonWidgetFeature.java
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
package org.xidget.swing.button;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import org.xidget.IXidget;
import org.xidget.ifeature.button.IButtonWidgetFeature;

/**
 * An implementation of IButtonWidgetFeature for Swing AbstractButton widgets.
 */
public class ButtonWidgetFeature implements IButtonWidgetFeature
{
  public ButtonWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.button.IButtonWidgetFeature#setState(boolean)
   */
  public void setState( boolean state)
  {
    AbstractButton button = xidget.getFeature( AbstractButton.class);
    if ( button != null)
    {
      ButtonModel model = button.getModel();
      model.setSelected( state);
    }
  }
  
  private IXidget xidget;
}
