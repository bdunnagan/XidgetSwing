/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.image;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.compress.CompressorException;
import org.xmodel.compress.ISerializer;

/**
 * An implementation of ISerializer for the Swing Image class.
 */
public class ImageSerializer implements ISerializer
{
  /* (non-Javadoc)
   * @see org.xmodel.compress.ISerializer#readObject(java.io.DataInput)
   */
  @Override
  public Object readObject( DataInputStream input) throws IOException, ClassNotFoundException, CompressorException
  {
    int length = input.readInt();
    byte[] bytes = new byte[ length];
    input.readFully( bytes);
    
    ByteArrayInputStream buffer = new ByteArrayInputStream( bytes);
    return ImageIO.read( buffer);
  }

  /* (non-Javadoc)
   * @see org.xmodel.compress.ISerializer#writeObject(java.io.DataOutput, java.lang.Object)
   */
  @Override
  public int writeObject( DataOutputStream output, IModelObject node) throws IOException, CompressorException
  {
    String format = "png";
    
    // check parent node for image file type
    IModelObject parent = node.getParent();
    String path = Xlate.get( parent, "path", (String)null);
    if ( path != null) format = path.replaceFirst( "^.*\\.([^.]++)$", "$1");
    
    Image image = (Image)node.getValue();
    
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    ImageIO.write( (RenderedImage)image, format, buffer);
    buffer.close();
    
    byte[] bytes = buffer.toByteArray();
    output.writeInt( bytes.length);
    output.write( bytes);
    
    return bytes.length;
  }
}
