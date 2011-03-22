/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * SwingTitleFeature.java
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
package org.xidget.swing.feature;

import javax.swing.JTabbedPane;

import org.xidget.IXidget;
import org.xidget.ifeature.ITitleFeature;
import org.xidget.swing.tabs.CustomTab;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of ITitleFeature for suitable for all Swing containers.
 */
public class SwingTitleFeature implements ITitleFeature
{
  public SwingTitleFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITitleFeature#getTitle()
   */
  public String getTitle()
  {
    IModelObject element = xidget.getConfig();
    IExpression titleExpr = Xlate.childGet( element, "title", Xlate.get( element, "title", (IExpression)null));
    return titleExpr.evaluateString();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ITitleFeature#setTitle(java.lang.String)
   */
  public void setTitle( String title)
  {
    IXidget parent = xidget.getParent();
    if ( parent == null) return;
    
    if ( parent.getConfig().isType( "tabs"))
    {
      JTabbedPane jtabbedPane = parent.getFeature( JTabbedPane.class);
      int index = parent.getChildren().indexOf( xidget);
      if ( index < jtabbedPane.getTabCount())
      {
        CustomTab tab = (CustomTab)jtabbedPane.getTabComponentAt( index);
        tab.setTitle( title);
      }
    }
  }

  private IXidget xidget;
}
