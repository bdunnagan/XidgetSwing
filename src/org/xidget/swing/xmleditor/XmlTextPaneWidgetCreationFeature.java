/**
 * Xidget - WYSIWYG Xidget Builder
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */

package org.xidget.swing.xmleditor;

import java.awt.Container;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.xidget.IXidget;
import org.xidget.ifeature.model.ISingleValueUpdateFeature;
import org.xidget.ifeature.model.ISingleValueWidgetFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xmodel.IModelObject;
import org.xmodel.xml.XmlIO;

/**
 * An implementation of IWidgetCreationFeature that creates a net.boplicity.xmleditor.XmlTextPane.
 */
public class XmlTextPaneWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public XmlTextPaneWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
    
    xmlIO = new XmlIO();
    xmlIO.setErrorHandler( errorHandler);
    errorHighlighter = new ErrorHighlightPainter();

    executor = Executors.newFixedThreadPool( 1);
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    xmlTextPane = new XmlTextPane();
    xmlTextPane.getDocument().addDocumentListener( documentListener);

    jScrollPane = new JScrollPane( xmlTextPane);
    jScrollPane.setBorder( BorderFactory.createEmptyBorder());
    return jScrollPane;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#destroyWidgets()
   */
  public void destroyWidgets(IXidget parent)
  {
    executor.shutdownNow();
    
    jScrollPane.setEnabled( false);
    
    Container container = jScrollPane.getParent();
    if ( container != null) container.remove( jScrollPane);
    
    jScrollPane = null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jScrollPane, xmlTextPane};
  }
  
  /**
   * Returns the scroll pane that holds the table.
   * @return Returns the scroll pane that holds the table.
   */
  public JScrollPane getJScrollPane()
  {
    return jScrollPane;
  }

  /**
   * Returns the XmlTextPane that was created.
   * @return Returns the XmlTextPane that was created.
   */
  public XmlTextPane getXmlTextPane()
  {
    return xmlTextPane;
  }
  
  /**
   * Specify an error marker to be drawn at the specified line and column.
   * @param line The line number. 
   * @param column The column number.
   */
  private void setErrorMarker( int line, int column)
  {
    int index = getAbsoluteLocation( line-1, column-1);
    try
    {
      xmlTextPane.getHighlighter().removeAllHighlights();
      xmlTextPane.getHighlighter().addHighlight( index, index+1, errorHighlighter);
    }
    catch( Exception e)
    {
    }
  }
  
  /**
   * Convert the specified line and column number into an absolute location.
   * @param line The line number. 
   * @param column The column number.
   * @return Returns the absolute location.
   */
  private int getAbsoluteLocation( int line, int column)
  {
    int index = 0;
    
    String text = xmlTextPane.getText();
    char[] array = text.toCharArray();
    
    // find line
    for( ; index < text.length() && line > 0; index++)
    {
      if ( array[ index] == '\n') line--;
    }
    
    index += column;
    if ( line > 0 || index >= text.length()) return text.length()-1;
    return index;
  }
  
  /**
   * Reparse the editor content and update markers.
   */
  private void updateEditor()
  {
    xmlTextPane.getHighlighter().removeAllHighlights();
    if ( future != null) future.cancel( false);
    future = new FutureTask<IModelObject>( parseCallable);
    executor.submit( future);
  }

  private DocumentListener documentListener = new DocumentListener() {
    public void changedUpdate( DocumentEvent e)
    {
      updateEditor();
    }
    public void insertUpdate( DocumentEvent e)
    {
      updateEditor();
    }
    public void removeUpdate( DocumentEvent e)
    {
      updateEditor();
    }
  };
  
  private Callable<IModelObject> parseCallable = new Callable<IModelObject>() {
    public IModelObject call() throws Exception
    {
      String text = xmlTextPane.getText();
      IModelObject element = xmlIO.read( text);
      SwingUtilities.invokeLater( new Commiter( element));
      return element;
    }
  };
  
  private class Commiter implements Runnable
  {
    public Commiter( IModelObject element)
    {
      this.element = element;
    }
    
    public void run()
    {
      XmlTextPaneSingleValueWidgetFeature widgetFeature = (XmlTextPaneSingleValueWidgetFeature)xidget.getFeature( ISingleValueWidgetFeature.class);
      widgetFeature.ignoreUpdate( true);
      
      try
      {
        ISingleValueUpdateFeature updateFeature = xidget.getFeature( ISingleValueUpdateFeature.class);
        updateFeature.commit( element);
      }
      finally
      {
        widgetFeature.ignoreUpdate( false);
      }
    }
    
    private IModelObject element;
  }
  
  private ErrorHandler errorHandler = new ErrorHandler() {
    public void warning( SAXParseException exception) throws SAXException
    {
      setErrorMarker( exception.getLineNumber(), exception.getColumnNumber());
    }
    public void error( SAXParseException exception) throws SAXException
    {
      setErrorMarker( exception.getLineNumber(), exception.getColumnNumber());
    }
    public void fatalError( SAXParseException exception) throws SAXException
    {
      setErrorMarker( exception.getLineNumber(), exception.getColumnNumber());
    }
  };

  private JScrollPane jScrollPane;
  private XmlTextPane xmlTextPane;
  private ErrorHighlightPainter errorHighlighter;
  private XmlIO xmlIO;
  private FutureTask<IModelObject> future;
  private ExecutorService executor;
}
