/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.border.Border;
import org.xidget.IXidget;
import org.xidget.config.util.Quad;
import org.xidget.feature.ComputeNodeFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.ConstantNode;
import org.xidget.layout.ContainerHeightNode;
import org.xidget.layout.ContainerWidthNode;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.OffsetNode;
import org.xmodel.Xlate;

/**
 * An implementation of IComputeNodeFeature which takes into account a Swing component's border.
 */
public class BorderComputeNodeFeature extends ComputeNodeFeature
{
  public BorderComputeNodeFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IComputeNodeFeature#getParentAnchor(java.lang.String)
   */
  public IComputeNode getParentAnchor( String type)
  {
    IWidgetFeature widget = xidget.getFeature( IWidgetFeature.class);
    if ( widget == null) return null;
    
    IComputeNode node = nodes.get( "p"+type);
    if ( node != null) return node;

    Quad quad = new Quad( Xlate.get( xidget.getConfig(), "margins", (String)null), 0, 0, 0, 0);    
    JComponent component = xidget.getFeature( JComponent.class);
    if ( component != null)
    {
      Border border = component.getBorder();
      if ( border != null)
      {
        Insets insets = border.getBorderInsets( component);
        quad.a += insets.left;
        quad.b += insets.top;
        quad.c += insets.right;
        quad.d += insets.bottom;
      }
    }
    
    if ( quad == null) quad = new Quad( Xlate.get( xidget.getConfig(), "margins", (String)null), 0, 0, 0, 0);    
    
    if ( type.equals( "x0")) node = new ConstantNode( quad.a);
    else if ( type.equals( "x1")) node = new OffsetNode( new ContainerWidthNode( widget), -quad.c);
    else if ( type.equals( "y0")) node = new ConstantNode( quad.b);
    else if ( type.equals( "y1")) node = new OffsetNode( new ContainerHeightNode( widget), -quad.d);
    else if ( type.equals( "w")) node = new OffsetNode( new ContainerWidthNode( widget), -(quad.a + quad.c));
    else if ( type.equals( "h")) node = new OffsetNode( new ContainerHeightNode( widget), -(quad.b + quad.d));
    
    nodes.put( "p"+type, node);
    return node;    
  }
}
