/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.calendar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.xidget.IXidget;
import org.xidget.chart.Plot;
import org.xidget.chart.Point;
import org.xidget.ifeature.chart.IPlotFeature;
import org.xidget.ifeature.model.ISingleValueUpdateFeature;
import org.xidget.ifeature.table.ITableWidgetFeature;

/**
 * A canvas which displays a calendar month.
 */
@SuppressWarnings({"serial", "unchecked"})
public class CalendarPanel extends JPanel implements IPlotFeature, ITableWidgetFeature
{
  public CalendarPanel( IXidget xidget)
  {
    this.xidget = xidget;
    this.glyphs = new GlyphVector[ 7][ 7];
    this.labels = new String[ 7][ 7];
    this.points = new List[ 7][ 7];
    this.time = System.currentTimeMillis();
    this.initHeader = true;
    this.initCells = true;
    this.initPoints = true;
    this.dayNames = new String[] { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};    
    this.plots = new ArrayList<Plot>();
    this.colors = new HashMap<String, Color>();
    this.showGrid = true;
    
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
    SwingUtilities.invokeLater( updateRunnable);
    
    initCells = true;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#setShowGrid(boolean)
   */
  @Override
  public void setShowGrid( boolean showGrid)
  {
    this.showGrid = showGrid;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#addPlot(org.xidget.chart.Plot)
   */
  @Override
  public void addPlot( Plot plot)
  {
    plots.add( plot);
    initPoints = true;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#removePlot(org.xidget.chart.Plot)
   */
  @Override
  public void removePlot( Plot plot)
  {
    plots.remove( plot);
    initPoints = true;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateForeground(org.xidget.chart.Plot, java.lang.String)
   */
  @Override
  public void updateForeground( Plot plot, String color)
  {
    initPoints = true;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateBackground(org.xidget.chart.Plot, java.lang.String)
   */
  @Override
  public void updateBackground( Plot plot, String color)
  {
    initPoints = true;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateStrokeWidth(org.xidget.chart.Plot, double)
   */
  @Override
  public void updateStrokeWidth( Plot plot, double value)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#addPoint(org.xidget.chart.Plot, int, org.xidget.chart.Point)
   */
  @Override
  public void addPoint( Plot plot, int index, Point point)
  {
    initPoints = true;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#removePoint(org.xidget.chart.Plot, int)
   */
  @Override
  public void removePoint( Plot plot, int index)
  {
    initPoints = true;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateCoords(org.xidget.chart.Point, double[])
   */
  @Override
  public void updateCoords( Point point, double[] coords)
  {
    initPoints = true;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateCoord(org.xidget.chart.Point, int, double)
   */
  @Override
  public void updateCoord( Point point, int coordinate, double value)
  {
    initPoints = true;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateLabel(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateLabel( Point point, String label)
  {
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateForeground(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateForeground( Point point, String fcolor)
  {
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateBackground(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateBackground( Point point, String bcolor)
  {
    repaint();
  }

  /**
   * Initialize the header text graphics by creating the appropriate GlyphVectors.
   * @param g The graphics context.
   */
  private final void initHeaderTextGraphics( Graphics2D g)
  {
    initHeader = false;
    
    FontRenderContext frc = g.getFontRenderContext();
    Font cellFont = getFont();
    Font headerFont = cellFont.deriveFont( cellFont.getSize2D() * 0.85f);
    System.out.println( headerFont);
    
    for( int i=0; i<glyphs.length; i++)
    {
      GlyphVector gv = headerFont.createGlyphVector( frc, dayNames[ i]);
      glyphs[ i][ 0] = gv;
      labels[ i][ 0] = dayNames[ i];
    }
    
    headerHeight = (float)glyphs[ 0][ 0].getVisualBounds().getHeight() + (headerPadY * 2);
  }
  
  /**
   * Initialize the cell text graphics by creating the appropriate GlyphVectors.
   * @param g The graphics context.
   */
  private final void initCellTextGraphics( Graphics2D g)
  {
    initCells = false;
    initPoints = false;
    
    FontRenderContext frc = g.getFontRenderContext();
    Font cellFont = getFont();
    
    for( int i=0; i<glyphs.length; i++)
      for( int j=1; j<glyphs[ i].length; j++)
      {
        glyphs[ i][ j] = null;
        points[ i][ j] = null;
      }
    
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis( time);
    
    int dayOfMonth = calendar.get( Calendar.DAY_OF_MONTH);
    
    // get day of week of first day of month
    calendar.set( Calendar.DAY_OF_MONTH, 1);
    int dayOfWeek = calendar.get( Calendar.DAY_OF_WEEK) - 1;
    
    // get last day of month
    calendar.add( Calendar.MONTH, 1);
    calendar.add( Calendar.DAY_OF_MONTH, -1);
    int lastDayOfMonth = calendar.get( Calendar.DAY_OF_MONTH);

    // prepare calendar for iteration through days
    calendar.setTimeInMillis( time);
    calendar.set( Calendar.DAY_OF_MONTH, 1);

    // create day labels and sort points into days
    Set<Point> taken = new HashSet<Point>();
    int i = dayOfWeek;
    int j = 1;
    long prevDay = 0;
    for( int k=1; k<=lastDayOfMonth; k++, i++)
    {
      if ( i > 6) { i = 0; j++;}
      if ( k == dayOfMonth) { iDay = i; jDay = j;}
      
      String label = Integer.toString( k);
      GlyphVector gv = cellFont.createGlyphVector( frc, label);
      glyphs[ i][ j] = gv;
      labels[ i][ j] = label;
      
      calendar.set( Calendar.DAY_OF_MONTH, k);
      long currDay = calendar.getTimeInMillis();

      for( Plot plot: plots)
      {
        for( int m=0; m<plot.getPoints().size(); m++)
        {
          Point point = plot.getPoints().get( m);
          if ( taken.contains( point)) continue;
          
          if ( point.coords != null && point.coords.length > 0)
          {
            double coord = point.coords[ 0];
            if ( coord >= prevDay && coord < currDay)
            {
              taken.add( point);
              if ( points[ i][ j] == null) points[ i][ j] = new ArrayList<Point>( 1);
              points[ i][ j].add( point);
            }
          }
        }
      }
      
      prevDay = currDay;
    }
  }
  
  /**
   * Initialize the point graphics.
   * @param g The graphics context.
   */
  private final void initPointGraphics( Graphics2D g)
  {
    initPoints = false;
    
    for( int i=0; i<points.length; i++)
      for( int j=1; j<points[ i].length; j++)
      {
        points[ i][ j] = null;
      }
    
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis( time);
    
    int dayOfMonth = calendar.get( Calendar.DAY_OF_MONTH);
    
    // get day of week of first day of month
    calendar.set( Calendar.DAY_OF_MONTH, 1);
    int dayOfWeek = calendar.get( Calendar.DAY_OF_WEEK) - 1;
    
    // get last day of month
    calendar.add( Calendar.MONTH, 1);
    calendar.add( Calendar.DAY_OF_MONTH, -1);
    int lastDayOfMonth = calendar.get( Calendar.DAY_OF_MONTH);

    // prepare calendar for iteration through days
    calendar.add( Calendar.MONTH, -1);

    // sort points into days
    Set<Point> taken = new HashSet<Point>();
    int i = dayOfWeek;
    int j = 1;
    long prevDay = 0;
    for( int k=1; k<=lastDayOfMonth; k++, i++)
    {
      if ( i > 6) { i = 0; j++;}
      if ( k == dayOfMonth) { iDay = i; jDay = j;}
      
      calendar.set( Calendar.DAY_OF_MONTH, k);
      long currDay = calendar.getTimeInMillis();

      for( Plot plot: plots)
      {
        for( int m=0; m<plot.getPoints().size(); m++)
        {
          Point point = plot.getPoints().get( m);
          if ( taken.contains( point)) continue;
          
          if ( point.coords != null && point.coords.length > 0)
          {
            double coord = point.coords[ 0];
            if ( coord >= prevDay && coord < currDay)
            {
              taken.add( point);
              if ( points[ i][ j] == null) points[ i][ j] = new ArrayList<Point>( 1);
              points[ i][ j].add( point);
            }
          }
        }
      }
      
      prevDay = currDay;
    }
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @Override
  protected void paintComponent( Graphics g)
  {
    super.paintComponent( g);

    Graphics2D g2d = (Graphics2D)g;
    if ( initHeader) initHeaderTextGraphics( g2d);
    if ( initCells) initCellTextGraphics( g2d);
    if ( initPoints) initPointGraphics( g2d);
    
    int width = getWidth();
    float height = getHeight() - headerHeight;
    float hh = headerHeight;
    float mhh = hh / 2;
    float dx = width / 7f;
    float dy = height / 5f;
    float mdx = dx / 2;
    float mdy = dy / 2;
    int rhh = (int)Math.round( hh);

    // draw header background
    g.setColor( getForeground());
    g.fillRect( 0, 0, width, rhh);
    
    // draw header text
    g.setColor( getBackground());
    float x = 0;
    float y = 0;
    for( int i=0; i<7; i++)
    {
      Rectangle2D bounds = glyphs[ i][ 0].getVisualBounds();
      float cx = (float)bounds.getCenterX();
      float cy = (float)bounds.getCenterY();
      g2d.drawGlyphVector( glyphs[ i][ 0], x + mdx - cx, y + mhh - cy);
      x += dx;
    }

    // draw cell text and background
    int i, j;
    for( x=0, i=0; i<7; x += dx, i++)
    {
      for( y=hh, j=1; j<7; y += dy, j++)
      {
        int rx1 = (int)Math.round( x);
        int ry1 = (int)Math.round( y);
        int rx2 = (int)Math.round( x + dx);
        int ry2 = (int)Math.round( y + dy);
        
        List<Point> pointList = points[ i][ j];
        
        if ( i == iDay && j == jDay) 
        {
          g.setColor( getForeground());
          g.fillRect( rx1 + 2, ry1 + 2, (rx2 - rx1 - 3), (ry2 - ry1 - 3));
          g.setColor( getBackground());
        }
        else if ( pointList != null)
        {
          Color color = colors.get( pointList.get( 0).bcolor);
          g.setColor( (color != null)? color: getForeground());
          g.fillRect( rx1 + 2, ry1 + 2, (rx2 - rx1 - 3), (ry2 - ry1 - 3));
          
          color = colors.get( pointList.get( 0).fcolor);
          g.setColor( (color != null)? color: getBackground());
        }
        else
        {
          g.setColor( getForeground());
        }

        GlyphVector gv = glyphs[ i][ j];
        if ( gv != null)
        {
          Rectangle2D bounds = gv.getVisualBounds();
          float cx = (float)bounds.getCenterX();
          float cy = (float)bounds.getCenterY();
          g2d.drawGlyphVector( gv, x + mdx - cx, y + mdy - cy);
        }
      }
    }
    
    if ( showGrid)
    {
      // draw vertical grid lines
      for( x=dx; x<width; x+=dx)
      {
        int rx1 = (int)Math.round( x);
        
        g.setColor( getBackground());
        g.drawLine( rx1, 0, rx1, rhh);
        
        g.setColor( getForeground());
        g.drawLine( rx1, rhh, rx1, getHeight());
      }
      
      // draw horizontal grid lines
      g.setColor( getForeground());
      for( y=dy+hh; y<getHeight(); y+=dy)
      {
        int ry1 = (int)Math.round( y);
        g.drawLine( 0, ry1, width, ry1);
      }
    }
  }
    
//  /**
//   * Returns the grid coordinate for the specified panel coordinate.
//   * @param x The x-coordinate in the panel.
//   * @return Returns the grid coordinate for the specified panel coordinate.
//   */
//  private int toGridX( int x)
//  {
//    int width = getWidth();
//    float dx = width / 7f;
//    return (int)Math.round( x / dx);
//  }
//  
//  /**
//   * Returns the grid coordinate for the specified panel coordinate.
//   * @param y The y-coordinate in the panel.
//   * @return Returns the grid coordinate for the specified panel coordinate.
//   */
//  private int toGridY( int y)
//  {
//    float hh = headerHeight;
//    float height = getHeight() - hh;
//    float dy = height / 5f;
//    return (int)Math.round( (y - hh) / dy);
//  }
  
  private final MouseListener mouseListener = new MouseAdapter() {
    public void mousePressed( MouseEvent event)
    {
      int x = event.getX();
      int y = event.getY();
      
      int width = getWidth();
      float hh = headerHeight;
      float height = getHeight() - hh;
      float dx = width / 7f;
      float dy = height / 5f;

      try
      {
        int i = (int)(x / dx);
        int j = (int)((y - hh) / dy);
        int dayOfMonth = Integer.parseInt( labels[ i][ j+1]);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis( time);
        calendar.set( Calendar.DAY_OF_MONTH, dayOfMonth);
        time = calendar.getTimeInMillis();
        
        repaint( (int)(iDay * dx), (int)((jDay-1) * dy + hh), (int)dx + 1, (int)dy + 1);
        iDay = i;
        jDay = j+1;
        repaint( (int)(iDay * dx), (int)((jDay-1) * dy + hh), (int)dx + 1, (int)dy + 1);
        
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

  private int headerPadY = 3;
  
  private IXidget xidget;
  private long time;
  private String[] dayNames;
  private boolean initHeader;
  private boolean initCells;
  private boolean initPoints;
  private String[][] labels;
  private GlyphVector[][] glyphs;
  private List<Point>[][] points;
  private float headerHeight;
  private int iDay;
  private int jDay;
  private boolean showGrid;
  private List<Plot> plots;
  private Map<String, Color> colors;
}
