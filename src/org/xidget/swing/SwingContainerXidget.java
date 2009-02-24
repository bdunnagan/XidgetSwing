package org.xidget.swing;

import java.awt.Container;
import org.xidget.AbstractXidget;
import org.xidget.IXidget;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xmodel.IModelObject;

/**
 */
public class SwingContainerXidget extends AbstractXidget
{

  /* (non-Javadoc)
   * @see org.xidget.IXidget#endConfig(org.xidget.config.TagProcessor, org.xmodel.IModelObject)
   */
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAdapter(java.lang.Class)
   */
  public Object getAdapter( Class<? extends Object> clss)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#startConfig(org.xidget.config.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * Returns a Swing/AWT widget container.
   * @return Returns a Swing/AWT widget container.
   */
  public Container getContainer()
  {
    return container;
  }
  
  private Container container;
}
