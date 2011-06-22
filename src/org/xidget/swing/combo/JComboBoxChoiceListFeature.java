/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JComboBoxChoiceListFeature.java
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
package org.xidget.swing.combo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IChoiceListFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IChoiceListFeature which is backed by a JComboBox.
 */
public class JComboBoxChoiceListFeature implements IChoiceListFeature
{
  public JComboBoxChoiceListFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.choices = new ArrayList<Object>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IChoiceListFeature#setTransform(org.xmodel.xpath.expression.IExpression)
   */
  @Override
  public void setTransform( IExpression transform)
  {
    this.transform = transform;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IChoiceListFeature#transform(java.lang.Object)
   */
  public Object transform( Object value)
  {
    System.out.printf( "%s\n", value);
    if ( transform == null) return value;

    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext parent = bindFeature.getBoundContext();
    
    StatefulContext context = new StatefulContext( parent, (IModelObject)value);
    context.set( "v", (IModelObject)value);
    
    String s = transform.evaluateString( context);
    System.out.printf( "    -> %s\n", s);
    return s;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#getChoices()
   */
  public List<Object> getChoices()
  {
    return choices;
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#addChoice(java.lang.Object)
   */
  public void addChoice( Object choice)
  {
    choices.add( choice);
    
    JComboBox widget = xidget.getFeature( JComboBox.class);
    choice = transform( choice);
    widget.addItem( choice);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#insertChoice(int, java.lang.Object)
   */
  public void insertChoice( int index, Object choice)
  {
    choices.add( index, choice);
    
    JComboBox widget = xidget.getFeature( JComboBox.class);
    choice = transform( choice);
    widget.insertItemAt( choice, index);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeAllChoices()
   */
  public void removeAllChoices()
  {
    choices.clear();
    
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.removeAllItems();
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeChoice(int)
   */
  public void removeChoice( int index)
  {
    choices.remove( index);
    
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.removeItemAt( index);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeChoice(java.lang.Object)
   */
  public void removeChoice( Object choice)
  {
    choices.remove( choice);
    
    JComboBox widget = xidget.getFeature( JComboBox.class);
    choice = transform( choice);
    widget.removeItem( choice);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IChoiceListFeature#updateChoice(int, java.lang.Object)
   */
  @Override
  public void updateChoice( int index, Object choice)
  {
    choices.set( index, choice);
    
    JComboBox widget = xidget.getFeature( JComboBox.class);
    CustomComboModel model = (CustomComboModel)widget.getModel();
    choice = transform( choice);
    model.updateElementAt( choice, index);
  }
  
  private IXidget xidget;
  private IExpression transform;
  private List<Object> choices;
}
