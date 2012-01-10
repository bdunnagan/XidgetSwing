/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.xaction;

import java.io.ByteArrayInputStream;

import javax.swing.plaf.synth.SynthLookAndFeel;

import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xml.XmlIO;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction that reads a Synth configuration file and installs the Synth look-and-feel.
 */
public class InstallSynthAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    synthExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    IModelObject element = synthExpr.queryFirst( context);
    
    XmlIO xmlIO = new XmlIO();
    String configuration = xmlIO.write( element);
    ByteArrayInputStream stream = new ByteArrayInputStream( configuration.getBytes());

    try
    {
      SynthLookAndFeel synth = new SynthLookAndFeel();
      synth.load( stream, InstallSynthAction.class);
    }
    catch( Exception e)
    {
      throw new XActionException( "Unable to install Synth look-and-feel.", e);
    }
    
    return null;
  }

  private IExpression synthExpr;
}
