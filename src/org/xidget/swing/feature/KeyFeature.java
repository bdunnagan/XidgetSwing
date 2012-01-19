/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.event.KeyAdapter;

import org.xidget.IXidget;
import org.xidget.ifeature.IKeyFeature;
import org.xidget.swing.key.KeyManager;
import org.xmodel.xaction.IXAction;

/**
 * An implementation of IKeyboardFeature for the Swing Control class.
 */
public class KeyFeature extends KeyAdapter implements IKeyFeature
{
  public KeyFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IKeyFeature#bind(java.lang.String, boolean, org.xmodel.xaction.IXAction)
   */
  @Override
  public void bind( String keys, boolean override, IXAction script)
  {
    KeyManager.getInstance().bind( xidget, keys, override, script);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IKeyboardFeature#unbind(java.lang.String, org.xmodel.xaction.IXAction)
   */
  @Override
  public void unbind( String keys, IXAction script)
  {
    KeyManager.getInstance().unbind( xidget, keys, script);
  }

  private IXidget xidget;
}
