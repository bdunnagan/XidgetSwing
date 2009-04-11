/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.xidget.AbstractXidget;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.feature.IWidgetCreationFeature;
import org.xmodel.IModelObject;

/**
 * An implementation of IXidget which represents a Swing JFrame widget.
 */
public class SwingApplicationXidget extends AbstractXidget implements IWidgetCreationFeature, ISwingFrameFeature, ISwingContainerFeature
{
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#endConfig(org.xidget.config.processor.TagProcessor, org.xmodel.IModelObject)
   */
  @Override
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException
  {
    super.endConfig( processor, element);

    SwingUtilities.invokeLater( new Runnable() {
      public void run()
      {
        frame.pack();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
        frame.setVisible( true);
      }
    });
  }

  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#getWidgetCreationFeature()
   */
  @Override
  protected IWidgetCreationFeature getWidgetCreationFeature()
  {
    return this;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss.equals( ISwingContainerFeature.class)) return (T)this;
    if ( clss.equals( ISwingFrameFeature.class)) return (T)this; 
    return super.getFeature( clss);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#createWidget( java.lang.String, org.xmodel.IModelObject)
   */
  public void createWidget( String label, IModelObject element)
  {
    frame = new JFrame();
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IWidgetCreationFeature#destroyWidget()
   */
  public void destroyWidget()
  {
    frame.dispose();
    frame = null;
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
