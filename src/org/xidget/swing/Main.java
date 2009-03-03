package org.xidget.swing;

import java.io.File;
import java.io.FileInputStream;
import javax.swing.SwingUtilities;
import org.xmodel.IDispatcher;
import org.xmodel.IModelObject;
import org.xmodel.ModelRegistry;
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
    final File file = new File( args[ 0]);
    
    SwingUtilities.invokeLater( new Runnable() {
      public void run()
      {
        try
        {
          // load xml
          XmlIO xmlIO = new XmlIO();
          IModelObject root = xmlIO.read( new FileInputStream( file));
          
          // create dispatcher
          ModelRegistry.getInstance().getModel().setDispatcher( new IDispatcher() {
            public void execute( Runnable runnable)
            {
              SwingUtilities.invokeLater( runnable);
            }
          });
          
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
