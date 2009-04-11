/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import javax.swing.JComponent;
import javax.swing.JTable;
import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetCreationFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.swing.ISwingWidgetFeature;
import org.xidget.swing.feature.SwingTooltipErrorFeature;
import org.xidget.table.TableXidget;
import org.xidget.table.features.ITableModelFeature;
import org.xidget.table.features.ITableWidgetFeature;
import org.xmodel.IModelObject;

/**
 * An implementation of TableXidget for use with the JTable widget.
 */
public class SwingTableXidget extends TableXidget implements IWidgetCreationFeature, ISwingWidgetFeature
{  
  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#createWidget(java.lang.String, org.xmodel.IModelObject)
   */
  public void createWidget( String label, IModelObject element)
  {
    tableModel = new CustomTableModel( this);
    table = new JTable( tableModel);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidget()
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.ISwingWidgetFeature#getWidget()
   */
  public JComponent getWidget()
  {
    return table;
  }

  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#getWidgetCreationFeature()
   */
  @Override
  protected IWidgetCreationFeature getWidgetCreationFeature()
  {
    return this;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.TableXidget#getErrorFeature()
   */
  @Override
  protected IErrorFeature getErrorFeature()
  {
    return new SwingTooltipErrorFeature( this);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.TableXidget#getTableModelFeature()
   */
  @Override
  protected ITableModelFeature getTableModelFeature()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.TableXidget#getTableWidgetFeature()
   */
  @Override
  protected ITableWidgetFeature getTableWidgetFeature()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.TableXidget#getWidgetFeature()
   */
  @Override
  protected IWidgetFeature getWidgetFeature()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.TableXidget#getFeature(java.lang.Class)
   */
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    // TODO Auto-generated method stub
    return super.getFeature( clss);
  }

  private JTable table;
  private CustomTableModel tableModel;
}
