/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import org.xidget.IToolkit;
import org.xidget.binding.XidgetTagHandler;
import org.xidget.config.TagProcessor;
import org.xidget.swing.text.JComboBoxXidget;
import org.xidget.swing.text.JTextXidget;

/**
 * An implementation of IToolkit for the Swing platform.
 */
public class SwingToolkit implements IToolkit
{
  /* (non-Javadoc)
   * @see org.xidget.IToolkit#configure(org.xidget.config.TagProcessor)
   */
  public void configure( TagProcessor processor)
  {
    processor.addHandler( "app", new XidgetTagHandler( JFrameXidget.class));
    processor.addHandler( "application", new XidgetTagHandler( JFrameXidget.class));
    processor.addHandler( "form", new XidgetTagHandler( JPanelXidget.class));
    processor.addHandler( "text", new XidgetTagHandler( JTextXidget.class));
    processor.addHandler( "combo", new XidgetTagHandler( JComboBoxXidget.class));
  }
}
