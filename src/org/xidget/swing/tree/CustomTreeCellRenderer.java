/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.tree;

import java.awt.Component;
import java.util.List;
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
  public CustomTreeCellRenderer()
  {
    sb = new StringBuilder();
  }
  
  /* (non-Javadoc)
   * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
   */
  @Override
  public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int rowIndex, boolean hasFocus)
  {
    Component component = super.getTreeCellRendererComponent( tree, value, selected, expanded, leaf, rowIndex, hasFocus);
    
    Row row = (Row)value;
    Cell firstCell = row.getCell( 0);
    setIcon( (Icon)firstCell.icon);
    
    sb.setLength( 0);
    List<Cell> cells = row.getCells();
    for( Cell cell: cells)
    {
      if ( cell != firstCell) sb.append( ", ");
      sb.append( (cell.text != null)? cell.text: "");
    }
    
    // remove trailing commas
    for( int i = cells.size() - 1; i > 0; i--)
    {
      Cell cell = cells.get( i);
      if ( cell.text != null && cell.text.length() > 0) break;
      sb.setLength( sb.length() - 2);
    }
    
    setText( sb.toString());
    
    return component;
  }
  
  private StringBuilder sb;
}
