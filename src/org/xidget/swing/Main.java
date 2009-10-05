/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import java.io.File;
import java.io.FileInputStream;
import javax.swing.SwingUtilities;
import org.xidget.Creator;
import org.xmodel.IModelObject;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xml.XmlIO;

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
    final File file = new File( args[ 0]);

    String classpath = System.getProperty( "java.class.path");
    String[] entries = classpath.split( String.format( "[%s]", File.pathSeparator));
    for( String entry: entries) System.out.printf( "%s\n", entry);
    
    // get into the ui thread
    SwingUtilities.invokeLater( new Runnable() {
      public void run()
      {
        try
        {
          // register toolkit
          Creator.getInstance().setToolkit( new Toolkit());
          
          // load xml
          XmlIO xmlIO = new XmlIO();
          IModelObject root = xmlIO.read( new FileInputStream( file));
          XActionDocument document = new XActionDocument( Main.class.getClassLoader());
          document.setRoot( root);
          ScriptAction script = document.createScript();
          script.run();
        }
        catch( Exception e)
        {
          e.printStackTrace( System.err);
        }
      }
    });
  }
}
