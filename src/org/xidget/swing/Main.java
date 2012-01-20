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
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collections;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.xidget.Creator;
import org.xidget.caching.FileSystemCachingPolicy;
import org.xidget.caching.ZipCachingPolicy;
import org.xmodel.IModelObject;
import org.xmodel.external.ExternalReference;
import org.xmodel.external.ICachingPolicy;
import org.xmodel.external.IExternalReference;
import org.xmodel.log.Log;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.Context;
import org.xmodel.xpath.expression.IExpression;
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
    final String path = (args.length > 0)? args[ 0]: "main.xml";
    
    // handle uncaught exceptions
    Thread.setDefaultUncaughtExceptionHandler( new UncaughtExceptionHandler() {
      public void uncaughtException( Thread t, Throwable e)
      {
        e.printStackTrace( System.err);
        JOptionPane.showMessageDialog( null, String.format( "Thread: %s\n%s: %s",
          t.getName(), e.getClass().getName(), e.getMessage()));
      }
    });

    // get into the ui thread
    SwingUtilities.invokeLater( new Runnable() {
      public void run()
      {
        try
        {
          for ( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
          {
            log.info( info.getName());
            if ( "Nimbus".equals( info.getName()))
            {
              UIManager.setLookAndFeel( info.getClassName());
              break;
            }
          }
        } catch ( Exception e)
        {
        }
        
        // register toolkit
        Creator.setToolkit( new Toolkit());

        // create caching policy depending on whether we are running in a jar
        ICachingPolicy cachingPolicy = null;
        String classpath = System.getProperty( "java.class.path");
        boolean runningFromJar = classpath.indexOf( File.pathSeparator) < 0 && classpath.endsWith( ".jar");
        cachingPolicy = (runningFromJar)? new ZipCachingPolicy(): new FileSystemCachingPolicy();
        
        try
        {
          String resourcePath = runningFromJar? System.getProperty( "java.class.path"): ".";
          
          IExternalReference resources = new ExternalReference( "resources");
          resources.setCachingPolicy( cachingPolicy);
          resources.setAttribute( "path", resourcePath);
          resources.setDirty( true);
          
          // run configuration
          IExpression xpath = XPath.createExpression( path);
          IModelObject main = xpath.queryFirst( new Context( resources));
          if ( main == null) throw new IllegalArgumentException( "Unable to locate startup script: "+path);
          
          XActionDocument document = new XActionDocument( Main.class.getClassLoader());
          document.setRoot( main);
          
          StatefulContext context = new StatefulContext( resources);
          context.set( "applet", Collections.<IModelObject>emptyList());
          
          document.addPackage( "org.xidget.xaction");
          ScriptAction script = document.createScript();
          script.run( context);
        }
        catch( Exception e)
        {
          log.exception( e);
        }
      }
    });
  }
  
  private static Log log = Log.getLog( "org.xidget.swing");
}
