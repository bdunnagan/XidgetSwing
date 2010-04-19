/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JDialogFeature.java
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

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JDialog;
import org.xidget.IXidget;
import org.xidget.ifeature.dialog.IDialogFeature;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IDialogFeature for Swing JDialog.
 */
public class JDialogFeature implements IDialogFeature
{
  public JDialogFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.dialog.IDialogFeature#open(org.xmodel.xpath.expression.StatefulContext)
   */
  public void open( StatefulContext context)
  {
    JDialog widget = xidget.getFeature( JDialog.class);
    
    // save requested bounds
    Rectangle rectangle = widget.getBounds();

    // pack
    widget.pack();
    
    // set requested dimensions
    if ( rectangle.width < 0) rectangle.width = widget.getWidth();
    if ( rectangle.height < 0) rectangle.height = widget.getHeight();
    widget.setSize( new Dimension( rectangle.width, rectangle.height));
    
    // show
    widget.setVisible( true);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.dialog.IDialogFeature#close(org.xmodel.xpath.expression.StatefulContext)
   */
  public void close( StatefulContext context)
  {
    JDialog widget = xidget.getFeature( JDialog.class);
    widget.setVisible( false);
  }

  private IXidget xidget;
}
