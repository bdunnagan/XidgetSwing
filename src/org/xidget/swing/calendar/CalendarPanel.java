/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.calendar;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.xidget.IXidget;
import org.xidget.ifeature.model.ISingleValueUpdateFeature;

/**
 * A canvas which displays a calendar month.
 */
@SuppressWarnings("serial")
public class CalendarPanel extends JPanel
{
  public CalendarPanel( IXidget xidget)
  {
    this.xidget = xidget;
    this.labels = new String[ 7][ 7];

    String[] days = { "S", "M", "T", "W", "T", "F", "S"};
    for( int i=0; i<labels.length; i++)
      labels[ i][ 0] = days[ i];
    
    for( int i=0; i<labels.length; i++)
      for( int j=1; j<labels[ i].length; j++)
        labels[ i][ j] = "";
    
    setTime( System.currentTimeMillis());
    
    setBackground( Color.WHITE);
    addMouseListener( mouseListener);
    
    setFocusable( true);
  }

  /**
   * Returns the currently selected date.
   * @return Returns the currently selected date.
   */
  public long getTime()
  {
    return time;
  }

  /**
   * Set the current date.
   * @param time The time in milliseconds since the epoch.
   */
  public void setTime( long time)
  {
    this.time = time;
    updateDisplay();
    SwingUtilities.invokeLater( updateRunnable);
  }

  /**
   * Create calendar labels.
   */
  private void updateDisplay()
  {
    for( int i=0; i<labels.length; i++)
      for( int j=1; j<labels[ i].length; j++)
        labels[ i][ j] = "";
    
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis( time);
    
    int dayOfMonth = calendar.get( Calendar.DAY_OF_MONTH);
    
    calendar.set( Calendar.DAY_OF_MONTH, 1);
    int dayOfWeek = calendar.get( Calendar.DAY_OF_WEEK) - 1;
    
    calendar.add( Calendar.MONTH, 1);
    calendar.add( Calendar.DAY_OF_MONTH, -1);
    int lastDayOfMonth = calendar.get( Calendar.DAY_OF_MONTH);
    
    int i = dayOfWeek;
    int j = 1;
    for( int k=1; k<=lastDayOfMonth; k++, i++)
    {
      if ( i > 6) { i = 0; j++;}
      if ( k == dayOfMonth) { iDay = i; jDay = j;}
      labels[ i][ j] = ""+k;
    }
    
    repaint();
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent( Graphics g)
  {
    super.paintComponent( g);
    
    FontMetrics metrics = g.getFontMetrics();
    
    int width = getWidth();
    int height = getHeight();
    double dx = width / 7d;
    double dy = height / 7d;
    double hdx = dx / 2;
    double hdy = dy / 2;
    
    int rdy = (int)Math.round( dy);

    g.setColor( Color.LIGHT_GRAY);
    g.fillRect( 0, 0, width, rdy);
    g.setColor( Color.WHITE);
    
    double x = 0;
    double y = 0;
    for( int i=0; i<7; i++)
    {
      Rectangle2D bounds = metrics.getStringBounds( labels[ i][ 0], g);
      double cx = bounds.getX() + bounds.getWidth() / 2d;
      double cy = bounds.getY() + bounds.getHeight() / 2d;
      
      g.drawString( labels[ i][ 0], (int)Math.round( x + hdx - cx), (int)(y + hdy - cy));
      x += dx;
    }

    int i, j;
    for( x=0, i=0; i < 7; x += dx, i++)
      for( y=dy, j=1; j < 7; y += dy, j++)
      {
        if ( i == iDay && j == jDay) 
        {
          g.setColor( Color.LIGHT_GRAY);
          g.fillRect( (int)x, (int)y, (int)dx+1, (int)dy+1);
          g.setColor( Color.WHITE);
        }
        else
        {
          g.setColor( Color.BLACK);
        }
       
        Rectangle2D bounds = metrics.getStringBounds( labels[ i][ j], g);
        double cx = bounds.getX() + bounds.getWidth() / 2d;
        double cy = bounds.getY() + bounds.getHeight() / 2d;
        
        g.drawString( labels[ i][ j], (int)(x + hdx - cx), (int)(y + hdy - cy));
      }
  }
  
  private final MouseListener mouseListener = new MouseAdapter() {
    public void mousePressed( MouseEvent event)
    {
      int x = event.getX();
      int y = event.getY();
      
      int width = getWidth();
      int height = getHeight();
      double dx = width / 7d;
      double dy = height / 7d;

      try
      {
        int i = (int)(x / dx);
        int j = (int)(y / dy);
        int dayOfMonth = Integer.parseInt( labels[ i][ j]);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( time);
        calendar.set( Calendar.DAY_OF_MONTH, dayOfMonth);
        time = calendar.getTimeInMillis();
        
        repaint( (int)(iDay * dx), (int)(jDay * dy), (int)dx + 1, (int)dy + 1);
        iDay = i;
        jDay = j;
        repaint( (int)(i * dx), (int)(j * dy), (int)dx + 1, (int)dy + 1);
        
        SwingUtilities.invokeLater( updateRunnable);
      }
      catch( Exception e)
      {
      }
    }
  };
  
  private final Runnable updateRunnable = new Runnable() {
    public void run()
    {
      ISingleValueUpdateFeature feature = xidget.getFeature( ISingleValueUpdateFeature.class);
      feature.updateModel();
    }
  };
  
  private IXidget xidget;
  private long time;
  private String[][] labels;
  private int iDay;
  private int jDay;
}
