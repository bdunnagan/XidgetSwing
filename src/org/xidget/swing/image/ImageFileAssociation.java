/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.image;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
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
   * @see org.xmodel.external.caching.IFileAssociation#apply(org.xmodel.IModelObject, java.lang.String, java.io.InputStream)
   */
  public void apply( IModelObject parent, String name, InputStream stream) throws CachingException
  {
    try
    {
      byte[] content = readAll( stream);
      ImageIcon image = new ImageIcon( content);
      parent.setValue( image);
    }
    catch( IOException e)
    {
      throw new CachingException( "Unable to load image: "+name, e);
    }
  }
  
  /**
   * Read the entire content of the stream and return the content.
   * @param stream The stream.
   * @return Returns the content of the stream.
   */
  private byte[] readAll( InputStream stream) throws IOException
  {
    ReadableByteChannel channel = Channels.newChannel( stream);
    
    int nread = 1, size = 0;
    List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();
    while( nread > 0)
    {
      ByteBuffer buffer = ByteBuffer.allocate( 1 << 16);
      nread = channel.read( buffer);
      if ( nread > 0)
      {
        buffer.flip();
        size += nread;
        buffers.add( buffer);
      }
    }
    
    int offset = 0;
    byte[] content = new byte[ size];
    for( ByteBuffer buffer: buffers)
    {
      System.arraycopy( buffer.array(), 0, content, offset, buffer.remaining());
      offset += buffer.remaining();
    }
    
    return content;
  }

  /* (non-Javadoc)
   * @see org.xmodel.external.caching.IFileAssociation#getExtensions()
   */
  public String[] getExtensions()
  {
    return new String[] { ".jpg", ".jpeg", ".gif", ".png"};
  }
}
