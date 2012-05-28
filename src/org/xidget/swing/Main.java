/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * Main.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.swing;

import java.io.File;
import javax.swing.SwingUtilities;
import org.xidget.Creator;
import org.xidget.caching.ZipCachingPolicy;
import org.xidget.swing.image.ImageFileAssociation;
import org.xmodel.caching.FileSystemCachingPolicy;
import org.xmodel.external.ExternalReference;
import org.xmodel.external.ICachingPolicy;
import org.xmodel.external.IExternalReference;
import org.xmodel.log.Log;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An application that runs loads and executes an xaction script.
 */
public class Main
{
  public static void main( final String[] args)
  {  
    try
    {
      SwingUtilities.invokeLater( new Runnable() {
        public void run()
        {
          start( args);
        }
      });
    }
    catch( Exception e)
    {
      e.printStackTrace();
    }
  }  

  public static void start( String[] args)
  {    
    final String path = (args.length > 0)? args[ 0]: "xapp/main.xml";
    
    //
    // Initialize Creator first so that scripts relying on xidget XPath functions can run headless.
    //
    Creator.getInstance();
    
    //
    // Create caching policy depending on whether we are running in a jar.
    //
    ICachingPolicy cachingPolicy = null;
    String classpath = System.getProperty( "java.class.path");
    String resourcePath;
    if ( classpath.indexOf( File.pathSeparator) < 0 && classpath.endsWith( ".jar"))
    {
      cachingPolicy = new ZipCachingPolicy();
      resourcePath = System.getProperty( "java.class.path");
    }
    else
    {
      FileSystemCachingPolicy fileSystemCachingPolicy = new FileSystemCachingPolicy();
      fileSystemCachingPolicy.addAssociation( new ImageFileAssociation());
      cachingPolicy = fileSystemCachingPolicy;
      resourcePath = ".";
    }
    
    try
    {
      IExternalReference resources = new ExternalReference( "resources");
      resources.setCachingPolicy( cachingPolicy);
      resources.setAttribute( "path", resourcePath);
      resources.setDirty( true);
      
      // Run configuration
      Startup startup = new Startup( new StatefulContext( resources));
      startup.start( XPath.createExpression( path));
    }
    catch( Exception e)
    {
      log.exception( e);
    }
  }
  
  private static Log log = Log.getLog( "org.xidget.swing");
}
