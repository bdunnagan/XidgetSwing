/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.combo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.xidget.IXidget;
import org.xidget.feature.text.TextModelFeature;
import org.xidget.ifeature.combo.IChoiceListFeature;
import org.xidget.ifeature.text.ITextModelFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetCreationFeature which creates a JTextField or JTextArea.
 */
public class JComboBoxWidgetCreationFeature extends SwingWidgetCreationFeature
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
    jcombo = new JComboBox();
    jcombo.setBorder( new EmptyBorder( 2, 3, 2, 3));

    // add statically defined choices if present
    addStaticChoices( xidget);
    
    // get label
    String label = Xlate.childGet( xidget.getConfig(), "label", (String)null);
    
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
      layout.setConstraints( jcombo, constraints);
      
      component = new JPanel( layout);
      component.add( jlabel);
      component.add( jcombo);
    }
    else
    {
      component = jcombo;
    }
    
    // add listeners to the widget
    jcombo.addActionListener( actionListener);
    
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
    return new Object[] { jcombo};
  }

  /**
   * Returns the JComboBox.
   * @return Returns the JComboBox.
   */
  public JComboBox getComboBox()
  {
    return jcombo;
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
      ITextModelFeature feature = xidget.getFeature( ITextModelFeature.class);
      if ( feature != null) feature.setText( TextModelFeature.allChannel, jcombo.getSelectedItem().toString());
    }
  };

  private JComponent component;
  private JLabel jlabel;
  private JComboBox jcombo;
}
