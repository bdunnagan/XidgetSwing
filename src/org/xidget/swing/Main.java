/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import java.io.File;
import javax.swing.SwingUtilities;
import org.xidget.Creator;
import org.xidget.caching.FileSystemCachingPolicy;
import org.xmodel.IModelObject;
import org.xmodel.external.ExternalReference;
import org.xmodel.external.ICachingPolicy;
import org.xmodel.external.IExternalReference;
import org.xmodel.external.caching.JarCachingPolicy;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An application that runs loads and executes an xaction script.
 */
public class Main
{
  public static void main( String[] args)
  {  
    try
    {
      run( args);
    }
    catch( Exception e)
    {
      e.printStackTrace();
    }
  }  

  public static void run( String[] args) throws Exception
  {    
    final File path = new File( args[ 0]);
    
    // get into the ui thread
    SwingUtilities.invokeLater( new Runnable() {
      public void run()
      {
        // register toolkit
        Creator.getInstance().setToolkit( new Toolkit());

        // create caching policy depending on whether we are running in a jar
        ICachingPolicy cachingPolicy = null;
        String classpath = System.getProperty( "java.class.path");
        boolean runningFromJar = classpath.indexOf( File.pathSeparator) < 0 && classpath.endsWith( ".jar");
        cachingPolicy = (runningFromJar)? new JarCachingPolicy(): new FileSystemCachingPolicy();
        
        try
        {
          String resourcePath = runningFromJar? System.getProperty( "java.class.path"): ".";
          
          IExternalReference resources = new ExternalReference( "resources");
          resources.setCachingPolicy( cachingPolicy);
          resources.setAttribute( "path", resourcePath);
          resources.setDirty( true);
          
          // run configuration
          IModelObject main = resources.getFirstChild( path.getName());
          if ( main == null) throw new IllegalArgumentException( "Unable to locate startup script: "+path);
          
          XActionDocument document = new XActionDocument( Main.class.getClassLoader());
          document.setRoot( main);
          
          StatefulContext context = new StatefulContext( resources);
          ScriptAction script = document.createScript();
          script.run( context);
        }
        catch( Exception e)
        {
          e.printStackTrace( System.err);
        }
      }
    });
  }
}
