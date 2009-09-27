/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import org.xidget.IXidget;
import org.xidget.ifeature.IErrorFeature;

/**
 * An IErrorAdapter which displays the error just below the widget.
 */
public class AdjacentErrorFeature implements IErrorFeature
{
  public AdjacentErrorFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.adapter.IErrorAdapter#structureError(java.lang.String)
   */
  public void structureError( String message)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.adapter.IErrorAdapter#valueError(java.lang.String)
   */
  public void valueError( String message)
  {
    JComponent widget = xidget.getFeature( JComponent.class);
    if ( message == null || message.length() == 0)
    {
      if ( label != null) label.setVisible( false); 
    }
    else
    {
      if ( label == null)
      {
        label = new JLabel();
        label.setForeground( new Color( 200, 60, 60));
        label.setBorder( new LineBorder( label.getBackground().darker(), 1, false));
        label.setHorizontalAlignment( SwingConstants.CENTER);
        label.setOpaque( true);
        label.setText( message);
        
        Rectangle bounds = widget.getBounds();
        int x = bounds.x;
        int y = bounds.y + bounds.height;
        int h = label.getPreferredSize().height;
        if ( y + h > widget.getParent().getHeight())
          y = bounds.y - h;
        
        label.setLocation( x+5, y);
        label.setSize( bounds.width-10, label.getPreferredSize().height);
        
        widget.getParent().add( label);
        widget.getParent().setComponentZOrder( label, 0);
        widget.getParent().repaint();
      }
      else
      {
        label.setText( message);
        label.setVisible( true);
      }
    }
  }
  
  private IXidget xidget;
  private JLabel label;
}
