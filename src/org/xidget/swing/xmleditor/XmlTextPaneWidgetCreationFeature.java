/**
 * Xidget - WYSIWYG Xidget Builder
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */

package org.xidget.swing.xmleditor;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.xidget.IXidget;
import org.xidget.ifeature.ISourceFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xmodel.ChangeSet;
import org.xmodel.IModelObject;
import org.xmodel.diff.DefaultXmlMatcher;
import org.xmodel.diff.XmlDiffer;
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
    differ = new XmlDiffer();
    differ.setMatcher( new DefaultXmlMatcher( true));
    errorHighlighter = new ErrorHighlightPainter();
    
    timer = new Timer( 250, new ActionListener() {
      public void actionPerformed( ActionEvent e)
      {
        Thread thread = new Thread( parseRunnable, "XmlTextPane");
        thread.setDaemon( true);
        thread.start();
      }
    });
    timer.setRepeats( false);
    
    lock = new Object();
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
  public void destroyWidgets()
  {
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
    timer.restart();
    synchronized( lock) { text = xmlTextPane.getText();}
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
  
  private Runnable parseRunnable = new Runnable() {
    public void run()
    {
      try
      {
        String xml = null;
        synchronized( lock) { xml = text;}
        IModelObject element = xmlIO.read( xml);
        synchronized( lock) { parsed = element;}
        SwingUtilities.invokeAndWait( updateSourceRunnable);
      } 
      catch( Exception e)
      {
      }
    }
  };
  
  private Runnable updateSourceRunnable = new Runnable() {
    public void run()
    {
      ISourceFeature feature = XmlTextPaneWidgetCreationFeature.this.xidget.getFeature( ISourceFeature.class);
      IModelObject node = feature.getSource( ISourceFeature.allChannel);
      if ( node != null)
      {
        IModelObject element = null;
        synchronized( lock) { element = parsed;}
        if ( element != null) 
        {
          ChangeSet changeSet = new ChangeSet();
          differ.diff( node, parsed, changeSet);
          changeSet.applyChanges();
        }
      }
    }
  };
  
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
  private XmlIO xmlIO;
  private String text;
  private IModelObject parsed;
  private XmlDiffer differ;
  private ErrorHighlightPainter errorHighlighter;
  private Timer timer;
  private Object lock;
}