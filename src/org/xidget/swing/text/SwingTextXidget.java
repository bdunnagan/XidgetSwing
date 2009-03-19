package org.xidget.swing.text;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
import org.xidget.IXidget;
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
import org.xidget.text.feature.ModelTextFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of TextXidget for a Swing text widget.
 */
public class SwingTextXidget extends TextXidget
{  
  /* (non-Javadoc)
   * @see org.xidget.text.TextXidget#build(org.xidget.IXidget, String, org.xmodel.IModelObject)
   */
  @Override
  protected final void build( IXidget parent, String label, IModelObject element) throws TagException
  {
    if ( !(getParent() instanceof SwingContainerXidget)) 
      throw new TagException( "Expected SwingContainerXidget instead of: "+
        getParent().getClass().getSimpleName());
    
    Container container = ((SwingContainerXidget)parent).getContainer();

    // create text widget
    Pair size = new Pair( Xlate.get( element, "size", Xlate.childGet( element, "size", "")), 0, 0);    
    if ( size.y > 1)
    {
      jtext = new JTextArea( size.y, size.x);
    }
    else
    {
      jtext = new JTextField( size.x);
      jtext.setBorder( new EmptyBorder( 2, 3, 2, 3));
    }
        
    // create extra container to hold label and widget
    if ( label != null)
    {
      jlabel = new JLabel( label);
      
      GridBagLayout layout = new GridBagLayout();
      
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.fill = GridBagConstraints.NONE;
      constraints.anchor = GridBagConstraints.WEST;
      constraints.insets = new Insets( 0, 0, 0, 6);
      layout.setConstraints( jlabel, constraints);
      
      constraints = new GridBagConstraints();
      constraints.fill = GridBagConstraints.HORIZONTAL;
      constraints.anchor = GridBagConstraints.WEST;
      constraints.weightx = 1;
      layout.setConstraints( jtext, constraints);
      
      component = new JPanel( layout);
      component.add( jlabel);
      component.add( jtext);
      
      container.add( component);
    }
    else
    {
      component = jtext;
      container.add( jtext);
    }
    
    // add listeners to the widget
    jtext.addKeyListener( keyListener);
    jtext.addCaretListener( caretListener);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.TextXidget#getErrorFeature()
   */
  @Override
  protected IErrorFeature getErrorFeature()
  {
    return new SwingTooltipErrorFeature( component);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.TextXidget#getWidgetFeature()
   */
  @Override
  protected IWidgetFeature getWidgetFeature()
  {
    return new SwingWidgetFeature( component);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.TextXidget#getWidgetTextFeature()
   */
  @Override
  protected IWidgetTextFeature getWidgetTextFeature()
  {
    return new TextFeature( jtext);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAdapter(java.lang.Class)
   */
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss.equals( ISwingWidgetFeature.class)) return (T)super.getFeature( clss);
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
      if ( adapter != null) adapter.setText( ModelTextFeature.selectedChannel, jtext.getSelectedText());
    }
  };
  
  private final Runnable updateRunnable = new Runnable() {
    public void run()
    {
      IModelTextFeature adapter = getFeature( IModelTextFeature.class);
      if ( adapter != null) adapter.setText( TextXidget.allChannel, jtext.getText());
    }
  };

  private JComponent component;
  private JLabel jlabel;
  private JTextComponent jtext;
}
