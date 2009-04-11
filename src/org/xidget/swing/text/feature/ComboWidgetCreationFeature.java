/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text.feature;

import java.awt.Container;
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
import org.xidget.swing.ISwingWidgetFeature;
import org.xidget.swing.SwingCreationFeature;
import org.xidget.text.TextXidget;
import org.xidget.text.feature.ITextModelFeature;
import org.xmodel.IModelObject;

/**
 * An implementation of IWidgetCreationFeature which creates a JTextField or JTextArea.
 */
public class ComboWidgetCreationFeature extends SwingCreationFeature implements ISwingWidgetFeature
{
  public ComboWidgetCreationFeature( IXidget xidget)
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
    jcombo = new JComboBox();
    jcombo.setBorder( new EmptyBorder( 2, 3, 2, 3));
        
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
      
      container.add( component);
    }
    else
    {
      component = jcombo;
      container.add( jcombo);
    }
    
    // add listeners to the widget
    jcombo.addActionListener( actionListener);
    
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
      if ( feature != null) feature.setText( TextXidget.allChannel, jcombo.getSelectedItem().toString());
    }
  };

  private JComponent component;
  private JLabel jlabel;
  private JComboBox jcombo;
}