/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.chart.line;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * Graphics context for testing drawing features.
 */
public class TestGraphics extends Graphics2D
{
  /* (non-Javadoc)
   * @see java.awt.Graphics2D#addRenderingHints(java.util.Map)
   */
  @Override
  public void addRenderingHints( Map<?, ?> arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#clip(java.awt.Shape)
   */
  @Override
  public void clip( Shape arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#draw(java.awt.Shape)
   */
  @Override
  public void draw( Shape arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#drawGlyphVector(java.awt.font.GlyphVector, float, float)
   */
  @Override
  public void drawGlyphVector( GlyphVector gv, float x, float y)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#drawImage(java.awt.Image, java.awt.geom.AffineTransform, java.awt.image.ImageObserver)
   */
  @Override
  public boolean drawImage( Image arg0, AffineTransform arg1, ImageObserver arg2)
  {
    return false;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#drawImage(java.awt.image.BufferedImage, java.awt.image.BufferedImageOp, int, int)
   */
  @Override
  public void drawImage( BufferedImage arg0, BufferedImageOp arg1, int arg2, int arg3)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#drawRenderableImage(java.awt.image.renderable.RenderableImage, java.awt.geom.AffineTransform)
   */
  @Override
  public void drawRenderableImage( RenderableImage arg0, AffineTransform arg1)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#drawRenderedImage(java.awt.image.RenderedImage, java.awt.geom.AffineTransform)
   */
  @Override
  public void drawRenderedImage( RenderedImage arg0, AffineTransform arg1)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#drawString(java.lang.String, int, int)
   */
  @Override
  public void drawString( String arg0, int arg1, int arg2)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#drawString(java.lang.String, float, float)
   */
  @Override
  public void drawString( String arg0, float arg1, float arg2)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#drawString(java.text.AttributedCharacterIterator, int, int)
   */
  @Override
  public void drawString( AttributedCharacterIterator arg0, int arg1, int arg2)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#drawString(java.text.AttributedCharacterIterator, float, float)
   */
  @Override
  public void drawString( AttributedCharacterIterator arg0, float arg1, float arg2)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#fill(java.awt.Shape)
   */
  @Override
  public void fill( Shape arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#getBackground()
   */
  @Override
  public Color getBackground()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#getComposite()
   */
  @Override
  public Composite getComposite()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#getDeviceConfiguration()
   */
  @Override
  public GraphicsConfiguration getDeviceConfiguration()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#getFontRenderContext()
   */
  @Override
  public FontRenderContext getFontRenderContext()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#getPaint()
   */
  @Override
  public Paint getPaint()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#getRenderingHint(java.awt.RenderingHints.Key)
   */
  @Override
  public Object getRenderingHint( Key arg0)
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#getRenderingHints()
   */
  @Override
  public RenderingHints getRenderingHints()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#getStroke()
   */
  @Override
  public Stroke getStroke()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#getTransform()
   */
  @Override
  public AffineTransform getTransform()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#hit(java.awt.Rectangle, java.awt.Shape, boolean)
   */
  @Override
  public boolean hit( Rectangle arg0, Shape arg1, boolean arg2)
  {
    return false;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#rotate(double)
   */
  @Override
  public void rotate( double arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#rotate(double, double, double)
   */
  @Override
  public void rotate( double arg0, double arg1, double arg2)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#scale(double, double)
   */
  @Override
  public void scale( double arg0, double arg1)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#setBackground(java.awt.Color)
   */
  @Override
  public void setBackground( Color arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#setComposite(java.awt.Composite)
   */
  @Override
  public void setComposite( Composite arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#setPaint(java.awt.Paint)
   */
  @Override
  public void setPaint( Paint arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#setRenderingHint(java.awt.RenderingHints.Key, java.lang.Object)
   */
  @Override
  public void setRenderingHint( Key arg0, Object arg1)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#setRenderingHints(java.util.Map)
   */
  @Override
  public void setRenderingHints( Map<?, ?> arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#setStroke(java.awt.Stroke)
   */
  @Override
  public void setStroke( Stroke arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#setTransform(java.awt.geom.AffineTransform)
   */
  @Override
  public void setTransform( AffineTransform arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#shear(double, double)
   */
  @Override
  public void shear( double arg0, double arg1)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#transform(java.awt.geom.AffineTransform)
   */
  @Override
  public void transform( AffineTransform arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#translate(int, int)
   */
  @Override
  public void translate( int arg0, int arg1)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics2D#translate(double, double)
   */
  @Override
  public void translate( double arg0, double arg1)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#clearRect(int, int, int, int)
   */
  @Override
  public void clearRect( int arg0, int arg1, int arg2, int arg3)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#clipRect(int, int, int, int)
   */
  @Override
  public void clipRect( int arg0, int arg1, int arg2, int arg3)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#copyArea(int, int, int, int, int, int)
   */
  @Override
  public void copyArea( int arg0, int arg1, int arg2, int arg3, int arg4, int arg5)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#create()
   */
  @Override
  public Graphics create()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#dispose()
   */
  @Override
  public void dispose()
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#drawArc(int, int, int, int, int, int)
   */
  @Override
  public void drawArc( int arg0, int arg1, int arg2, int arg3, int arg4, int arg5)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, java.awt.image.ImageObserver)
   */
  @Override
  public boolean drawImage( Image arg0, int arg1, int arg2, ImageObserver arg3)
  {
    return false;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, java.awt.Color, java.awt.image.ImageObserver)
   */
  @Override
  public boolean drawImage( Image arg0, int arg1, int arg2, Color arg3, ImageObserver arg4)
  {
    return false;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, int, int, java.awt.image.ImageObserver)
   */
  @Override
  public boolean drawImage( Image arg0, int arg1, int arg2, int arg3, int arg4, ImageObserver arg5)
  {
    return false;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, int, int, java.awt.Color, java.awt.image.ImageObserver)
   */
  @Override
  public boolean drawImage( Image arg0, int arg1, int arg2, int arg3, int arg4, Color arg5, ImageObserver arg6)
  {
    return false;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, int, int, int, int, int, int, java.awt.image.ImageObserver)
   */
  @Override
  public boolean drawImage( Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8,
      ImageObserver arg9)
  {
    return false;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, int, int, int, int, int, int, java.awt.Color, java.awt.image.ImageObserver)
   */
  @Override
  public boolean drawImage( Image arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8,
      Color arg9, ImageObserver arg10)
  {
    return false;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#drawLine(int, int, int, int)
   */
  @Override
  public void drawLine( int arg0, int arg1, int arg2, int arg3)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#drawOval(int, int, int, int)
   */
  @Override
  public void drawOval( int arg0, int arg1, int arg2, int arg3)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#drawPolygon(int[], int[], int)
   */
  @Override
  public void drawPolygon( int[] arg0, int[] arg1, int arg2)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#drawPolyline(int[], int[], int)
   */
  @Override
  public void drawPolyline( int[] arg0, int[] arg1, int arg2)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#drawRoundRect(int, int, int, int, int, int)
   */
  @Override
  public void drawRoundRect( int arg0, int arg1, int arg2, int arg3, int arg4, int arg5)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#fillArc(int, int, int, int, int, int)
   */
  @Override
  public void fillArc( int arg0, int arg1, int arg2, int arg3, int arg4, int arg5)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#fillOval(int, int, int, int)
   */
  @Override
  public void fillOval( int arg0, int arg1, int arg2, int arg3)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#fillPolygon(int[], int[], int)
   */
  @Override
  public void fillPolygon( int[] arg0, int[] arg1, int arg2)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#fillRect(int, int, int, int)
   */
  @Override
  public void fillRect( int arg0, int arg1, int arg2, int arg3)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#fillRoundRect(int, int, int, int, int, int)
   */
  @Override
  public void fillRoundRect( int arg0, int arg1, int arg2, int arg3, int arg4, int arg5)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#getClip()
   */
  @Override
  public Shape getClip()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#getClipBounds()
   */
  @Override
  public Rectangle getClipBounds()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#getColor()
   */
  @Override
  public Color getColor()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#getFont()
   */
  @Override
  public Font getFont()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#getFontMetrics(java.awt.Font)
   */
  @Override
  public FontMetrics getFontMetrics( Font arg0)
  {
    return null;
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#setClip(java.awt.Shape)
   */
  @Override
  public void setClip( Shape arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#setClip(int, int, int, int)
   */
  @Override
  public void setClip( int arg0, int arg1, int arg2, int arg3)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#setColor(java.awt.Color)
   */
  @Override
  public void setColor( Color arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#setFont(java.awt.Font)
   */
  @Override
  public void setFont( Font arg0)
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#setPaintMode()
   */
  @Override
  public void setPaintMode()
  {
  }

  /* (non-Javadoc)
   * @see java.awt.Graphics#setXORMode(java.awt.Color)
   */
  @Override
  public void setXORMode( Color arg0)
  {
  }
}
