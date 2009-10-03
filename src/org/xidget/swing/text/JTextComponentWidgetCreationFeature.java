/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
import org.xidget.IXidget;
import org.xidget.feature.text.TextModelFeature;
import org.xidget.ifeature.text.ITextModelFeature;
import org.xidget.layout.Size;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetCreationFeature which creates a JTextField or JTextArea.
 */
public class JTextComponentWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public JTextComponentWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {    
    IModelObject element = xidget.getConfig();
    
    // create text widget
    Size size = new Size( Xlate.get( element, "size", (String)null));
    if ( element.isType( "password"))
    {
      jtext = new JPasswordField( size.width);
    }
    else if ( size.height > 1)
    {
      jtext = new JTextArea( size.height, size.width);
      component = new JScrollPane( jtext);
    }
    else if ( Xlate.get( element, "multiline", false))
    {
      jtext = new JTextArea();
      component = new JScrollPane( jtext);
    }
    else
    {
      jtext = new JTextField( size.width);
    }
    
    // pretty
    //jtext.setBorder( new EmptyBorder( 1, 1, 1, 1));
     
    // create extra container to hold label and widget
    if ( xidget.getConfig().getFirstChild( "label") != null)
    {
      jlabel = new JLabel( "");
      
      GridBagLayout layout = new GridBagLayout();
      
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.NORTHWEST;
      constraints.insets = new Insets( 0, 0, 0, 4);
      layout.setConstraints( jlabel, constraints);
      
      constraints = new GridBagConstraints();
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.NORTHWEST;
      constraints.weightx = 1;
      
      JPanel jPanel = new JPanel( layout);
      if ( component == null)
      {
        layout.setConstraints( jtext, constraints);
        jPanel.add( jlabel);
        jPanel.add( jtext);
      }
      else
      {
        layout.setConstraints( component, constraints);
        jPanel.add( jlabel);
        jPanel.add( component);
      }
      
      component = jPanel;
    }
    
    if ( component == null) component = jtext;
    
    // add listeners to the widget
    jtext.addKeyListener( keyListener);
    jtext.addCaretListener( caretListener);
    
    return component;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { component, jtext};
  }

  /**
   * Returns the container widget which holds the label and text widgets.
   * @return Returns the container widget which holds the label and text widgets.
   */
  public JComponent getContainer()
  {
    return component;
  }
    
  /**
   * Returns the text widget which may be different from the widget returned
   * by the <code>getWidget</code> method which will return a container if
   * a label is defined.
   * @return Returns the text widget.
   */
  public JTextComponent getTextWidget()
  {
    return jtext;
  }
  
  /**
   * Returns the label widget.
   * @return Returns null or the label widget.
   */
  public JLabel getLabelWidget()
  {
    return jlabel;
  }

  private final KeyListener keyListener = new KeyAdapter() {
    public void keyTyped( KeyEvent e)
    {
      SwingUtilities.invokeLater( updateRunnable);
    }
  };
    
  private final CaretListener caretListener = new CaretListener() {
    public void caretUpdate( CaretEvent e)
    {
      ITextModelFeature adapter = xidget.getFeature( ITextModelFeature.class);
      if ( adapter != null) adapter.setText( TextModelFeature.selectedChannel, jtext.getSelectedText());
    }
  };
  
  private final Runnable updateRunnable = new Runnable() {
    public void run()
    {
      ITextModelFeature adapter = xidget.getFeature( ITextModelFeature.class);
      if ( adapter != null) adapter.setText( TextModelFeature.allChannel, jtext.getText());
    }
  };

  private JComponent component;
  private JLabel jlabel;
  private JTextComponent jtext;
}
