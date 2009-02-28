package org.xidget.swing.text;

import java.awt.Container;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.xidget.config.TagException;
import org.xidget.config.util.Size;
import org.xidget.swing.SwingContainerXidget;
import org.xidget.swing.adapter.SwingTooltipErrorAdapter;
import org.xidget.text.ITextWidgetAdapter;
import org.xidget.text.TextXidget;

/**
 * An implementation of TextXidget for a Swing text widget.
 */
public class SwingTextXidget extends TextXidget
{
  public SwingTextXidget()
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.TextXidget#createWidget(org.xidget.config.util.Size)
   */
  @Override
  protected void createWidget( Size size) throws TagException
  {
    if ( !(getParent() instanceof SwingContainerXidget)) 
      throw new TagException( "Expected SwingContainerXidget instead of: "+
        getParent().getClass().getSimpleName());
    
    SwingContainerXidget parent = (SwingContainerXidget)getParent();
    Container container = parent.getContainer();
    widget = createWidget( container, size);
  }

  /**
   * Create a JTextComponent with the specified number of rows and columns initially.
   * @param rows The number of rows.
   * @param columns The number of columns.
   * @return Returns the new widget.
   */
  protected static JTextComponent createWidget( Container container, Size size)
  {
    if ( size.y > 1)
    {
      JTextArea widget = new JTextArea( size.y, size.x);
      container.add( widget);
      return widget;
    }
    else
    {
      JTextField widget = new JTextField( size.x);
      container.add( widget);
      return widget;
    }
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
