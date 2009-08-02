/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.image;

import java.io.File;
import javax.swing.ImageIcon;
import org.xmodel.IModelObject;
import org.xmodel.external.CachingException;
import org.xmodel.external.caching.IFileAssociation;

/**
 * An implementation of IFileAssociation that loads Swing images into instances of ImageIcon.
 */
public class ImageFileAssociation implements IFileAssociation
{
  /* (non-Javadoc)
   * @see org.xmodel.external.caching.IFileAssociation#apply(org.xmodel.IModelObject, java.io.File)
   */
  public void apply( IModelObject parent, File file) throws CachingException
  {
    ImageIcon image = new ImageIcon( file.getAbsolutePath());
    parent.setValue( image);
  }

  /* (non-Javadoc)
   * @see org.xmodel.external.caching.IFileAssociation#getExtensions()
   */
  public String[] getExtensions()
  {
    return new String[] { ".jpg", ".jpeg", ".gif", ".png"};
  }
}
