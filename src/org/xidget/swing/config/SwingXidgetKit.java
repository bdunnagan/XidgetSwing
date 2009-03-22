/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.config;

import org.xidget.XidgetTagHandler;
import org.xidget.config.IXidgetKit;
import org.xidget.config.processor.ITagHandler;
import org.xidget.swing.SwingApplicationXidget;
import org.xidget.swing.SwingContainerXidget;
import org.xidget.swing.text.SwingTextXidget;

/**
 * An implementation of IReferenceConfiguration for Swing/AWT.
 */
public class SwingXidgetKit implements IXidgetKit
{
  /* (non-Javadoc)
   * @see org.xidget.config.IXidgetKit#getApplicationHandler()
   */
  public ITagHandler getApplicationHandler()
  {
    return new XidgetTagHandler( SwingApplicationXidget.class);
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IXidgetKit#getDialogHandler()
   */
  public ITagHandler getDialogHandler()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IXidgetKit#getFormHandler()
   */
  public ITagHandler getFormHandler()
  {
    return new XidgetTagHandler( SwingContainerXidget.class);
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IReferenceConfiguration#getButtonHandler()
   */
  public ITagHandler getButtonHandler()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IReferenceConfiguration#getComboHandler()
   */
  public ITagHandler getComboHandler()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IReferenceConfiguration#getSliderHandler()
   */
  public ITagHandler getSliderHandler()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IReferenceConfiguration#getTableHandler()
   */
  public ITagHandler getTableHandler()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IReferenceConfiguration#getTextHandler()
   */
  public ITagHandler getTextHandler()
  {
    return new XidgetTagHandler( SwingTextXidget.class);
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IReferenceConfiguration#getTreeHandler()
   */
  public ITagHandler getTreeHandler()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
