/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.tree;

import org.netbeans.swing.outline.RowModel;
import org.xmodel.IModelObject;

/**
 * An implementation of RowModel for use with the Netbeans Outline widget.
 */
public class CustomRowModel implements RowModel
{
  public CustomRowModel( int columns)
  {
    this.columns = columns;
  }
  
  /* (non-Javadoc)
   * @see org.netbeans.swing.outline.RowModel#getColumnClass(int)
   */
  @SuppressWarnings("unchecked")
  public Class getColumnClass( int column)
  {
    return IModelObject.class;
  }

  /* (non-Javadoc)
   * @see org.netbeans.swing.outline.RowModel#getColumnCount()
   */
  public int getColumnCount()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  /* (non-Javadoc)
   * @see org.netbeans.swing.outline.RowModel#getColumnName(int)
   */
  public String getColumnName( int column)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.netbeans.swing.outline.RowModel#getValueFor(java.lang.Object, int)
   */
  public Object getValueFor( Object node, int column)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.netbeans.swing.outline.RowModel#isCellEditable(java.lang.Object, int)
   */
  public boolean isCellEditable( Object node, int column)
  {
    // TODO Auto-generated method stub
    return false;
  }

  /* (non-Javadoc)
   * @see org.netbeans.swing.outline.RowModel#setValueFor(java.lang.Object, int, java.lang.Object)
   */
  public void setValueFor( Object node, int column, Object value)
  {
    // TODO Auto-generated method stub

  }
}
