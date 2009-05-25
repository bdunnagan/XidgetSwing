/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.tree;

import javax.swing.JComponent;
import javax.swing.JTree;
import org.xidget.IFeatured;
import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.ComputeNodeFeature;
import org.xidget.feature.SelectionModelFeature;
import org.xidget.feature.tree.TreeExpandFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.swing.feature.BasicFeatureSet;
import org.xidget.swing.feature.SwingWidgetFeature;
import org.xidget.swing.feature.tree.JTreeWidgetCreationFeature;
import org.xidget.swing.feature.tree.JTreeWidgetFeature;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A tree xidget which uses the Swing JTree class.
 */
public class JTreeXidget extends Xidget
{
  /* (non-Javadoc)
   * @see org.xidget.Xidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    bindFeature = new BindFeature( this, new String[] { "tree"});
    creationFeature = new JTreeWidgetCreationFeature( this);
    widgetFeature = new SwingWidgetFeature( this);
    expandFeature = new TreeExpandFeature( this);
    treeWidgetFeature = new JTreeWidgetFeature( this);
    computeNodeFeature = new ComputeNodeFeature( this);    
    basicFeatureSet = new BasicFeatureSet( this);
  }

  /* (non-Javadoc)
   * @see org.xidget.Xidget#createFeatures(org.xmodel.xpath.expression.StatefulContext)
   */
  @Override
  public IFeatured createFeatures( StatefulContext context)
  {
    return new ContextFeatures( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IBindFeature.class) return (T)bindFeature;
    if ( clss == ITreeExpandFeature.class) return (T)expandFeature;
    if ( clss == ITreeWidgetFeature.class) return (T)treeWidgetFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)creationFeature;
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == IComputeNodeFeature.class) return (T)computeNodeFeature;
    
    if ( clss == JComponent.class) return (T)creationFeature.getJScrollPane();
    if ( clss == JTree.class) return (T)creationFeature.getJTree();
    
    T feature = basicFeatureSet.getFeature( clss);
    if ( feature != null) return feature;
    
    return super.getFeature( clss);
  }  
  
  private class ContextFeatures implements IFeatured
  {
    public ContextFeatures( StatefulContext context)
    {
      selectionModelFeature = new SelectionModelFeature( JTreeXidget.this, context); 
    }

    /* (non-Javadoc)
     * @see org.xidget.IFeatured#getFeature(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T> T getFeature( Class<T> clss)
    {
      if ( clss == ISelectionModelFeature.class) return (T)selectionModelFeature;
      return null;
    }
    
    private ISelectionModelFeature selectionModelFeature;
  }
  
  private IBindFeature bindFeature;
  private ITreeExpandFeature expandFeature;
  private ITreeWidgetFeature treeWidgetFeature;
  private JTreeWidgetCreationFeature creationFeature;
  private IWidgetFeature widgetFeature;
  private IComputeNodeFeature computeNodeFeature;
  private IFeatured basicFeatureSet;
}
