package org.xidget.swing.adapter;

import javax.swing.JComponent;
import org.xidget.adapter.IErrorAdapter;

/**
 * An IErrorAdapter which displays the error in a tooltip on a Swing component.
 * The adapter will restore the original tooltip text when the error is cleared.
 * This adapter is not suitable if the tooltip text is dynamic.
 */
public class SwingTooltipErrorAdapter implements IErrorAdapter
{
  public SwingTooltipErrorAdapter( JComponent widget)
  {
    this.widget = widget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.adapter.IErrorAdapter#structureError(java.lang.String)
   */
  public void structureError( String message)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.adapter.IErrorAdapter#valueError(java.lang.String)
   */
  public void valueError( String message)
  {
    if ( message == null || message.length() == 0)
    {
      widget.setToolTipText( tooltip);
      tooltip = null;
    }
    else
    {
      if ( tooltip == null) tooltip = widget.getToolTipText();
      widget.setToolTipText( "Error: "+message);
    }
  }
  
  private JComponent widget;
  private String tooltip;
}
