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
package org.xidget.swing.spinner;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xidget.IXidget;
import org.xidget.feature.text.TextModelFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.combo.IChoiceListFeature;
import org.xidget.ifeature.text.ITextModelFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetCreationFeature which creates a JTextField or JTextArea.
 */
public class JSpinnerWidgetCreationFeature extends SwingWidgetCreationFeature implements ILabelFeature
{
  public JSpinnerWidgetCreationFeature( IXidget xidget)
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
    component = jSpinner = new JSpinner();
    jSpinner.setBorder( new EmptyBorder( 1, 1, 1, 1));

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
      layout.setConstraints( jSpinner, constraints);
      
      component = new JPanel( layout);
      component.add( jLabel);
      component.add( jSpinner);
    }
    
    // add listeners to the widget
    jSpinner.addChangeListener( changeListener);
    
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
    if ( component != jSpinner) return new Object[] { component, jSpinner};
    return new Object[] { jSpinner};
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
  public JSpinner getJSpinner()
  {
    return jSpinner;
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
  
  private final ChangeListener changeListener = new ChangeListener() {
    public void stateChanged( ChangeEvent e)
    {
      SwingUtilities.invokeLater( updateRunnable);      
    }
  };
        
  private final Runnable updateRunnable = new Runnable() {
    public void run()
    {
      ITextModelFeature textModelFeature = xidget.getFeature( ITextModelFeature.class);
      if ( textModelFeature != null) 
      {
        IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
        textModelFeature.setText( bindFeature.getBoundContext(), TextModelFeature.allChannel, jSpinner.getValue().toString());
      }
    }
  };
  
  private JComponent component;
  private JLabel jLabel;
  private JSpinner jSpinner;
}