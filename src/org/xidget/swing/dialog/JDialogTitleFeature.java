/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JDialogTitleFeature.java
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
package org.xidget.swing.dialog;

import javax.swing.JDialog;
import org.xidget.IXidget;
import org.xidget.ifeature.ITitleFeature;

/**
 * An implementation of ITitleFeature for setting the title of a Swing JDialog.
 */
public class JDialogTitleFeature implements ITitleFeature
{
  public JDialogTitleFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITitleFeature#setTitle(java.lang.String)
   */
  public void setTitle( String title)
  {
    JDialog widget = xidget.getFeature( JDialog.class);
    if ( widget != null) widget.setTitle( title);
  }
  
  private IXidget xidget;
}

