/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Dimension;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.layout.Size;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of IWidgetCreationFeature which creates a Swing JFrame for the application.
 */
public class JTabbedPaneWidgetCreationFeature implements IWidgetCreationFeature
{
  public JTabbedPaneWidgetCreationFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#createWidget()
   */
  public void createWidgets()
  {
    jtabbedPane = new JTabbedPane();
    
    IWidgetContainerFeature containerFeature = xidget.getParent().getFeature( IWidgetContainerFeature.class);
    if ( containerFeature != null) containerFeature.addWidget( xidget);
        
    // optionally constrain size
    IModelObject config = xidget.getConfig();
    Size size = new Size( Xlate.get( config, "size", (String)null), -1, -1);
    if ( size.width >= 0 || size.height >= 0) jtabbedPane.setSize( new Dimension( size.width, size.height));
    
    // create titled border if necessary (but not for tab entries)
    String title = getTitle();
    if ( title != null && title.length() > 0)
    {
      IXidget parent = xidget.getParent();
      if ( parent != null)
      {
        if ( parent.getConfig().isType( "form"))
        {
          jtabbedPane.setBorder( new TitledBorder( title));
        }
      }
    }
  }

  /**
   * Returns the title of the form.
   * @return Returns null or the title of the form.
   */
  private String getTitle()
  {
    IModelObject element = xidget.getConfig();
    IExpression titleExpr = Xlate.childGet( element, "title", Xlate.get( element, "title", (IExpression)null));
    if ( titleExpr != null) return titleExpr.evaluateString();
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidgets()
  {
    jtabbedPane.getParent().remove( jtabbedPane);
    jtabbedPane = null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jtabbedPane};
  }

  /**
   * Returns the JTabbedPane created for this form.
   * @return Returns the JTabbedPane created for this form.
   */
  public JTabbedPane getJTabbedPane()
  {
    return jtabbedPane;
  }

  private IXidget xidget;
  private JTabbedPane jtabbedPane;
}
