/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.label;

import javax.swing.JLabel;
import org.xidget.IXidget;
import org.xidget.ifeature.ISourceFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ISourceFeature for the Swing JLabel widget.
 */
public class JLabelSourceFeature implements ISourceFeature
{
  public JLabelSourceFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#getSource(java.lang.String)
   */
  public IModelObject getSource( String channel)
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#setSource(java.lang.String, org.xmodel.IModelObject)
   */
  public void setSource( StatefulContext context, String channel, IModelObject node)
  {
    if ( channel == ISourceFeature.allChannel)
    {
      JLabel jLabel = xidget.getFeature( JLabel.class);
      jLabel.setText( Xlate.get( node, ""));
    }
  }
  
  private IXidget xidget;
}
