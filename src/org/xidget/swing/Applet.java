package org.xidget.swing;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.swing.applet.JAppletXidget;
import org.xidget.swing.feature.SwingWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.caching.URLCachingPolicy;
import org.xmodel.external.ExternalReference;
import org.xmodel.external.UnboundedCache;
import org.xmodel.log.Log;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.StatefulContext;

public class Applet extends JApplet
{
  public Applet()
  {
    Log.getLog( SwingWidgetFeature.class.getCanonicalName()).setLevel( Log.all);
  }
  
  /* (non-Javadoc)
   * @see java.applet.Applet#init()
   */
  @Override
  public void init()
  {
    createContent();
  }
  
  /* (non-Javadoc)
   * @see java.applet.Applet#start()
   */
  @Override
  public void start()
  {
  }

  /* (non-Javadoc)
   * @see java.applet.Applet#stop()
   */
  @Override
  public void stop()
  {
  }
  
  /* (non-Javadoc)
   * @see java.applet.Applet#destroy()
   */
  @Override
  public void destroy()
  {
    if ( xidget != null)
    {
      Creator.getInstance().destroy( xidget);
      xidget = null;
    }
  }

  /**
   * Build the ui.
   */
  private void createContent()
  {
    setBackground( Color.white);
    getContentPane().setBackground( Color.white);

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
    } catch ( Exception e)
    {
    }    
    
    // Register toolkit
    Creator.getInstance().setToolkit( new Toolkit());

    // Register applet widget with a JAppletXidget instance
    JAppletXidget appletXidget = new JAppletXidget( this);
    Creator.getInstance().register( this, appletXidget);
    
    try
    {
      URL url = getClass().getResource( "/xac.xip");
      
      // Create caching policy for app.xip
      ExternalReference root = new ExternalReference( "root");
      root.setAttribute( "url", url);
      root.setCachingPolicy( new URLCachingPolicy( new UnboundedCache()));
      root.setDirty( true);
            
      // Context
      StatefulContext context = new StatefulContext( root);
      context.set( "applet", appletXidget.getConfig());
      
      // Run configuration
      IModelObject xac = root.getFirstChild( "xac");
      IModelObject main = xac.getFirstChild( "main.xml");
      if ( main == null) throw new RuntimeException( "Unable to locate startup script: main.xml.");
      
      XActionDocument document = new XActionDocument( Applet.class.getClassLoader());
      document.setRoot( main);
      
      document.addPackage( "org.xidget.xaction");
      ScriptAction script = document.createScript();
      script.run( context);
    }
    catch( Throwable e)
    {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      e.printStackTrace( new PrintStream( stream));
      
      JTextArea text = new JTextArea();
      text.setText( stream.toString());
      
      getContentPane().add( text);
    }
  }
  
  private static final long serialVersionUID = 1L;
  
  private IXidget xidget;
}
