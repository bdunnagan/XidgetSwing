/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.tabs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.swing.util.IconCache;

/**
 * A custom widget for use as a JTabbedPane tab. This widget supports an icon,
 * text, background gradient and close button similar to the SWT style close
 * button.
 */
@SuppressWarnings("serial")
public class CustomTab extends JPanel
{
  public CustomTab( IXidget xidget)
  {
    this.xidget = xidget;
    this.placement = -1;
    
    setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));
    setOpaque( false);
    
    // official swing hack for center tab content
    setBorder( BorderFactory.createEmptyBorder( 2, 0, 0, 0));    
    
    label = new JLabel();
    add( label);
    
    closeButton = new CloseButton();
    add( closeButton);
  }
  
  /**
   * Set the tab image.
   * @param image The image.
   */
  public void setImage( Image image)
  {
    label.setIcon( IconCache.getInstance().getIcon( image));
  }
  
  /**
   * Set the tab image.
   * @param icon The image.
   */
  public void setIcon( ImageIcon icon)
  {
    label.setIcon( icon);
  }
  
  /**
   * Set the title of the tab.
   * @param title The title.
   */
  public void setTitle( String title)
  {
    label.setText( title);
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintChildren(java.awt.Graphics)
   */
  @Override
  protected void paintChildren( Graphics g)
  {
    Graphics2D g2d = (Graphics2D)g;
    AffineTransform transform = g2d.getTransform(); 
    
    if ( getPlacement() == JTabbedPane.LEFT)
    {
      if ( rotate == null) rotate = AffineTransform.getRotateInstance( -Math.PI / 2);
      AffineTransform t = new AffineTransform( transform);
      t.translate( 0, getHeight());
      t.concatenate( rotate);
      g2d.setTransform( t);
    }
    else if ( getPlacement() == JTabbedPane.RIGHT)
    {
      if ( rotate == null) rotate = AffineTransform.getRotateInstance( Math.PI / 2);
      AffineTransform t = new AffineTransform( transform);
      t.translate( getWidth(), 0);
      t.concatenate( rotate);
      g2d.setTransform( t);
    }
    
    super.paintChildren( g);
    
    g2d.setTransform( transform);
  }

  /* (non-Javadoc)
   * @see javax.swing.JComponent#getPreferredSize()
   */
  @Override
  public Dimension getPreferredSize()
  {
    Dimension size = super.getPreferredSize();
    if ( !isHorizontal())
    {
      int width = size.width;
      size.width = size.height;
      size.height = width;
    }
    return size;
  }
  
  /**
   * @return Returns the Swing tab placement constant.
   */
  private int getPlacement()
  {
    if ( placement == -1) placement = JTabbedPaneWidgetCreationFeature.getTabPlacement( xidget.getParent());
    return placement;
  }
  
  /**
   * @return Returns true if the tab placement is horizontal.
   */
  private boolean isHorizontal()
  {
    return placement == JTabbedPane.TOP || placement == JTabbedPane.BOTTOM;
  }
  
  /**
   * Set whether the tab has a close button.
   * @param hasCloseButton True if the tab has a close button.
   */
  public void setCloseButton( boolean button)
  {
    closeButton.setVisible( button);
  }

  private class CloseButton extends JButton implements ActionListener
  {
    public CloseButton()
    {
      setBorder( BorderFactory.createEmptyBorder());
      addActionListener( this);
      addMouseListener( mouseListener);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize()
    {
      return new Dimension( 10, 10);
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent( Graphics g)
    {
      super.paintComponent( g);
      
      Color color = g.getColor();
      g.setColor( hovering? Color.pink: Color.white);
      g.fillPolygon( closeButtonXs, closeButtonYs, closeButtonXs.length);
      g.setColor( color);
      g.drawPolygon( closeButtonXs, closeButtonYs, closeButtonXs.length);
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed( ActionEvent e)
    {
      Creator.getInstance().destroy( xidget);
    }
    
    private final MouseListener mouseListener = new MouseAdapter() {
      public void mouseEntered( MouseEvent e)
      {
        super.mouseEntered( e);
        hovering = true;
        repaint();
      }
      public void mouseExited( MouseEvent e)
      {
        super.mouseExited( e);
        hovering = false;
        repaint();
      }
    };
  }
  
  private final static int[] closeButtonXs = { 0, 2, 4, 5, 7, 9, 9, 7, 7, 9, 9, 7, 5, 4, 2, 0, 0, 2, 2, 0, 0};
  private final static int[] closeButtonYs = { 0, 0, 2, 2, 0, 0, 2, 4, 5, 7, 9, 9, 7, 7, 9, 9, 7, 5, 4, 2, 0};

  private IXidget xidget;
  private boolean hovering;
  private JLabel label;
  private CloseButton closeButton;
  private AffineTransform rotate;
  private int placement;
}
