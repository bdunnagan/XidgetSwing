/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JComboBoxWidgetCreationFeature.java
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
package org.xidget.swing.combo;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.xidget.IXidget;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.IValueFeature;
import org.xidget.ifeature.combo.IChoiceListFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetCreationFeature which creates a JTextField or JTextArea.
 */
public class JComboBoxWidgetCreationFeature extends SwingWidgetCreationFeature implements ILabelFeature
{
  public JComboBoxWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {    
    // create text widget
    component = jCombo = new JComboBox();
    jCombo.setBorder( new EmptyBorder( 1, 1, 1, 1));

    // add statically defined choices if present
    addStaticChoices( xidget);
    
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
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.anchor = GridBagConstraints.WEST;
      constraints.weightx = 1;
      layout.setConstraints( jCombo, constraints);
      
      component = new JPanel( layout);
      component.add( jLabel);
      component.add( jCombo);
    }
    
    // add listeners to the widget
    jCombo.addActionListener( actionListener);
    
    return component;
  }

  /**
   * Add the static choices defined in choices/choice.
   * @param xidget The combo xidget.
   */
  private static void addStaticChoices( IXidget xidget)
  {
    IModelObject config = xidget.getConfig();
    IModelObject choices = config.getFirstChild( "choices");
    if ( choices != null)
    {
      IChoiceListFeature feature = xidget.getFeature( IChoiceListFeature.class);
      for( IModelObject choice: choices.getChildren())
        feature.addChoice( Xlate.get( choice, ""));
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    if ( component != jCombo) return new Object[] { component, jCombo};
    return new Object[] { jCombo};
  }

  /**
   * Returns the container component which will be a JPanel if a label is defined.
   * @return Returns the container component which will be a JPanel if a label is defined.
   */
  public JComponent getContainer()
  {
    return component;
  }
  
  /**
   * Returns the JComboBox.
   * @return Returns the JComboBox.
   */
  public JComboBox getComboBox()
  {
    return jCombo;
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
   * @see org.xidget.ifeature.ILabelFeature#setLabelText(java.lang.String)
   */
  public void setLabelText( String text)
  {
    if ( jLabel != null) jLabel.setText( text);
  }
  
  private final ActionListener actionListener = new ActionListener() {
    public void actionPerformed( ActionEvent e)
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
  
  private JComponent component;
  private JLabel jLabel;
  private JComboBox jCombo;
}
