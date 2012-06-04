package org.xidget.swing;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import javax.swing.JApplet;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.caching.XipAssociation;
import org.xidget.swing.applet.JAppletXidget;
import org.xidget.swing.feature.SwingWidgetFeature;
import org.xmodel.caching.URLCachingPolicy;
import org.xmodel.external.ExternalReference;
import org.xmodel.external.UnboundedCache;
import org.xmodel.log.Log;
import org.xmodel.xpath.XPath;
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
    try
    {
      //
      // Even though it is technically legal to run create content from the init() method,
      // we must dispatch here so that the Applet instance is registered with the Creator
      // for the event dispatching thread.  See * in createContent.
      //
      SwingUtilities.invokeAndWait( new Runnable() {
        public void run()
        {
          createContent();
        }
      });
    }
    catch( Exception e)
    {
      throw new RuntimeException( e);
    }
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
    
    //
    // Register applet widget with a JAppletXidget instance.
    // 
    JAppletXidget appletXidget = new JAppletXidget( this);
    
    //
    // * This call is one reason why this method must be invoked in the event dispatching thread.
    //
    Creator.getInstance().register( this, appletXidget);
    
    // configure the xidget toolkit
    Creator.setToolkitClass( Toolkit.class);
    
    try
    {
      URL url = getClass().getResource( "/xapp.xip");
      URLCachingPolicy cachingPolicy = new URLCachingPolicy( new UnboundedCache());
      
      // HACK: Need to formalize caching policy structure for startup and put it into org.xidget.Startup.
      Creator.setToolkitClass( Toolkit.class);
      cachingPolicy.addAssociation( new XipAssociation());
      
      ExternalReference resources = new ExternalReference( "resources");
      resources.setAttribute( "url", url);
      resources.setCachingPolicy( cachingPolicy);
      resources.setDirty( true);
            
      // Run configuration
      StatefulContext context = new StatefulContext( resources);
      context.set( "window", appletXidget.getConfig());
      Startup startup = new Startup( context);
      startup.start( XPath.createExpression( "xapp/main.xml"));
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
