/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JTextComponentWidgetCreationFeature.java
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
package org.xidget.swing.text;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.xidget.IXidget;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.IValueFeature;
import org.xidget.layout.Size;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetCreationFeature which creates a JTextField or JTextArea.
 */
public class JTextComponentWidgetCreationFeature extends SwingWidgetCreationFeature implements ILabelFeature
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
      jText = new JPasswordField( size.width);
      jText.setBorder( BorderFactory.createEmptyBorder( 1, 1, 1, 1));
    }
    else if ( size.height > 1)
    {
      jText = new JTextArea( size.height, size.width);
      jText.setBorder( BorderFactory.createEmptyBorder( 1, 1, 1, 1));
      component = new JScrollPane( jText);
      component.setBorder( BorderFactory.createEmptyBorder());
    }
    else if ( Xlate.get( element, "multiline", false))
    {
      jText = new JTextArea();
      jText.setBorder( BorderFactory.createEmptyBorder( 1, 1, 1, 1));
      component = new JScrollPane( jText);
      component.setBorder( BorderFactory.createEmptyBorder());
    }
    else
    {
      jText = new JTextField( size.width);
      jText.setBorder( BorderFactory.createEmptyBorder( 1, 1, 1, 1));
      
      // add action listener so that enter key will be processed
      ((JTextField)jText).addActionListener( actionListener);
    }
    
    // create extra container to hold label and widget
    if ( xidget.getConfig().getFirstChild( "label") != null)
    {
      jLabel = new JLabel( "");
      jLabel.setHorizontalAlignment( SwingConstants.RIGHT);
      
      GridBagLayout layout = new GridBagLayout();
      
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.fill = GridBagConstraints.VERTICAL;
      constraints.anchor = GridBagConstraints.WEST;
      constraints.insets = new Insets( 0, 0, 0, 4);
      layout.setConstraints( jLabel, constraints);
      
      constraints = new GridBagConstraints();
      constraints.fill = GridBagConstraints.BOTH;
      constraints.anchor = GridBagConstraints.NORTHWEST;
      constraints.weightx = 1;
      
      JPanel jPanel = new JPanel( layout);
      if ( component == null)
      {
        layout.setConstraints( jText, constraints);
        jPanel.add( jLabel);
        jPanel.add( jText);
      }
      else
      {
        layout.setConstraints( component, constraints);
        jPanel.add( jLabel);
        jPanel.add( component);
      }
      
      component = jPanel;
    }
    
    if ( component == null) component = jText;
    
    jText.addFocusListener( focusListener);
    
    return component;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    if ( component != jText) return new Object[] { component, jText};
    return new Object[] { jText};
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
    return jText;
  }
  
  /**
   * Returns the label widget.
   * @return Returns null or the label widget.
   */
  public JLabel getLabelWidget()
  {
    return jLabel;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#setWidth(int)
   */
  public void setLabelWidth( int width)
  {
    if ( jLabel == null) return;
    Dimension size = jLabel.getPreferredSize();
    jLabel.setPreferredSize( new Dimension( width, size.height));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#getLabelWidth()
   */
  public int getLabelWidth()
  {
    if ( jLabel == null) return 0;
    Dimension size = jLabel.getPreferredSize();
    return size.width;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#setText(java.lang.String)
   */
  public void setLabelText( String text)
  {
    if ( jLabel != null) jLabel.setText( text);
  }
      
  private final FocusListener focusListener = new FocusListener() {
    public void focusGained( FocusEvent event) 
    {
    }
    public void focusLost( FocusEvent event) 
    {
      SwingUtilities.invokeLater( updateRunnable);
    }
  };
  
  private final Runnable updateRunnable = new Runnable() {
    public void run()
    {
      IValueFeature feature = xidget.getFeature( IValueFeature.class);
      feature.commit();
    }
  };
  
  private final ActionListener actionListener = new ActionListener() {
    public void actionPerformed( ActionEvent e)
    {
      SwingUtilities.invokeLater( updateRunnable);
    }
  };

  private JComponent component;
  private JLabel jLabel;
  private JTextComponent jText;
}
