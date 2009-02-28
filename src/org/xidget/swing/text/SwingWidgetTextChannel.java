package org.xidget.swing.text;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.xidget.text.IWidgetTextChannel;

/**
 * Display text in a JTextComponent.
 */
public class SwingWidgetTextChannel implements IWidgetTextChannel
{
  public SwingWidgetTextChannel( JTextComponent widget)
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
    
  /* (non-Javadoc)
   * @see org.xidget.text.IWidgetTextChannel#addListener(org.xidget.text.IWidgetTextChannel.IListener)
   */
  public void addListener( IListener listener)
  {
    if ( listeners == null) listeners = new ArrayList<IListener>( 1);
    listeners.add( listener);
    widget.getDocument().addDocumentListener( documentListener);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.IWidgetTextChannel#removeListener(org.xidget.text.IWidgetTextChannel.IListener)
   */
  public void removeListener( IListener listener)
  {
    listeners.remove( listener);
  }

  /**
   * Notify listeners that the text has changed.
   */
  private void notifyTextChanged()
  {
    if ( listeners != null)
    {
      String text = widget.getText();
      for( IListener listener: listeners)
        listener.onTextChanged( text);
    }
  }
  
  private DocumentListener documentListener = new DocumentListener() {
    public void changedUpdate( DocumentEvent e)
    {
      notifyTextChanged();
    }
    public void insertUpdate( DocumentEvent e)
    {
      notifyTextChanged();
    }
    public void removeUpdate( DocumentEvent e)
    {
      notifyTextChanged();
    }
  };
  
  private JTextComponent widget;
  private List<IListener> listeners;
}
