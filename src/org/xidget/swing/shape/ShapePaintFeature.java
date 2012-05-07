/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.shape;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.EnumSet;
import java.util.List;
import javax.swing.border.Border;
import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IColorFeature;
import org.xidget.ifeature.ITextWidgetFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.canvas.ICanvasFeature;
import org.xidget.ifeature.canvas.IPaintFeature;
import org.xidget.ifeature.shape.IShapeFeature;
import org.xidget.layout.Bounds;
import org.xidget.layout.Margins;
import org.xidget.swing.Toolkit;
import org.xidget.swing.feature.SwingTextWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpression.ResultType;
import org.xmodel.xpath.expression.StatefulContext;

/**
 */
public class ShapePaintFeature implements IPaintFeature<Graphics2D>, IShapeFeature, IWidgetFeature, ITextWidgetFeature
{
  public ShapePaintFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.path = new Path2D.Double();
    this.arc = new Arc2D.Double();
    this.reset = true;
    this.frc = new FontRenderContext( new AffineTransform(), true, false);
    
    this.defaultBounds = new Bounds( 0, 0, -1, -1);
    this.computedBounds = new Bounds( 0, 0, -1, -1);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.IPaintFeature#setLayer(java.lang.String)
   */
  @Override
  public void setLayer( String layer)
  {
    this.layer = layer;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.IPaintFeature#getLayer()
   */
  @Override
  public String getLayer()
  {
    return layer;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.canvas.IPaintFeature#paint(java.lang.Object)
   */
  @Override
  public void paint( Graphics2D graphics)
  {
    if ( reset) buildPath( xidget.getConfig());
    
    graphics = (Graphics2D)graphics.create();
    if ( fill)
    {
      graphics.setColor( bcolor);
      graphics.fill( path);
    }
    
    graphics.setStroke( stroke);
    graphics.setColor( fcolor);
    graphics.draw( path);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.shape.IShapeFeature#setFill(boolean)
   */
  @Override
  public void setFill( boolean fill)
  {
    this.fill = fill;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.shape.IShapeFeature#setStrokeWidth(double)
   */
  @Override
  public void setStrokeWidth( double width)
  {
    stroke = new BasicStroke( (float)width);
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.shape.IShapeFeature#setRelativeCoordinates(boolean)
   */
  @Override
  public void setRelativeCoordinates( boolean relative)
  {
    this.relative = relative;
    repaint();
  }
  
  /**
   * Repaint the parent canvas.
   */
  private void repaint()
  {
    ICanvasFeature canvasFeature = xidget.getFeature( ICanvasFeature.class);
    if ( canvasFeature != null) canvasFeature.repaint();
  }

  /**
   * Build a path from the specified configuration.
   * @param config The configuration.
   */
  private void buildPath( IModelObject config)
  {
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext context = bindFeature.getBoundContext();
    
    path.reset();
    double x = 0, y = 0;
    Spline spline = null;
    for( IModelObject child: config.getChildren())
    {
      if ( child.isType( "curveTo"))
      {
        if ( spline == null) 
        {
          spline = new Spline();
          spline.addControl( x, y);
        }
          
        double[] coords = getCoordinates( context, child);
        x = coords[ 0];
        y = coords[ 1];
        spline.addControl( x, y);
      }
      else
      {
        if ( spline != null)
        {
          for( Point2D.Double point: spline.interpolate())
            path.lineTo( point.x, point.y);
          spline = null;
        }
        
        if ( child.isType( "moveTo"))
        {
          double[] coords = getCoordinates( context, child);
          x = coords[ 0];
          y = coords[ 1];
          path.moveTo( x, y);
        }
        else if ( child.isType( "lineTo"))
        {
          double[] coords = getCoordinates( context, child);
          x = coords[ 0];
          y = coords[ 1];
          path.lineTo( x, y);
        }
        else if ( child.isType( "arcTo"))
        {
          // cx, cy, r
          double[] coords = getCoordinates( context, child);
          
          double dx = x - coords[ 0];
          double dy = y - coords[ 1];
          double r = Math.sqrt( dx * dx + dy * dy);
          double s = Math.atan2( dy, dx);
          
          int closure = Xlate.get( child, "fill", false)? Arc2D.OPEN: Arc2D.PIE;
          arc.setArcByCenter( coords[ 0], coords[ 1], r, s, coords[ 2], closure);
          path.append( arc, true);
          
          x = coords[ 0];
          y = coords[ 1];
        }
        else if ( child.isType( "text"))
        {
          if ( font == null) font = new Font( fontFamily, fontStyles, (int)fontSize);
          TextLayout textLayout = new TextLayout( Xlate.get( child, ""), font, frc);
          Shape shape = textLayout.getOutline( new AffineTransform());
          path.append( shape, false);
        }
      }
    }
  }
  
  /**
   * Returns the coordinates defined in the specified element.
   * @param context The context.
   * @param element The element.
   * @return Returns the coordinates defined in the specified element.
   */
  private double[] getCoordinates( IContext context, IModelObject element)
  {
    IExpression pointExpr = Xlate.get( element, (IExpression)null);
    if ( pointExpr != null)
    {
      ResultType type = pointExpr.getType( context);
      if ( type == ResultType.NODES)
      {
        List<IModelObject> points = pointExpr.query( context, null);
        if ( points.size() > 0)
        {
          Object first = points.get( 0).getValue();
          if ( first instanceof Number || !first.toString().contains( ","))
          {
            // list of single coordinates
            double[] result = new double[ points.size()];
            for( int i=0; i<result.length; i++)
            {
              IModelObject point = points.get( i);
              Object value = point.getValue();
              result[ i] = (value instanceof Number)? 
                ((Number)value).doubleValue():
                Double.parseDouble( value.toString());
            }
          }
          else
          {
            // list of coordinate pairs as strings
            double[] result = new double[ points.size() * 2];
            for( int i=0, j=0; i<result.length; i++)
            {
              IModelObject point = points.get( i);
              String value = Xlate.get( point, "");
              String[] parts = value.split( "\\s*,\\s*");
              result[ j++] = Double.parseDouble( parts[ 0]);
              result[ j++] = Double.parseDouble( parts[ 1]);
            }
          }
        }
      }
      else if ( type == ResultType.STRING)
      {
        String points = pointExpr.evaluateString( context);
        String[] parts = points.split( "\\s*,\\s*");
        double[] result = new double[ parts.length];
        for( int i=0; i<parts.length; i++)
          result[ i] = Double.parseDouble( parts[ i].trim());
        return result;
      }
    }

    throw new IllegalArgumentException( "Illegal coordinate definition.");
  }
    
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setDefaultBounds(float, float, float, float, boolean)
   */
  public void setDefaultBounds( float x, float y, float width, float height, boolean clamp)
  {
    defaultBounds.x = x;
    defaultBounds.y = y;
    defaultBounds.width = width;
    defaultBounds.height = height;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getDefaultBounds()
   */
  public Bounds getDefaultBounds()
  {
    buildPath( xidget.getConfig());
    Rectangle bounds = path.getBounds();
    
    if ( defaultBounds.x < 0) defaultBounds.x = bounds.x;
    if ( defaultBounds.y < 0) defaultBounds.y = bounds.y;
    if ( defaultBounds.width < 0) defaultBounds.width = bounds.width;
    if ( defaultBounds.height < 0) defaultBounds.height = bounds.height;
    
    return defaultBounds;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setComputedBounds(float, float, float, float)
   */
  public void setComputedBounds( float x, float y, float width, float height)
  {
    if ( x == computedBounds.x && y == computedBounds.y && width == computedBounds.width && height == computedBounds.height) return;
    
    computedBounds.x = x;
    computedBounds.y = y;
    computedBounds.width = width;
    computedBounds.height = height;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getComputedBounds()
   */
  public Bounds getComputedBounds()
  {
    Bounds defaultBounds = getDefaultBounds();
    Bounds bounds = new Bounds( computedBounds);
    if ( !bounds.isXDefined()) bounds.x = defaultBounds.x;
    if ( !bounds.isYDefined()) bounds.y = defaultBounds.y;
    if ( !bounds.isWidthDefined()) bounds.width = defaultBounds.width;
    if ( !bounds.isHeightDefined()) bounds.height = defaultBounds.height;
    
    if ( !bounds.isXDefined()) bounds.x = 0;
    if ( !bounds.isYDefined()) bounds.y = 0;
    
    return bounds;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setBoundsNode(org.xmodel.IModelObject)
   */
  public void setBoundsNode( IModelObject node)
  {
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getBoundsNode()
   */
  public IModelObject getBoundsNode()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setInsideMargins(org.xidget.layout.Margins)
   */
  @Override
  public void setInsideMargins( Margins margins)
  {
    insideMargins = margins;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getInsideMargins()
   */
  @Override
  public Margins getInsideMargins()
  {
    if ( insideMargins == null) insideMargins = new Margins();
    return insideMargins;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setOutsideMargins(org.xidget.layout.Margins)
   */
  @Override
  public void setOutsideMargins( Margins margins)
  {
    outsideMargins = margins;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getOutsideMargins()
   */
  @Override
  public Margins getOutsideMargins()
  {
    if ( outsideMargins == null) outsideMargins = new Margins();
    return outsideMargins;
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetFeature#setVisible(boolean)
   */
  public void setVisible( boolean visible)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#getVisible()
   */
  public boolean getVisible()
  {
    return true;
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setEnabled(boolean)
   */
  public void setEnabled( boolean enabled)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.IWidgetAdapter#setTooltip(java.lang.String)
   */
  public void setTooltip( String tooltip)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setBackground(java.lang.Object)
   */
  @SuppressWarnings({ "unchecked", "unused" })
  public void setBackground( Object color)
  {
    Toolkit toolkit = (Toolkit)Creator.getToolkit();
    IColorFeature<Color, Graphics2D> colorFeature = toolkit.getFeature( IColorFeature.class);
    fcolor = (Color)color;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetFeature#setForeground(java.lang.Object)
   */
  @SuppressWarnings({ "unchecked", "unused" })
  public void setForeground( Object color)
  {
    Toolkit toolkit = (Toolkit)Creator.getToolkit();
    IColorFeature<Color, Graphics2D> colorFeature = toolkit.getFeature( IColorFeature.class);
    bcolor = (Color)color;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setHAlign(org.xidget.ifeature.ITextWidgetFeature.HAlign)
   */
  @Override
  public void setHAlign( HAlign alignment)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setVAlign(org.xidget.ifeature.ITextWidgetFeature.VAlign)
   */
  @Override
  public void setVAlign( VAlign alignment)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setEditable(boolean)
   */
  @Override
  public void setEditable( boolean editable)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setFontFamily(java.lang.String)
   */
  @Override
  public void setFontFamily( String family)
  {
    fontFamily = SwingTextWidgetFeature.matchFamily( family);
    font = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setFontStyles(java.util.EnumSet)
   */
  @Override
  public void setFontStyles( EnumSet<FontStyle> styles)
  {
    fontStyles = Font.PLAIN;
    if ( styles.contains( FontStyle.italic)) fontStyles |= Font.ITALIC;
    if ( styles.contains( FontStyle.bold)) fontStyles |= Font.BOLD;
    
    font = null;
    repaint();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITextWidgetFeature#setFontSize(double)
   */
  @Override
  public void setFontSize( double size)
  {
    fontSize = size;
    font = null;
    repaint();
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
  private String layer;
  private Path2D.Double path;
  private Arc2D.Double arc;
  private Stroke stroke;
  private boolean fill;
  private boolean relative;
  private boolean reset;
  private Color fcolor;
  private Color bcolor;
  
  private FontRenderContext frc;
  private String fontFamily;
  private int fontStyles;
  private double fontSize;
  private Font font;

  protected Bounds defaultBounds = new Bounds();
  protected Bounds computedBounds = new Bounds();
  protected Margins insideMargins;
  protected Margins outsideMargins;
  protected Border insideBorder;
  protected Border outsideBorder;
}
