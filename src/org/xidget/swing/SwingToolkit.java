/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import org.xidget.IToolkit;
import org.xidget.binding.XidgetTagHandler;
import org.xidget.binding.table.TableTagHandler;
import org.xidget.binding.tree.TreeTagHandler;
import org.xidget.config.TagProcessor;
import org.xidget.swing.button.AbstractButtonXidget;
import org.xidget.swing.combo.JComboBoxXidget;
import org.xidget.swing.dialog.JDialogXidget;
import org.xidget.swing.menu.JMenuItemXidget;
import org.xidget.swing.menu.MenuTagHandler;
import org.xidget.swing.menu.SubMenuTagHandler;
import org.xidget.swing.table.JTableXidget;
import org.xidget.swing.text.JTextXidget;
import org.xidget.swing.tree.JTreeXidget;
import org.xmodel.external.ICachingPolicy;

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
    processor.addHandler( "dialog", new XidgetTagHandler( JDialogXidget.class));
    processor.addHandler( "form", new XidgetTagHandler( JPanelXidget.class));
    processor.addHandler( "tabs", new XidgetTagHandler( JTabbedPaneXidget.class));
    processor.addHandler( "text", new XidgetTagHandler( JTextXidget.class));
    processor.addHandler( "combo", new XidgetTagHandler( JComboBoxXidget.class));
    processor.addHandler( "button", new XidgetTagHandler( AbstractButtonXidget.class));
    processor.addHandler( "menu", new MenuTagHandler());
    processor.addHandler( "menu", new SubMenuTagHandler());
    processor.addHandler( "menuItem", new XidgetTagHandler( JMenuItemXidget.class));
    processor.addHandler( "separator", new XidgetTagHandler( JMenuItemXidget.class));
    processor.addHandler( "table", new TableTagHandler( JTableXidget.class));
    processor.addHandler( "tree", new TreeTagHandler( JTreeXidget.class));
  }

  /* (non-Javadoc)
   * @see org.xidget.IToolkit#getImageCachingPolicy()
   */
  public ICachingPolicy getImageCachingPolicy()
  {
    return null;
  }
}
