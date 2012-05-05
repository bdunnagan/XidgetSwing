/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

import javax.swing.RepaintManager;

import org.xidget.IXidget;
import org.xidget.ifeature.IPrintFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.Bounds;
import org.xmodel.log.Log;

/**
 * An implementation of IPrintFeature for the Swing/AWT platform.
 */
public class SwingPrintFeature implements IPrintFeature, Printable
{
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPrintFeature#capture(org.xidget.IXidget, float)
   */
  @Override
  public Object capture( IXidget xidget, float scale)
  {
    IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    Object[] widgets = creationFeature.getLastWidgets();
    Component component = (Component)widgets[ 0];
    
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = widgetFeature.getDefaultBounds();
    int width = (int)Math.ceil( bounds.width * scale);
    int height = (int)Math.ceil( bounds.height * scale);
    
    BufferedImage image = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = image.createGraphics();
    g.setTransform( AffineTransform.getScaleInstance( scale, scale));
    component.paint( g);
    
    return image;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IPrintFeature#print(java.util.List)
   */
  @Override
  public void print( List<IXidget> xidgets)
  {
    components = new ArrayList<Component>();
    
    for( IXidget xidget: xidgets)
    {
      IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
      Object[] widgets = creationFeature.getLastWidgets();
      components.add( (Component)widgets[ 0]);
    }
    
    PrinterJob printJob = PrinterJob.getPrinterJob();
    printJob.setPrintable( this);
    if ( printJob.printDialog())
    {
      try 
      { 
        printJob.print();
      } 
      catch( PrinterException e) 
      {
        log.exception( e);
      }
    }
  }
  
  public static void disableDoubleBuffering( Component c)
  {
    RepaintManager currentManager = RepaintManager.currentManager( c);
    currentManager.setDoubleBufferingEnabled( false);
  }

  public static void enableDoubleBuffering( Component c)
  {
    RepaintManager currentManager = RepaintManager.currentManager( c);
    currentManager.setDoubleBufferingEnabled( true);
  }

  /* (non-Javadoc)
   * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
   */
  @Override
  public int print( Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException
  {
    if ( pageIndex > 0) return NO_SUCH_PAGE;
    
    Graphics2D g2d = (Graphics2D)g;
    g2d.translate( pageFormat.getImageableX(), pageFormat.getImageableY());
    
    for( Component component: components)
    {
      disableDoubleBuffering( component);
      component.paint( g2d);
      enableDoubleBuffering( component);
    }
    
    return PAGE_EXISTS;
  }

  private final static Log log = Log.getLog( SwingPrintFeature.class);
  
  private List<Component> components;
}
