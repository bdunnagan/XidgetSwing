/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.button;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import org.xidget.IXidget;
import org.xidget.ifeature.IScriptFeature;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.ifeature.button.IButtonModelFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IWidgetCreationFeature which creates a concrete implementation of Swing AbstractButton.
 */
public class AbstractButtonWidgetCreationFeature extends SwingWidgetCreationFeature
{
  private enum Type { push, toggle, check, radio};
  
  public AbstractButtonWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    // create button
    String spec = Xlate.get( xidget.getConfig(), "type", "push");
    Type type = Enum.valueOf( Type.class, spec);
    switch( type)
    {
      case push:   button = new JButton(); break;
      case toggle: button = new JToggleButton(); break;
      case check:  button = new JCheckBox(); break;
      case radio:  button = createRadioButton(); break;
    }
    
    button.setMargin( new Insets( 0, 0, 0, 0));
    
    //
    // HACK:
    // Force the button to use center alignment so that it will work seamlessly with 
    // the DefaultTableCellRenderer for boolean values when used as a table cell editor.
    //
    button.setHorizontalAlignment( SwingConstants.CENTER);
    
    // set button label
    String label = Xlate.childGet( xidget.getConfig(), "label", (String)null);
    if ( label != null) button.setText( label);
    
    // add button listener
    button.addActionListener( actionListener);
    button.addItemListener( itemListener);
    
    return button;
  }
  
  /**
   * Create the radio button and configure it with respect to other contiguous radio buttons.
   * @return Returns the new radio button.
   */
  private AbstractButton createRadioButton()
  {
    JRadioButton button = new JRadioButton();
    
    List<IXidget> children = xidget.getParent().getChildren();
    
    // get index of this radio button
    int index = children.indexOf( xidget);
    if ( index == -1) index = children.size();
    
    // see if previous xidget is a radio button
    if ( index > 0)
    {
      IXidget previous = children.get( index - 1);
      group = previous.getFeature( ButtonGroup.class);
    }
    
    // create group if needed
    if ( group == null) group = new ButtonGroup();
    
    // add this button to the group
    group.add( button);
    
    return button;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { button};
  }

  /**
   * Returns the AbstractButton that was created.
   * @return Returns the AbstractButton that was created.
   */
  public AbstractButton getAbstractButton()
  {
    return button;
  }
  
  /**
   * Returns the ButtonGroup to which this radio button belongs.
   * @return Returns the ButtonGroup to which this radio button belongs.
   */
  public ButtonGroup getButtonGroup()
  {
    return group;
  }
    
  private ActionListener actionListener = new ActionListener() {
    public void actionPerformed( ActionEvent e)
    {
      IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
      if ( scriptFeature != null) 
      {
        IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
        StatefulContext context = contextFeature.getContext( e.getSource());
        if ( context != null) scriptFeature.runScript( "onPress", context);
      }
    }
  };
  
  private ItemListener itemListener = new ItemListener() {
    public void itemStateChanged( ItemEvent e)
    {
      IButtonModelFeature feature = xidget.getFeature( IButtonModelFeature.class);
      if ( feature != null) feature.setState( e.getStateChange() == ItemEvent.SELECTED);
    }
  };
  
  private AbstractButton button;
  private ButtonGroup group;
}
