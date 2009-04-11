/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text.feature;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
import org.xidget.IXidget;
import org.xidget.config.util.Pair;
import org.xidget.swing.ISwingWidgetFeature;
import org.xidget.swing.SwingCreationFeature;
import org.xidget.text.TextXidget;
import org.xidget.text.feature.ITextModelFeature;
import org.xidget.text.feature.TextModelFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetCreationFeature which creates a JTextField or JTextArea.
 */
public class TextWidgetCreationFeature extends SwingCreationFeature implements ISwingWidgetFeature
{
  public TextWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.SwingWidgetHierarchyFeature#createSwingWidget(java.awt.Container, java.lang.String, org.xmodel.IModelObject)
   */
  @Override
  protected JComponent createSwingWidget( Container container, String label, IModelObject element)
  {    
    // create text widget
    Pair size = new Pair( Xlate.get( element, "size", Xlate.childGet( element, "size", "")), 0, 0);    
    if ( size.y > 1)
    {
      jtext = new JTextArea( size.y, size.x);
    }
    else
    {
      jtext = new JTextField( size.x);
      jtext.setBorder( new EmptyBorder( 2, 3, 2, 3));
    }
        
    // create extra container to hold label and widget
    if ( label != null)
    {
      jlabel = new JLabel( label);
      
      GridBagLayout layout = new GridBagLayout();
      
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      constraints.insets = new Insets( 0, 0, 0, 6);
      layout.setConstraints( jlabel, constraints);
      
      constraints = new GridBagConstraints();
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.anchor = GridBagConstraints.WEST;
      constraints.weightx = 1;
      layout.setConstraints( jtext, constraints);
      
      component = new JPanel( layout);
      component.add( jlabel);
      component.add( jtext);
      
      container.add( component);
    }
    else
    {
      component = jtext;
      container.add( jtext);
    }
    
    // add listeners to the widget
    jtext.addKeyListener( keyListener);
    jtext.addCaretListener( caretListener);
    
    return component;
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.ISwingWidgetFeature#getWidget()
   */
  public JComponent getWidget()
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
      if ( adapter != null) adapter.setText( TextXidget.allChannel, jtext.getText());
    }
  };

  private JComponent component;
  private JLabel jlabel;
  private JTextComponent jtext;
}