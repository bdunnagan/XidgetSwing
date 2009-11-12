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
import javax.swing.JDialog;
import org.xidget.IXidget;
import org.xidget.ifeature.dialog.IDialogFeature;
import org.xidget.layout.Size;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
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
    
    // size to fit first
    widget.pack();
    
    // set size if requested
    IModelObject config = xidget.getConfig();
    Dimension oldSize = widget.getSize();
    Size newSize = new Size( Xlate.get( config, "size", (String)null), -1, -1);
    if ( newSize.width < 0) newSize.width = oldSize.width;
    if ( newSize.height < 0) newSize.height = oldSize.height; 
    widget.setSize( newSize.width, newSize.height);
    
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
