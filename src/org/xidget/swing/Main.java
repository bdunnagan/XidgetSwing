package org.xidget.swing;

import java.io.File;
import java.io.FileInputStream;
import org.xmodel.IModelObject;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xml.XmlIO;

/**
 * An application that runs loads and executes an xaction script.
 */
public class Main
{
  public static void main( String[] args) throws Exception
  {
    File file = new File( args[ 0]);
    System.out.println( file.getAbsolutePath());
    
    // load xml
    XmlIO xmlIO = new XmlIO();
    IModelObject root = xmlIO.read( new FileInputStream( file));
    
    // run script
    XActionDocument document = new XActionDocument( Main.class.getClassLoader());
    document.setRoot( root);
    ScriptAction script = document.createScript();
    script.run();
  }
}
