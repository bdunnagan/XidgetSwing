/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.xidget.IXidget;
import org.xidget.ifeature.IIconFeature;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.IScriptFeature;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IWidgetCreationFeature for the Swing JMenuItem widget.
 */
public class JMenuItemWidgetCreationFeature extends SwingWidgetCreationFeature implements IIconFeature, ILabelFeature
{
  public JMenuItemWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    String spec = Xlate.get( xidget.getConfig(), "type", "push");
    if ( !spec.equals( "push")) throw new UnsupportedOperationException( "Only push menu items are currently supported.");

    IModelObject config = xidget.getConfig();
    if ( config.getNumberOfChildren() == 0 && config.getAttributeNames().size() < 2)
    {
      jMenuItem = new JMenu();
    }
    else
    {
      jMenuItem = new JMenuItem( "Fuck");
      jMenuItem.addActionListener( actionListener);
    }
    
    return jMenuItem;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jMenuItem};
  }
  
  /**
   * Returns the JMenuItem that was created.
   * @return Returns the JMenuItem that was created.
   */
  public JMenuItem getJMenuItem()
  {
    return jMenuItem;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IIconFeature#setIcon(java.lang.Object)
   */
  public void setIcon( Object icon)
  {
    jMenuItem.setIcon( (Icon)icon);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#setText(java.lang.String)
   */
  public void setText( String text)
  {
    jMenuItem.setText( text);
  }

  private ActionListener actionListener = new ActionListener() {
    public void actionPerformed( ActionEvent e)
    {
      IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
      if ( scriptFeature != null) 
      {
        IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
        StatefulContext context = contextFeature.getContext( e.getSource());
        if ( context != null) scriptFeature.runScript( "onPress", context);
      }
    }
  };
  
  private JMenuItem jMenuItem;
}
