/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.line;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import org.xidget.Creator;
import org.xidget.chart.IScale;
import org.xidget.chart.IScale.Tick;
import org.xidget.chart.Plot;
import org.xidget.chart.Point;
import org.xidget.ifeature.IColorFeature;
import org.xidget.ifeature.chart.IPlotFeature;
import org.xidget.swing.Toolkit;

/**
 * A custom widget that plots points on a two dimensional graph.  
 */
@SuppressWarnings("serial")
public class LineChart extends JPanel implements IPlotFeature
{
  public LineChart()
  {
    plots = new ArrayList<Plot>();
    plotArea = new Path2D.Double();    
    plotLine = new Path2D.Double();
    pointBox = new Path2D.Double();
    strokes = new HashMap<Plot, Stroke>();
    
    pointBoxStroke = new BasicStroke( 2);
    pointBoxFillColor = Color.white;
    pointBoxDrawColor = Color.gray;
    
    addMouseListener( mouseListener);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#addPlot(org.xidget.chart.Plot)
   */
  @Override
  public void addPlot( Plot plot)
  {
    plots.add( plot);
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#removePlot(org.xidget.chart.Plot)
   */
  @Override
  public void removePlot( Plot plot)
  {
    plots.remove( plot);
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateForeground(org.xidget.chart.Plot, java.lang.String)
   */
  @Override
  public void updateForeground( Plot plot, String color)
  {
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateBackground(org.xidget.chart.Plot, java.lang.String)
   */
  @Override
  public void updateBackground( Plot plot, String color)
  {
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateStrokeWidth(org.xidget.chart.Plot, double)
   */
  @Override
  public void updateStrokeWidth( Plot plot, double value)
  {
    BasicStroke stroke = new BasicStroke( (float)value, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
    strokes.put( plot, stroke);
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#addPoint(org.xidget.chart.Plot, int, org.xidget.chart.Point)
   */
  @Override
  public void addPoint( Plot plot, int index, Point point)
  {
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#removePoint(org.xidget.chart.Plot, int)
   */
  @Override
  public void removePoint( Plot plot, int index)
  {
    repaint( plot.getPoints().get( index));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateCoords(org.xidget.chart.Point, double[])
   */
  @Override
  public void updateCoords( Point point, double[] coords)
  {
    if ( point.coords != null) repaint( point);
    point.coords = coords;
    if ( point.coords != null) repaint( point);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateCoord(org.xidget.chart.Point, int, double)
   */
  @Override
  public void updateCoord( Point point, int coordinate, double value)
  {
    repaint( point);
    point.coords[ coordinate] = value;
    repaint( point);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateLabel(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateLabel( Point point, String label)
  {
    repaint( point);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateForeground(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateForeground( Point point, String fcolor)
  {
    repaint( point);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.chart.IPlotFeature#updateBackground(org.xidget.chart.Point, java.lang.String)
   */
  @Override
  public void updateBackground( Point point, String bcolor)
  {
    repaint( point);
  }
  
  /**
   * Repaint the specified point and connecting lines.
   * @param point The point to repaint.
   */
  private void repaint( Point point)
  {
    repaint();
//    if ( xAxis == null || yAxis == null) return;
//    
//    Scale xScale = xAxis.getScale();
//    Scale yScale = yAxis.getScale();
//    if ( xScale == null || yScale == null) return;
//    
//    int width = getWidth() - 1;
//    int height = getHeight() - 1;
//    
//    // repaint point
//    int x1 = (int)(xScale.plot( point.coords[ 0]) * width);
//    int y1 = (int)(yScale.plot( point.coords[ 1]) * height);
//    repaint( x1-5, y1-5, 10, 10);
//    
//    // repaint line to the new point
//    if ( point.prev != null)
//    {
//      int x0 = (int)(xScale.plot( point.prev.coords[ 0]) * width);
//      int y0 = (int)(yScale.plot( point.prev.coords[ 1]) * height);
//      repaint( x0, y0, (x1-x0)+2, (y1-y0)+2);
//    }
//    
//    // repaint line from the new point
//    if ( point.next != null)
//    {
//      int x0 = (int)(xScale.plot( point.next.coords[ 0]) * width);
//      int y0 = (int)(yScale.plot( point.next.coords[ 1]) * height);
//      repaint( x1, y1, (x0-x1)+2, (y0-y1)+2);
//    }
  }
  
  /**
   * Add an axis to the graph.
   * @param name The name.
   * @param axis The axis widget.
   */
  public void addAxis( String name, Axis axis)
  {
    if ( name.equals( "horizontal")) xAxis = axis;
    if ( name.equals( "vertical")) yAxis = axis;
  }
  
  /* (non-Javadoc)
   * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
   */
  @SuppressWarnings("unchecked")
  @Override
  protected void paintComponent( Graphics g) 
  {
    super.paintComponent( g);
    
    if ( xAxis == null || yAxis == null) return;
    
    Graphics2D g2d = (Graphics2D)g;
    g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

    int width = getWidth() - 1 - padWidth;
    int height = getHeight() - 1 - padHeight;
    
    AffineTransform shift = AffineTransform.getTranslateInstance( padWidth / 2f, padHeight / 2f);
    AffineTransform transform = g2d.getTransform();
    if ( transform != null) transform.concatenate( shift); else transform = shift;
    g2d.setTransform( transform);
    
    Toolkit toolkit = (Toolkit)Creator.getToolkit();
    IColorFeature<Color, Graphics2D> colorFeature = toolkit.getFeature( IColorFeature.class);
    
    // draw gradient background
//    GradientPaint paint = new GradientPaint( 0, 0, Color.white, 0, height, Color.lightGray);
//    g2d.setPaint( paint);
//    g2d.fillRect( 0, 0, width, height);
    
    // set grid color
    g2d.setColor( getForeground());
    
    // draw vertical grid-lines
    IScale xscale = xAxis.getScale();
    if ( xscale == null) return;
    
    for( Tick tick: xscale.getTicks())
    {
      int x = (int)Math.round( xscale.plot( tick.value) * width);
      g2d.drawLine( x, 0, x, height);
    }
    
    // draw horizontal grid-lines
    IScale yscale = yAxis.getScale();
    if ( yscale == null) return;
    
    for( Tick tick: yscale.getTicks())
    {
      int y = (int)Math.round( yscale.plot( tick.value) * height);
      g2d.drawLine( 0, y, width, y);
    }
    
    // compute graph shape
    double x = 0;
    double y = 0;
    for( Plot plot: plots)
    {
      plotLine.reset();
      plotArea.reset();
      
      List<Point> points = plot.getPoints();
      for( int i=0; i<points.size(); i++)
      {
        Point point = points.get( i);
        
        x = xscale.plot( point.coords[ 0]) * width;
        y = height - yscale.plot( point.coords[ 1]) * height;

        if ( i == 0)
        {
          plotLine.moveTo( x, y);
          plotArea.moveTo( x, height);
          plotArea.lineTo( x, y);
        }
        else
        {
          plotLine.lineTo( x, y);
          plotArea.lineTo( x, y);
        }
      }
    
      plotArea.lineTo( x, height);
      plotArea.closePath();
      
      g2d.setColor( getBackground());
      
      colorFeature.applyColor( plot.getBackground(), g2d, width, height);
      g2d.fill( plotArea);

      Stroke stroke = strokes.get( plot);
      if ( stroke != null) g2d.setStroke( stroke);
      colorFeature.applyColor( plot.getForeground(), g2d, width, height);
      g2d.draw( plotLine);
    }
    
    if ( selectedPoint != null) paintPointBox( g2d);
  }
  
  /**
   * Paint the point box for selected point.
   * @param g2d The graphics context.
   */
  @SuppressWarnings("unchecked")
  private void paintPointBox( Graphics2D g2d)
  {
    if ( selectedPoint == null || selectedPoint.label == null) return;
    
    if ( xAxis == null || yAxis == null) return;
    
    IScale xscale = xAxis.getScale();
    IScale yscale = yAxis.getScale();
    if ( xscale == null || yscale == null) return;
    
    if ( pointBoxText == null)
    {
      FontRenderContext renderContext = g2d.getFontRenderContext();
      pointBoxText = new TextLayout( selectedPoint.label, getFont(), renderContext);
    }
    
    int width = getWidth() - 1;
    int height = getHeight() - 1;
    
    double tw = pointBoxTabWidth;
    double th = pointBoxTabHeight;
    
    Rectangle2D bounds = pointBoxText.getBounds();
    double lw = Math.ceil( bounds.getWidth() + (pointBoxInset * 2));
    double lh = Math.ceil( bounds.getHeight() + (pointBoxInset * 2));
    
    double x = xscale.plot( selectedPoint.coords[ 0]) * width - selectedPlot.getStrokeWidth() - 1;
    double y = height - yscale.plot( selectedPoint.coords[ 1]) * height;
    
    pointBox.reset();
    pointBox.moveTo( x, y);
    x += tw / 2; y -= th;  pointBox.lineTo( x, y);
    x += lw - tw; pointBox.lineTo( x, y);
    y -= lh; pointBox.lineTo( x, y);
    x -= lw; pointBox.lineTo( x, y);
    y += lh; pointBox.lineTo( x, y);
    pointBox.closePath();
    
    // make (x, y) point to the center of the box
    y -= lh;

    // calculate text position
    x += pointBoxInset - bounds.getX();
    y += pointBoxInset - bounds.getY();

    Toolkit toolkit = (Toolkit)Creator.getToolkit();
    IColorFeature<Color, Graphics2D> colorFeature = toolkit.getFeature( IColorFeature.class);
    
    
    Rectangle pointBoxBounds = pointBox.getBounds();
    int pbw = (int)pointBoxBounds.getWidth();
    int pbh = (int)pointBoxBounds.getHeight();
    
    g2d.setColor( pointBoxFillColor);
    colorFeature.applyColor( selectedPoint.bcolor, g2d, pbw, pbh);
    g2d.fill( pointBox);
    
    g2d.setColor( pointBoxDrawColor);
    colorFeature.applyColor( selectedPoint.fcolor, g2d, pbw, pbh);
    g2d.setStroke( pointBoxStroke);
    g2d.draw( pointBox);
    
    g2d.setColor( Color.black);
    pointBoxText.draw( g2d, (float)x, (float)y);
  }
  
  private MouseListener mouseListener = new MouseAdapter() {
    @Override
    public void mousePressed( MouseEvent event)
    {
      if ( xAxis == null || yAxis == null) return;
      
      IScale xscale = xAxis.getScale();
      IScale yscale = yAxis.getScale();
      if ( xscale == null || yscale == null) return;
      
      int x0 = event.getX();
      int y0 = event.getY();
      int width = getWidth() - 1;
      int height = getHeight() - 1;

      selectedPlot = null;
      selectedPoint = null;
      pointBoxText = null;
      
      for( Plot plot: plots)
      {
        for( Point point: plot.getPoints())
        {
          double x1 = xscale.plot( point.coords[ 0]) * width;
          double y1 = height - yscale.plot( point.coords[ 1]) * height;
          
          double dx = Math.abs( x1 - x0);
          double dy = Math.abs( y1 - y0);
          if ( dx < hitTest || dy < hitTest)
          {
            double d = dx * dx + dy * dy;
            if ( d < hitTestSq)
            {
              selectedPlot = plot;
              selectedPoint = point;
              plots.remove( plot);
              plots.add( plot);
              repaint();
              return;
            }
          }
        }
      }
    }
  };

  final static int padWidth = 10;
  final static int padHeight = 10;
  
  private final static double pointBoxTabWidth = 7;
  private final static double pointBoxTabHeight = 7;
  private final static double pointBoxInset = 5;
  private final static double hitTest = 20;
  private final static double hitTestSq = hitTest * hitTest;
  
  private Axis xAxis;
  private Axis yAxis;
  
  private List<Plot> plots;
  private Map<Plot, Stroke> strokes;
  private Plot selectedPlot;
  private Point selectedPoint;
  
  private Path2D.Double plotLine;
  private Path2D.Double plotArea;
  private Path2D.Double pointBox;
  private TextLayout pointBoxText;
  private Stroke pointBoxStroke;
  
  private Color pointBoxDrawColor;
  private Color pointBoxFillColor;
}
