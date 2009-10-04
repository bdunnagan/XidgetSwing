/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import javax.swing.JTabbedPane;
import org.xidget.IXidget;
import org.xidget.ifeature.ITitleFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of ITitleFeature for suitable for all Swing containers.
 */
public class SwingTitleFeature implements ITitleFeature
{
  public SwingTitleFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITitleFeature#getTitle()
   */
  public String getTitle()
  {
    IModelObject element = xidget.getConfig();
    IExpression titleExpr = Xlate.childGet( element, "title", Xlate.get( element, "title", (IExpression)null));
    return titleExpr.evaluateString();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITitleFeature#setTitle(java.lang.String)
   */
  public void setTitle( String title)
  {
    IXidget parent = xidget.getParent();
    if ( parent == null) return;
    
    if ( parent.getConfig().isType( "tabs"))
    {
      JTabbedPane jtabbedPane = parent.getFeature( JTabbedPane.class);
      int index = parent.getChildren().indexOf( xidget);
      jtabbedPane.setTitleAt( index, title+"  ");
    }
  }

  private IXidget xidget;
}
