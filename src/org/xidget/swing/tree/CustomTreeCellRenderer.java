/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * CustomTreeCellRenderer.java
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
package org.xidget.swing.tree;

import java.awt.Component;
import java.awt.Image;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.xidget.swing.util.IconCache;
import org.xidget.tree.Cell;
import org.xidget.tree.Row;

/**
 * An implementation of TreeCellRenderer for rendering the cells of a tree.
 */
@SuppressWarnings("serial")
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
    if ( row.getParent() == null) 
    {
      setText( "ROOT");
      return component;
    }
    
    Cell firstCell = row.getCell( 0);
    setIcon( IconCache.getInstance().getIcon( firstCell.image));
    
    sb.setLength( 0);
    List<Cell> cells = row.getCells();
    for( Cell cell: cells)
    {
      if ( cell != firstCell) sb.append( ", ");
      sb.append( (cell.value != null)? cell.value: "");
    }
    
    // remove trailing commas
    for( int i = cells.size() - 1; i > 0; i--)
    {
      Cell cell = cells.get( i);
      String text = (cell.value != null)? cell.value.toString(): "";
      if ( text.length() > 0) break;
      sb.setLength( sb.length() - 2);
    }
    
    setText( sb.toString());
    
    return component;
  }
  
  private StringBuilder sb;
}
