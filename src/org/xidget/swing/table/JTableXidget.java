/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.table;

import org.xidget.IXidget;
import org.xidget.binding.XidgetTagHandler;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetCreationFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.swing.ISwingWidgetFeature;
import org.xidget.swing.SwingWidgetFeature;
import org.xidget.swing.feature.SwingTooltipErrorFeature;
import org.xidget.swing.table.column.JTableColumnXidget;
import org.xidget.swing.table.feature.JTableWidgetCreationFeature;
import org.xidget.swing.table.feature.JTableWidgetFeature;
import org.xidget.table.TableXidget;
import org.xidget.table.features.ITableModelFeature;
import org.xidget.table.features.ITableWidgetFeature;
import org.xidget.table.features.TableModelFeature;
import org.xmodel.IModelObject;

/**
 * An implementation of TableXidget for use with the JTable widget.
 */
public class JTableXidget extends TableXidget
{  
  public JTableXidget()
  {
    columnTagHandler = new XidgetTagHandler( JTableColumnXidget.class);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#startConfig(org.xidget.config.processor.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  @Override
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    processor.addHandler( "column", columnTagHandler);
    return super.startConfig( processor, parent, element);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#endConfig(org.xidget.config.processor.TagProcessor, org.xmodel.IModelObject)
   */
  @Override
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException
  {
    processor.removeHandler( "column", columnTagHandler);
    super.endConfig( processor, element);
  }

  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#getWidgetCreationFeature()
   */
  @Override
  protected IWidgetCreationFeature getWidgetCreationFeature()
  {
    widgetCreationFeature = new JTableWidgetCreationFeature( this);
    return widgetCreationFeature;
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
    return new TableModelFeature();
  }

  /* (non-Javadoc)
   * @see org.xidget.table.TableXidget#getTableWidgetFeature()
   */
  @Override
  protected ITableWidgetFeature getTableWidgetFeature()
  {
    return new JTableWidgetFeature( this);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.TableXidget#getWidgetFeature()
   */
  @Override
  protected IWidgetFeature getWidgetFeature()
  {
    ISwingWidgetFeature widgetFeature = getFeature( ISwingWidgetFeature.class);
    return new SwingWidgetFeature( widgetFeature.getWidget());
  }

  /* (non-Javadoc)
   * @see org.xidget.table.TableXidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == ISwingWidgetFeature.class) return (T)widgetCreationFeature;
    return super.getFeature( clss);
  }

  private XidgetTagHandler columnTagHandler;
  private JTableWidgetCreationFeature widgetCreationFeature;
}
