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
import org.xmodel.external.NonSyncingListener;
import org.xmodel.xml.XmlIO;
import org.xmodel.xml.IXmlIO.Style;

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
   * @see org.xidget.ifeature.ISourceFeature#getSource()
   */
  public IModelObject getSource()
  {
    return node;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#setSource(org.xmodel.IModelObject)
   */
  public void setSource( IModelObject node)
  {
    if ( node != null && !validate( node)) return;
    
    if ( this.node != null) listener.uninstall( this.node);
    this.node = node;
    if ( this.node != null) listener.install( this.node);
    
    update();
  }
  
  /**
   * Set the updating flag.
   * @param updating The updating flag.
   */
  void setUpdating( boolean updating)
  {
    this.updating = updating;
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

  /**
   * Update the context from the source node.
   */
  private void update()
  {
    if ( updating) return;
    
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
  
  private NonSyncingListener listener = new NonSyncingListener() {
    public void notifyAddChild(IModelObject parent, IModelObject child, int index) 
    {
      super.notifyAddChild(parent, child, index);
      update(); 
    }

    public void notifyRemoveChild(IModelObject parent, IModelObject child, int index) 
    {
      super.notifyRemoveChild( parent, child, index);
      update(); 
    }

    public void notifyChange(IModelObject object, String attrName, Object newValue, Object oldValue) 
    {
      update(); 
    }

    public void notifyClear(IModelObject object, String attrName, Object oldValue) 
    {
      update(); 
    }
  };
  
  private IXidget xidget;
  private IModelObject node;
  private XmlIO xmlIO;
  private boolean updating;
}
