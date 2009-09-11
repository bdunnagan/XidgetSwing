/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.util.Collections;
import java.util.List;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.xidget.IXidget;
import org.xidget.ifeature.IDynamicContainerFeature;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.layout.Size;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

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
    jtabbedPane.addChangeListener( selectionListener);

    // create titled border if necessary (but not for tab entries)
    IXidget parent = xidget.getParent();
    String title = getTitle();
    if ( title != null && title.length() > 0 && parent != null)
    {
      if ( !parent.getConfig().isType( "tabs"))
        jtabbedPane.setBorder( new TitledBorder( title));
    }

    // add panel to parent container
    IWidgetContainerFeature containerFeature = xidget.getParent().getFeature( IWidgetContainerFeature.class);
    if ( containerFeature != null) containerFeature.addWidget( xidget);
    
    // optionally set the width and height nodes in case children are dependent on them
    IModelObject config = xidget.getConfig();
    Size size = new Size( Xlate.get( config, "size", (String)null), -1, -1);
    if ( size.width >= 0 || size.height >= 0) jtabbedPane.setSize( size.width, size.height); 
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

  private ChangeListener selectionListener = new ChangeListener() {
    public void stateChanged( ChangeEvent e)
    {
      IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
      StatefulContext context = contextFeature.getContext( jtabbedPane);
      
      IDynamicContainerFeature dynamicContainerFeature = xidget.getFeature( IDynamicContainerFeature.class);
      List<IModelObject> children = dynamicContainerFeature.getChildren();
      
      int index = jtabbedPane.getSelectedIndex();
      if ( index >= 0 && index < children.size())
      {
        ISelectionModelFeature feature = xidget.getFeature( ISelectionModelFeature.class);
        feature.setSelection( context, Collections.singletonList( children.get( index)));
      }
    }
  };
  
  private IXidget xidget;
  private JTabbedPane jtabbedPane;
}
