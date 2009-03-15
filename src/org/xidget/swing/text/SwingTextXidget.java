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
import org.xidget.config.processor.TagException;
import org.xidget.config.util.Pair;
import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.swing.ISwingWidgetFeature;
import org.xidget.swing.SwingContainerXidget;
import org.xidget.swing.SwingWidgetFeature;
import org.xidget.swing.feature.SwingTooltipErrorFeature;
import org.xidget.swing.text.feature.TextFeature;
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
    component = createWidget( container, size);
    
    // add listeners to the widget
    component.addKeyListener( keyListener);
    component.addCaretListener( caretListener);
    
    // create features
    widgetFeature = new SwingWidgetFeature( component);
    errorFeature = new SwingTooltipErrorFeature( component);
    textFeature = new TextFeature( component);
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
    if ( clss.equals( IWidgetTextFeature.class)) return (T)textFeature;
    if ( clss.equals( ISwingWidgetFeature.class)) return (T)widgetFeature;
    if ( clss.equals( IWidgetFeature.class)) return (T)widgetFeature;
    if ( clss.equals( IErrorFeature.class)) return (T)errorFeature;    
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
      if ( adapter != null) adapter.setText( TextXidget.selectedChannel, component.getSelectedText());
    }
  };
  
  private final Runnable updateRunnable = new Runnable() {
    public void run()
    {
      IModelTextFeature adapter = getFeature( IModelTextFeature.class);
      if ( adapter != null) adapter.setText( TextXidget.allChannel, component.getText());
    }
  };
  
  private JTextComponent component;
  private TextFeature textFeature;
  private SwingWidgetFeature widgetFeature;
  private SwingTooltipErrorFeature errorFeature;
}
