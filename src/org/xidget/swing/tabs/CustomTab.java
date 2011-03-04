/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.tabs;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * A custom widget for use as a JTabbedPane tab. This widget supports an icon,
 * text, background gradient and close button similar to the SWT style close
 * button.
 */
public class CustomTab extends JPanel
{
  public CustomTab()
  {
    imageTextGap = 5;
    translate = new AffineTransform();
    closable = true;
  }
  
  /**
   * Set the title of the tab.
   * @param title The title.
   */
  public void setTitle( String title)
  {
    this.text = title;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent( Graphics g)
  {
    super.paintComponent( g);
    
    FontMetrics metrics = g.getFontMetrics();
    
    Insets insets = getInsets();
    int x = insets.left;
    int y = insets.top;
    int height = 10; // height of close button

    // find maximum height
    if ( text != null && text.length() > 0)
    {
      height = metrics.getHeight();
    }
    
    if ( image != null && image.getIconHeight() > height)
    {
      height = image.getIconHeight();
    }
    
    // draw tab border and background
    
    
    // draw image
    if ( image != null)
    {
      int iy = y + (height - image.getIconHeight()) / 2;
      g.drawImage( image.getImage(), x, iy, this);
      x += image.getIconWidth() + imageTextGap;
    }
    
    // draw text
    if ( text != null && text.length() > 0)
    {
      Rectangle2D textBounds = metrics.getStringBounds( text, g);
      int ty = (int)(y + (height - textBounds.getHeight()) / 2);
      g.drawString( text, x, ty + metrics.getMaxAscent());
      x += textBounds.getWidth() + imageTextGap;
    }
    
    // draw close button
    if ( closable)
    {
      Graphics2D g2d = (Graphics2D)g;
      AffineTransform transform = g2d.getTransform();
      
      int cy = (int)(y + (height - 10) / 2);
      translate.setToTranslation( x, cy);
      g2d.setTransform( translate);
      
      Color color = g.getColor();
      g.setColor( hovering? Color.red: Color.white);
      g.fillPolygon( closeButtonXs, closeButtonYs, closeButtonXs.length);
      g.setColor( color);
      g.drawPolygon( closeButtonXs, closeButtonYs, closeButtonXs.length);
      
      g2d.setTransform( transform);
    }
    
    
  }
  
  private final static int[] closeButtonXs = {
    0, 2, 4, 5, 7, 9,
    9, 7, 7, 9, 9,
    7, 5, 4, 2, 0, 
    0, 2, 2, 0, 0
  };
  
  private final static int[] closeButtonYs = {
    0, 0, 2, 2, 0, 0,
    2, 4, 5, 7, 9,
    9, 7, 7, 9, 9,
    7, 5, 4, 2, 0
  };
  
  private ImageIcon image;
  private String text;
  private boolean closable;
  private AffineTransform translate;
  private int imageTextGap;
  private boolean hovering;
}
