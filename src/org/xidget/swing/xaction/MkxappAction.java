/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.xaction;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.xidget.swing.image.ImageSerializer;
import org.xmodel.IModelObject;
import org.xmodel.compress.DefaultSerializer;
import org.xmodel.compress.ICompressor;
import org.xmodel.compress.TabularCompressor;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction that creates a Swing xapp.xip file and correctly handles ImageIcons.
 */
public class MkxappAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    
    sourceExpr = document.getExpression( "source", true);
    if ( sourceExpr == null) sourceExpr = document.getExpression();
    
    fileExpr = document.getExpression( "file", true);
    if ( fileExpr == null) fileExpr = document.getExpression();
    
    DefaultSerializer serializer = new DefaultSerializer();
    serializer.register( Image.class, new ImageSerializer());
    
    compressor = new TabularCompressor();
    compressor.setSerializer( serializer);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    File file = new File( fileExpr.evaluateString( context));
    
    IModelObject element = sourceExpr.queryFirst( context);
    if ( element == null)
    {
      try
      {
        // go ahead and create empty file
        file.createNewFile();
      }
      catch( IOException e)
      {
        throw new XActionException( "Unable to write file: "+file, e);
      }
      
      return null;
    }
    
    try
    {
      FileOutputStream stream = new FileOutputStream( file);
      compressor.compress( element, stream);
      stream.close();
    }
    catch( Exception e)
    {
      throw new XActionException( "Unable to write file: "+file, e);
    }
    
    return null;
  }
  
  private IExpression sourceExpr;
  private IExpression fileExpr;
  private ICompressor compressor;
}
