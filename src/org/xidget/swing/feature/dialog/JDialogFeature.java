/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.dialog;

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
 * @deprecated Use IWidgetFeature.setVisible.
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
