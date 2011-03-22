/**
 * Xidget - WYSIWYG Xidget Builder
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */

package org.xidget.swing.xmleditor;

import org.xidget.IXidget;
import org.xidget.ifeature.ISourceFeature;
import org.xmodel.IModelObject;
import org.xmodel.external.IExternalReference;
import org.xmodel.external.NonSyncingIterator;
import org.xmodel.xml.XmlIO;
import org.xmodel.xml.IXmlIO.Style;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ISourceFeature for a net.boplicity.xmleditor.XmlTextPane.
 * This implementation only supports "all" channel.
 */
public class XmlTextPaneSourceFeature implements ISourceFeature
{
  public XmlTextPaneSourceFeature( IXidget xidget)
  {
    this.xidget = xidget;
    
    xmlIO = new XmlIO();
    xmlIO.setOutputStyle( Style.printable);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#getSource(java.lang.String)
   */
  public IModelObject getSource( String channel)
  {
    if ( channel == allChannel) return node;
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#setSource(java.lang.String, org.xmodel.IModelObject)
   */
  public void setSource( StatefulContext context, String channel, IModelObject node)
  {
    if ( !validate( node)) return;
    
    if ( channel == allChannel) 
    {
      this.node = node;
      
      // update content of editor
      XmlTextPane xmlTextPane = xidget.getFeature( XmlTextPane.class);
      if ( xmlTextPane != null)
      {
        if ( node != null)
        {
          String xml = xmlIO.write( node);
          xmlTextPane.setText( xml);
          xmlTextPane.setCaretPosition( 0);
        }
        else
        {
          xmlTextPane.setText( "");
        }
      }
    }
  }
  
  /**
   * Returns true if the specified node is editable element. Elements that have 
   * descendants that are external references can not be edited.
   * @param source The node to be validated.
   * @return Returns true if the node is editable.
   */
  private boolean validate( IModelObject source)
  {
    NonSyncingIterator iter = new NonSyncingIterator( source);
    while( iter.hasNext())
    {
      IModelObject node = iter.next();
      if ( node instanceof IExternalReference)
        return false;
    }
    return true;
  }
  
  private IXidget xidget;
  private IModelObject node;
  private XmlIO xmlIO;
}
