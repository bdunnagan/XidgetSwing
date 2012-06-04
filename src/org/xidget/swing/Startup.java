/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import java.awt.Color;
import java.lang.Thread.UncaughtExceptionHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import org.xmodel.log.SLog;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * This class implements the startup behavior of a xidget application.
 */
public final class Startup extends org.xidget.Startup
{
  /**
   * Construct the Startup class.
   * @param resources The root of the resources.
   */
  public Startup( StatefulContext resources)
  {
    this.resources = resources;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.Startup#start(org.xmodel.xpath.expression.IExpression)
   */
  @Override
  public void start( IExpression path) throws Exception
  {
    //
    // Handle uncaught exceptions.
    //
    Thread.setDefaultUncaughtExceptionHandler( new UncaughtExceptionHandler() {
      public void uncaughtException( Thread t, Throwable e)
      {
        SLog.exception( this, e);
        //JOptionPane.showMessageDialog( null, BuildLabelHtml.buildHtml( "Error servicing request.\nPlease contact technical support."));
      }
    });
    
    //
    // Configure Swing LNF
    //
    try
    {
      for ( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
      {
        if ( "Nimbus".equals( info.getName()))
        {
          UIManager.setLookAndFeel( info.getClassName());
          break;
        }
      }
    } 
    catch ( Throwable t)
    {
    }
    
    //UIManager.put( "control", new Color( Color.HSBtoRGB( 0f, 0f, 1f)));
    //UIManager.put( "Table.alternateRowColor", Color.WHITE);
    UIManager.put( "nimbusOrange", new Color( Color.HSBtoRGB( 198 / 360f, 0.9f, 0.83f)));
    
    //
    // Start
    //
    super.start( path);
  }

  /* (non-Javadoc)
   * @see org.xidget.Startup#getResourceRoot()
   */
  @Override
  protected StatefulContext getResourceRoot()
  {
    return resources;
  }
  
  private StatefulContext resources;
}
