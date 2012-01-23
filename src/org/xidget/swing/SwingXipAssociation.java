/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import java.io.InputStream;
import javax.swing.ImageIcon;
import org.xmodel.IModelObject;
import org.xmodel.caching.AbstractFileAssociation;
import org.xmodel.compress.DefaultSerializer;
import org.xmodel.compress.ICompressor;
import org.xmodel.compress.TabularCompressor;
import org.xmodel.compress.serial.JavaSerializer;
import org.xmodel.external.CachingException;

/**
 * A customization of XipAssociation that configures the TabularCompressor ISerializer to recognized ImageIcons.
 */
public class SwingXipAssociation extends AbstractFileAssociation
{
  /* (non-Javadoc)
   * @see org.xmodel.external.caching.IFileAssociation#getAssociations()
   */
  public String[] getExtensions()
  {
    return extensions;
  }

  /* (non-Javadoc)
   * @see org.xmodel.external.caching.IFileAssociation#apply(org.xmodel.IModelObject, java.lang.String, java.io.InputStream)
   */
  public void apply( IModelObject parent, String name, InputStream stream) throws CachingException
  {
    try
    {
      DefaultSerializer serializer = new DefaultSerializer();
      serializer.register( ImageIcon.class, new JavaSerializer());
      
      ICompressor compressor = new TabularCompressor();
      compressor.setSerializer( serializer);
      
      IModelObject content = compressor.decompress( stream);
      parent.addChild( content);
    }
    catch( Exception e)
    {
      throw new CachingException( "Unable to parse xml in compressed file: "+name, e);
    }
  }
  
  private final static String[] extensions = { ".xip"};
}
