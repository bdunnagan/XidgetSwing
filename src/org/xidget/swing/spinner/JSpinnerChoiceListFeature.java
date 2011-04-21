/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JSpinnerChoiceListFeature.java
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
package org.xidget.swing.spinner;

import java.util.Collections;
import java.util.List;

import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;

import org.xidget.IXidget;
import org.xidget.ifeature.combo.IChoiceListFeature;

/**
 * An implementation of IChoiceListFeature which is backed by a JSpinner.
 */
public class JSpinnerChoiceListFeature implements IChoiceListFeature
{
  public JSpinnerChoiceListFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#getChoices()
   */
  public List<String> getChoices()
  {
    List<String> list = getList();
    if ( list != null) return list;
    return Collections.emptyList();
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#addChoice(java.lang.String)
   */
  public void addChoice( String choice)
  {
    List<String> list = getList();
    if ( list != null) list.add( choice);
    //updateChoice( choice);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#insertChoice(int, java.lang.String)
   */
  public void insertChoice( int index, String choice)
  {
    List<String> list = getList();
    if ( list != null) list.add( index, choice);
    //updateChoice( choice);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeAllChoices()
   */
  public void removeAllChoices()
  {
    List<String> list = getList();
    if ( list != null) list.clear();
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeChoice(int)
   */
  public void removeChoice( int index)
  {
    List<String> list = getList();
    if ( list != null) list.remove( index);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeChoice(java.lang.String)
   */
  public void removeChoice( String choice)
  {
    List<String> list = getList();
    if ( list != null) list.remove( choice);
  }
  
  /**
   * Update the selected choice if the choice was added after the text of the widget was set.
   * @param choice The choice that was added.
   */
//  private void updateChoice( String choice)
//  {
//    ITextWidgetFeature feature = xidget.getFeature( ITextWidgetFeature.class);
//    String text = ((JSpinnerTextWidgetFeature)feature).getText();
//    if ( text != null && text.equals( choice)) widget.setSelectedItem( text);
//  }

  /**
   * @return Returns null or the SpinnerListModel.
   */
  private SpinnerListModel getModel()
  {
    JSpinner widget = xidget.getFeature( JSpinner.class);
    SpinnerModel model = widget.getModel();
    if ( model instanceof SpinnerListModel) return (SpinnerListModel)model;
    return null;
  }
  
  /**
   * @return Returns the list from the SpinnerListModel or null.
   */
  @SuppressWarnings("unchecked")
  private List<String> getList()
  {
    SpinnerListModel model = getModel();
    List<?> list = model.getList();
    return (List<String>)list;
  }
  
  private IXidget xidget;
}
