/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JComboBoxTextWidgetFeature.java
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
package org.xidget.swing.combo;

import javax.swing.JComboBox;

import org.xidget.IXidget;
import org.xidget.swing.feature.SwingTextWidgetFeature;

/**
 * An implementation of IWidgetFeature for JComboBox.
 */
public class JComboBoxTextWidgetFeature extends SwingTextWidgetFeature
{
  public JComboBoxTextWidgetFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IWidgetTextAdapter#setEditable(boolean)
   */
  public void setEditable( boolean editable)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.setEditable( editable);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setHAlign(org.xidget.ifeature.ITextWidgetFeature.HAlign)
   */
  @Override
  public void setHAlign( HAlign alignment)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setVAlign(org.xidget.ifeature.ITextWidgetFeature.VAlign)
   */
  @Override
  public void setVAlign( VAlign alignment)
  {
  }
}
