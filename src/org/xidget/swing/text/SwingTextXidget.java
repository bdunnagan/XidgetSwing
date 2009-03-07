package org.xidget.swing.text;

import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
import org.xidget.IWidgetAdapter;
import org.xidget.config.processor.TagException;
import org.xidget.config.util.Pair;
import org.xidget.feature.IErrorFeature;
import org.xidget.swing.ISwingWidgetAdapter;
import org.xidget.swing.SwingContainerXidget;
import org.xidget.swing.SwingWidgetAdapter;
import org.xidget.swing.adapter.SwingTooltipErrorAdapter;
import org.xidget.swing.text.feature.TextAdapter;
import org.xidget.text.TextXidget;
import org.xidget.text.feature.IModelTextFeature;
import org.xidget.text.feature.IWidgetTextFeature;

/**
 * An implementation of TextXidget for a Swing text widget.
 */
public class SwingTextXidget extends TextXidget
{
  /* (non-Javadoc)
   * @see org.xidget.TextXidget#createWidget(org.xidget.config.util.Size)
   */
  @Override
  protected void createWidget( Pair size) throws TagException
  {
    if ( !(getParent() instanceof SwingContainerXidget)) 
      throw new TagException( "Expected SwingContainerXidget instead of: "+
        getParent().getClass().getSimpleName());
    
    SwingContainerXidget parent = (SwingContainerXidget)getParent();
    Container container = parent.getContainer();
    widget = createWidget( container, size);
    
    // add listeners to the widget
    widget.addKeyListener( keyListener);
    widget.addCaretListener( caretListener);
  }

  /**
   * Create a JTextComponent with the specified number of rows and columns initially.
   * @param rows The number of rows.
   * @param columns The number of columns.
   * @return Returns the new widget.
   */
  protected JTextComponent createWidget( Container container, Pair size)
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
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss.equals( IWidgetTextFeature.class)) return (T)new TextAdapter( widget);
    if ( clss.equals( ISwingWidgetAdapter.class)) return (T)new SwingWidgetAdapter( widget);
    if ( clss.equals( IWidgetAdapter.class)) return (T)new SwingWidgetAdapter( widget);
    if ( clss.equals( IErrorFeature.class)) return (T)new SwingTooltipErrorAdapter( widget);    
    return super.getFeature( clss);
  }

  private final KeyListener keyListener = new KeyAdapter() {
    public void keyTyped( KeyEvent e)
    {
      SwingUtilities.invokeLater( updateRunnable);
    }
  };
    
  private final CaretListener caretListener = new CaretListener() {
    public void caretUpdate( CaretEvent e)
    {
      IModelTextFeature adapter = getFeature( IModelTextFeature.class);
      if ( adapter != null) adapter.setText( TextXidget.selectedChannel, widget.getSelectedText());
    }
  };
  
  private final Runnable updateRunnable = new Runnable() {
    public void run()
    {
      IModelTextFeature adapter = getFeature( IModelTextFeature.class);
      if ( adapter != null) adapter.setText( TextXidget.allChannel, widget.getText());
    }
  };
  
  private JTextComponent widget;
}
