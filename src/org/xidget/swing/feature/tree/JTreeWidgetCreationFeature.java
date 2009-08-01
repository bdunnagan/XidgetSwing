/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.tree;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.xidget.IXidget;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xidget.swing.tree.CustomTreeCellRenderer;
import org.xidget.swing.tree.CustomTreeModel;
import org.xidget.tree.Row;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IWidgetCreationFeature for creating a Swing JTree widget.
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
    
    if ( xidget.getFeature( ISelectionModelFeature.class) != null)
      jtree.addTreeSelectionListener( selectionListener);
    
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
   * Returns the JTree widget.
   * @return Returns the JTree widget.
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
  
  private TreeSelectionListener selectionListener = new TreeSelectionListener() {
    public void valueChanged( TreeSelectionEvent event)
    {
      if ( updating) return;
      updating = true;
      
      try
      {
        IWidgetContextFeature widgetContextFeature = xidget.getFeature( IWidgetContextFeature.class);
        StatefulContext context = widgetContextFeature.getContext( event.getSource());

        // TODO: tree selection is not ordered and causes unnecessary change records
        JTree jtree = (JTree)event.getSource();
        TreePath[] paths = jtree.getSelectionPaths();
        
        List<IModelObject> elements = new ArrayList<IModelObject>();
        if ( paths != null)
        {
          for( int i=0; i<paths.length; i++)
          {
            Row row = (Row)paths[ i].getLastPathComponent();
            elements.add( row.getContext().getObject());
          }
        }
        
        ISelectionModelFeature selectionModelFeature = xidget.getFeature( ISelectionModelFeature.class);
        selectionModelFeature.setSelection( context, elements);
      }
      finally
      {
        updating = false;
      }
    }
    
    private boolean updating;
  };
  
  private JScrollPane jscrollPane;
  private JTree jtree;
}
