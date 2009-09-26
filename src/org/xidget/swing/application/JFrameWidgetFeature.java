/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JFrame;
import org.xidget.IXidget;
import org.xidget.ifeature.ITitleFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.Bounds;
import org.xidget.layout.Margins;
import org.xidget.layout.Size;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetFeature for JFrame. This class is similar to SwingWidgetFeature
 * but since the latter class depends on the widget being a JComponent, it cannot be used with
 * a JFrame. This class operates on a JFrame instance explicitly. In other words, this class
 * was added so that the other xidgets don't have to export a Component.class.
 */
public class JFrameWidgetFeature implements IWidgetFeature, ITitleFeature
{
  public JFrameWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITitleFeature#setTitle(java.lang.String)
   */
  public void setTitle( String title)
  {
    JFrame widget = xidget.getFeature( JFrame.class);
    widget.setTitle( title);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setBounds(float, float, float, float)
   */
  public void setBounds( float x, float y, float width, float height)
  {
    JFrame widget = xidget.getFeature( JFrame.class);
    widget.setBounds( (int)Math.round( x), (int)Math.round( y), (int)Math.round( width), (int)Math.round( height));
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetFeature#getBounds(org.xidget.feature.IWidgetFeature.Bounds)
   */
  public void getBounds( Bounds result)
  {
    JFrame widget = xidget.getFeature( JFrame.class);
    Rectangle rectangle = widget.getBounds();
    result.x = rectangle.x;
    result.y = rectangle.y;
    result.width = rectangle.width;
    result.height = rectangle.height;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getPreferredSize(org.xidget.layout.Size)
   */
  public void getPreferredSize( Size result)
  {
    JFrame widget = xidget.getFeature( JFrame.class);
    Dimension size = widget.getPreferredSize();
    result.width = size.width;
    result.height = size.height;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getOutsideMargins()
   */
  public Margins getOutsideMargins()
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetFeature#setVisible(boolean)
   */
  public void setVisible( boolean visible)
  {
    JFrame widget = xidget.getFeature( JFrame.class);
    if ( visible)
    {
      // size to fit first
      widget.pack();
      
      // set size if requested
      IModelObject config = xidget.getConfig();
      Dimension oldSize = widget.getSize();
      Size newSize = new Size( Xlate.get( config, "size", (String)null), -1, -1);
      if ( newSize.width < 0) newSize.width = oldSize.width;
      if ( newSize.height < 0) newSize.height = oldSize.height; 
      widget.setSize( newSize.width, newSize.height);
      
      // show
      widget.setVisible( true);
    }
    else
    {
      widget.setVisible( false);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setEnabled(boolean)
   */
  public void setEnabled( boolean enabled)
  {
    JFrame widget = xidget.getFeature( JFrame.class);
    widget.setEnabled( enabled);
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setTooltip(java.lang.String)
   */
  public void setTooltip( String tooltip)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setBackground(int)
   */
  public void setBackground( int color)
  {
    JFrame widget = xidget.getFeature( JFrame.class);
    widget.setBackground( new Color( color));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setForeground(int)
   */
  public void setForeground( int color)
  {
    JFrame widget = xidget.getFeature( JFrame.class);
    widget.setForeground( new Color( color));
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return xidget.toString();
  }

  private IXidget xidget;
}
