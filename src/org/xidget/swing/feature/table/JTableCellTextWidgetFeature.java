/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.table;

import org.xidget.IXidget;
import org.xidget.config.util.TextTransform;
import org.xidget.ifeature.table.IRowSetFeature;
import org.xidget.ifeature.table.ITableWidgetFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;
import org.xidget.text.TextXidget;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of ICellTextWidgetFeature for cells in a JTable.
 */
public class JTableCellTextWidgetFeature implements ITextWidgetFeature
{
  public JTableCellTextWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;

    // determine column index from config element
    IModelObject element = xidget.getConfig();
    this.column = element.getParent().getChildren( element.getType()).indexOf( element);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.ITextWidgetFeature#setEditable(boolean)
   */
  public void setEditable( boolean editable)
  {
    ITableWidgetFeature widgetFeature = xidget.getParent().getFeature( ITableWidgetFeature.class);
    widgetFeature.setEditable( getCurrentRow(), column, editable);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.ITextWidgetFeature#setText(java.lang.String, java.lang.String)
   */
  public void setText( String channel, String text)
  {
    if ( channel.equals( TextXidget.allChannel))
    {
      if ( transform != null) text = transform.transform( text);
      
      ITableWidgetFeature widgetFeature = xidget.getParent().getFeature( ITableWidgetFeature.class);
      widgetFeature.setText( getCurrentRow(), column, text);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.ITextWidgetFeature#setTransform(java.lang.String, org.xmodel.xpath.expression.IExpression)
   */
  public void setTransform( String channel, IExpression expression)
  {
    transform = new TextTransform( expression);
  }

  /**
   * Returns the current row being processed.
   * @return Returns the current row being processed.
   */
  private int getCurrentRow()
  {
    IRowSetFeature rowSetFeature = xidget.getParent().getFeature( IRowSetFeature.class);
    return rowSetFeature.getCurrentRow();
  }
  
  private IXidget xidget;
  private int column;
  private TextTransform transform;
}
