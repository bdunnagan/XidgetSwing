package org.xidget.swing;

import java.awt.Container;
import javax.swing.JFrame;
import org.xidget.AbstractXidget;
import org.xidget.IXidget;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xmodel.IModelObject;

/**
 * An implementation of IXidget which represents a Swing JFrame widget.
 */
public class SwingApplicationXidget extends AbstractXidget implements ISwingFrameFeature, ISwingContainerFeature
{
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#startConfig(org.xidget.config.processor.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  @Override
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    super.startConfig( processor, parent, element);
    frame = new JFrame();
    return true;
  }

  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#endConfig(org.xidget.config.processor.TagProcessor, org.xmodel.IModelObject)
   */
  @Override
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException
  {
    super.endConfig( processor, element);
    
    frame.pack();
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame.setVisible( true);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss.equals( ISwingContainerFeature.class)) return (T)this;
    if ( clss.equals( ISwingFrameFeature.class)) return (T)this; 
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.ISwingContainerFeature#getContainer()
   */
  public Container getContainer()
  {
    return frame.getContentPane();
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.ISwingApplicationWidgetFeature#getFrame()
   */
  public JFrame getFrame()
  {
    return frame;
  }

  private JFrame frame;
}
