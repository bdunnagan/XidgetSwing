/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * ImageFileAssociation.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import org.xmodel.caching.AbstractFileAssociation;
import org.xmodel.external.CachingException;

/**
 * An implementation of IFileAssociation that loads Swing images into instances of ImageIcon.
 */
public class ImageFileAssociation extends AbstractFileAssociation
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
