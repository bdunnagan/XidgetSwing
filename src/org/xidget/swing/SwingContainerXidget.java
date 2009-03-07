package org.xidget.swing;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import org.xidget.AbstractXidget;
import org.xidget.IWidgetAdapter;
import org.xidget.IXidget;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.feature.IErrorFeature;
import org.xidget.layout.LayoutTagHandler.Layout;
import org.xidget.swing.adapter.SwingTooltipErrorAdapter;
import org.xidget.swing.layout.LayoutManager;
import org.xmodel.IModelObject;

/**
 * An implementation of IXidget that serves as the Swing/AWT implementation of a form.
 */
public class SwingContainerXidget extends AbstractXidget
{  
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#startConfig(org.xidget.config.processor.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  @Override
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    panel = new JPanel( new SpringLayout());
    return true;
  }

  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#endConfig(org.xidget.config.processor.TagProcessor, org.xmodel.IModelObject)
   */
  @Override
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException
  {
    // set sizes
    Layout layout = getLayout();
    if ( layout != null) panel.setPreferredSize( new Dimension( layout.size.x, layout.size.y));
    
    // create layout for children
    LayoutManager manager = new LayoutManager();
    manager.applyLayout( this);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss.equals( ISwingWidgetAdapter.class)) return (T)new SwingWidgetAdapter( panel);
    if ( clss.equals( IWidgetAdapter.class)) return (T)new SwingWidgetAdapter( panel);
    if ( clss.equals( IErrorFeature.class)) return (T)new SwingTooltipErrorAdapter( panel);
    return null;
  }

  /**
   * Returns a Swing/AWT widget container.
   * @return Returns a Swing/AWT widget container.
   */
  public JPanel getContainer()
  {
    return panel;
  }
  
  private JPanel panel;
}
