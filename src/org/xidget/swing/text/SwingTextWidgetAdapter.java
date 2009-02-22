package org.xidget.swing.text;

import javax.swing.text.JTextComponent;
import org.xidget.widget.ITextWidgetAdapter;

/**
 * Display text in a JTextComponent.
 */
public class SwingTextWidgetAdapter implements ITextWidgetAdapter
{
  public SwingTextWidgetAdapter( JTextComponent widget)
  {
    this.widget = widget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.widget.IWidgetAdapter#setEnabled(boolean)
   */
  public void setEnabled( boolean enabled)
  {
    widget.setEnabled( enabled);
  }

  /* (non-Javadoc)
   * @see org.xidget.widget.ITextWidgetAdapter#setEditable(boolean)
   */
  public void setEditable( boolean editable)
  {
    widget.setEditable( editable);
  }

  /* (non-Javadoc)
   * @see org.xidget.adapter.ITextDisplayAdapter#setText(java.lang.String)
   */
  public void setText( String text)
  {
    widget.setText( text);
  }
  
  private JTextComponent widget;
}
