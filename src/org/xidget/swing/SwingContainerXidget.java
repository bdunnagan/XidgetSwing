package org.xidget.swing;

import javax.swing.JPanel;
import javax.swing.SpringLayout;
import org.xidget.AbstractXidget;
import org.xidget.IXidget;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.layout.AnchorLayoutFeature;
import org.xidget.layout.ILayoutFeature;
import org.xidget.swing.feature.SwingTooltipErrorFeature;
import org.xidget.swing.layout.AnchorLayoutManager;
import org.xmodel.IModelObject;

/**
 * An implementation of IXidget that serves as the Swing/AWT implementation of a form.
 */
public class SwingContainerXidget extends AbstractXidget
{  
  public SwingContainerXidget()
  {
    layoutFeature = new AnchorLayoutFeature( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#startConfig(org.xidget.config.processor.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  @Override
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    super.startConfig( processor, parent, element);
    
    panel = new JPanel( new SpringLayout());
    //panel.setLayout( new FlowLayout());
    panel.setLayout( new AnchorLayoutManager( layoutFeature));
    
    widgetFeature = new SwingWidgetFeature( panel);
    errorFeature = new SwingTooltipErrorFeature( panel);
    
    return true;
  }

  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#endConfig(org.xidget.config.processor.TagProcessor, org.xmodel.IModelObject)
   */
  @Override
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss.equals( ISwingWidgetAdapter.class)) return (T)widgetFeature;
    if ( clss.equals( IWidgetFeature.class)) return (T)widgetFeature;
    if ( clss.equals( IErrorFeature.class)) return (T)errorFeature;
    if ( clss.equals( ILayoutFeature.class)) return (T)layoutFeature;
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
  private ILayoutFeature layoutFeature;
  private SwingWidgetFeature widgetFeature;
  private SwingTooltipErrorFeature errorFeature; 
}
