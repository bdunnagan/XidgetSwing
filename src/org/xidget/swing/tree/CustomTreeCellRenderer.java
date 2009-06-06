/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.tree;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.xidget.tree.Cell;
import org.xidget.tree.Row;

/**
 * An implementatino of TreeCellRenderer for rendering the cells of a tree.
 */
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer
{
  /* (non-Javadoc)
   * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
   */
  @Override
  public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int rowIndex, boolean hasFocus)
  {
    Component component = super.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, rowIndex, hasFocus);
    
    Row row = (Row)value;
    Cell cell = row.getCell( 0);
    setIcon( (Icon)cell.icon);
    setText( ((cell.text != null)? cell.text: ""));
    
    return component;
  }
}
