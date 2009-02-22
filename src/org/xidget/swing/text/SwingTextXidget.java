package org.xidget.swing.text;

import javax.swing.text.JTextComponent;
import org.xidget.IXidget;
import org.xidget.TextXidget;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.swing.adapter.SwingTooltipErrorAdapter;
import org.xidget.widget.ITextWidgetAdapter;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of TextXidget for a Swing text widget.
 */
public class SwingTextXidget extends TextXidget
{
  public SwingTextXidget()
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#startConfig(org.xidget.config.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean startConfig( TagProcessor processor, IXidget xidget, IModelObject element) throws TagException
  {
    int rows = Xlate.get( element, "rows", -1);
    int columns = Xlate.get( element, "columns", -1);
    widget = createWidget( rows, columns);
    return true;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IXidget#endConfig(org.xidget.config.TagProcessor)
   */
  public void endConfig( TagProcessor processor) throws TagException
  {
  }

  /**
   * Create a JTextComponent with the specified number of rows and columns initially.
   * @param rows The number of rows.
   * @param columns The number of columns.
   * @return Returns the new widget.
   */
  protected JTextComponent createWidget( int rows, int columns)
  {
    return null;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAdapter(java.lang.Class)
   */
  public Object getAdapter( Class<? extends Object> clss)
  {
    if ( clss.equals( ITextWidgetAdapter.class)) return new SwingTextWidgetAdapter( widget);
    if ( clss.equals( ITextWidgetAdapter.class)) return new SwingTooltipErrorAdapter( widget);
    return null;
  }
  
  private JTextComponent widget;
}
