/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table.renderer;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.xidget.IXidget;
import org.xidget.table.features.ITableModelFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;

/**
 * A TableCellRenderer to be used as the table column header renderer. This 
 * implementation pulls the column header icon from the model. It gets the 
 * row object from the ITableModelFeature, evaluates the icon expression and 
 * sets the icon. 
 */
@SuppressWarnings("serial")
public class CustomHeaderCellRenderer extends DefaultTableCellRenderer
{
  /**
   * Create a custom cell renderer for the specified column xidget.
   * @param xidget The column xidget.
   */
  public CustomHeaderCellRenderer( IXidget xidget)
  {
    this.xidget = xidget;
    iconExpr = getIconExpression( xidget);
    titleExpr = getTitleExpression( xidget);
  }
  
  /**
   * Returns the icon expression from the config of the specified column xidget.
   * @param xidget The column xidget.
   * @return Returns the icon expression from the config of the specified column xidget.
   */
  private static IExpression getIconExpression( IXidget xidget)
  {
    IModelObject config = xidget.getConfig();
    return Xlate.childGet( config, "image", (IExpression)null);
  }
  
  /**
   * Returns the title expression from the config of the specified column xidget.
   * @param xidget The column xidget.
   * @return Returns the title expression from the config of the specified column xidget.
   */
  private static IExpression getTitleExpression( IXidget xidget)
  {
    IModelObject config = xidget.getConfig();
    return Xlate.childGet( config, "title", (IExpression)null);
  }
  
  /* (non-Javadoc)
   * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(
   * javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
   */
  @Override
  public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    // allow super-class to do its thing
    super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column);
    
    // consult table model for header title and icon
    ITableModelFeature modelFeature = xidget.getFeature( ITableModelFeature.class);
    IModelObject object = modelFeature.getRow( row);
    if ( object != null)
    {
      if ( iconExpr != null)
      {
        IModelObject iconNode = iconExpr.queryFirst( object);
        setIcon( (Icon)iconNode.getValue());
      }
      
      if ( titleExpr != null)
      {
        IModelObject titleNode = titleExpr.queryFirst( object);
        String title = Xlate.get( titleNode, ""); 
        setName( title); // this I found online.
        setText( title); // is this needed?
      }
    }
        
    return this;
  }
  
  private IXidget xidget;
  private IExpression iconExpr;
  private IExpression titleExpr;
}
