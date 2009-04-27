/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.tree;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import org.xidget.IXidget;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xidget.swing.tree.CustomTreeCellRenderer;
import org.xidget.swing.tree.CustomTreeModel;

/**
 * An implementation of IWidgetCreationFeature for creating a Netbeans Outline widget.
 */
public class JTreeWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public JTreeWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget(java.awt.Container)
   */
  @Override
  protected JComponent createSwingWidget( Container container)
  {
    jtree = new JTree( new CustomTreeModel());
    jtree.setCellRenderer( new CustomTreeCellRenderer());
    jtree.setShowsRootHandles( true);
    jtree.setRootVisible( false);
    jtree.putClientProperty( "JTree.lineStyle", "Angled");
    
    jscrollPane = new JScrollPane( jtree);
    container.add( jscrollPane);
    
    return jtree;
  }
  
  /**
   * Returns the scroll pane that holds the table.
   * @return Returns the scroll pane that holds the table.
   */
  public JScrollPane getJScrollPane()
  {
    return jscrollPane;
  }

  /**
   * Returns the Outline widget.
   * @return Returns the Outline widget.
   */
  public JTree getJTree()
  {
    return jtree;
  }

  private JScrollPane jscrollPane;
  private JTree jtree;
}
