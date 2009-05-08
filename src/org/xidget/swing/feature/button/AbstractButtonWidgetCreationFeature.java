/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.button;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import org.xidget.IXidget;
import org.xidget.ifeature.IScriptFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xmodel.Xlate;

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
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget(java.awt.Container)
   */
  @Override
  protected JComponent createSwingWidget( Container container)
  {
    String spec = Xlate.get( xidget.getConfig(), "type", "push");
    Type type = Enum.valueOf( Type.class, spec);
    switch( type)
    {
      case push:   button = new JButton(); break;
      case toggle: button = new JToggleButton(); break;
      case check:  button = new JCheckBox(); break;
      case radio:  button = createRadioButton(); break;
    }
    
    button.addActionListener( actionListener);
    container.add( button);
    
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
      IScriptFeature feature = xidget.getFeature( IScriptFeature.class);
      if ( feature != null) feature.runScript( "buttonPressed", xidget.getContext());
    }
  };
  
  private AbstractButton button;
  private ButtonGroup group;
}
