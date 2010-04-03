/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JLabelSourceFeature.java
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
package org.xidget.swing.label;

import javax.swing.JLabel;
import org.xidget.IXidget;
import org.xidget.ifeature.ISourceFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ISourceFeature for the Swing JLabel widget.
 */
public class JLabelSourceFeature implements ISourceFeature
{
  public JLabelSourceFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#getSource(java.lang.String)
   */
  public IModelObject getSource( String channel)
  {
    return source;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#setSource(java.lang.String, org.xmodel.IModelObject)
   */
  public void setSource( StatefulContext context, String channel, IModelObject node)
  {
    if ( channel == ISourceFeature.allChannel)
    {
      this.source = node;
      JLabel jLabel = xidget.getFeature( JLabel.class);
      jLabel.setText( Xlate.get( node, ""));
    }
  }
  
  private IXidget xidget;
  private IModelObject source;
}
