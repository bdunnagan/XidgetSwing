/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.tree;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;
import org.xidget.IXidget;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xidget.swing.tree.CustomTreeCellRenderer;
import org.xidget.swing.tree.CustomTreeModel;
import org.xidget.tree.Row;

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
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    jtree = new JTree( new CustomTreeModel( xidget));
    
    jtree.setCellRenderer( new CustomTreeCellRenderer());
    jtree.setShowsRootHandles( true);
    jtree.setRootVisible( false);
    jtree.putClientProperty( "JTree.lineStyle", "Angled");
    
    jtree.addTreeExpansionListener( expandListener);
    
    jscrollPane = new JScrollPane( jtree);
    return jscrollPane;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jscrollPane, jtree};
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

  private TreeExpansionListener expandListener = new TreeExpansionListener() {
    public void treeExpanded( TreeExpansionEvent event)
    {
      TreePath path = event.getPath();
      Row row = (Row)path.getLastPathComponent();

      ITreeExpandFeature feature = xidget.getFeature( ITreeExpandFeature.class);
      feature.expand( row);
    }
    
    public void treeCollapsed( TreeExpansionEvent event)
    {
      TreePath path = event.getPath();
      Row row = (Row)path.getLastPathComponent();

      ITreeExpandFeature feature = xidget.getFeature( ITreeExpandFeature.class);
      feature.collapse( row);
    }
  };
  
  private JScrollPane jscrollPane;
  private JTree jtree;
}
