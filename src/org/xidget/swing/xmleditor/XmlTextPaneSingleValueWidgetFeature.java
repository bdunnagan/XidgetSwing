/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.xmleditor;

import org.xidget.IXidget;
import org.xidget.ifeature.model.ISingleValueWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.xml.XmlException;
import org.xmodel.xml.XmlIO;

public class XmlTextPaneSingleValueWidgetFeature implements ISingleValueWidgetFeature
{
  public XmlTextPaneSingleValueWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    xmlIO = new XmlIO();
  }
  
  /**
   * Tell the feature to ignore updates.
   * @param ignore True if updates should be ignored.
   */
  public void ignoreUpdate( boolean ignore)
  {
    updating = ignore;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueWidgetFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    XmlTextPane textPane = xidget.getFeature( XmlTextPane.class);
    String text = textPane.getText();
    try
    {
      IModelObject node = xmlIO.read( text);
      return node;
    }
    catch( XmlException e)
    {
      return null;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueWidgetFeature#setValue(java.lang.Object)
   */
  @Override
  public void setValue( Object value)
  {
    if ( updating) return;
    if ( !(value instanceof IModelObject)) return;
    
    String text = xmlIO.write( (IModelObject)value);
    if ( text != null)
    {
      XmlTextPane textPane = xidget.getFeature( XmlTextPane.class);
      textPane.setText( text);
      textPane.setCaretPosition( 0);
    }
  }

  private IXidget xidget;
  private XmlIO xmlIO;
  private boolean updating;
}
