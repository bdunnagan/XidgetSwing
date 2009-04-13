/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.text.feature;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import org.xidget.text.feature.IChoiceListFeature;

/**
 * An implementation of IChoiceListFeature which is backed by a JComboBox.
 */
public class JComboBoxChoiceListFeature implements IChoiceListFeature
{
  public JComboBoxChoiceListFeature( JComboBox widget)
  {
    this.widget = widget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#getChoices()
   */
  public List<String> getChoices()
  {
    int count = widget.getItemCount();
    List<String> result = new ArrayList<String>( count);
    for( int i=0; i<count; i++) result.add( widget.getItemAt( i).toString());
    return result;
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#addChoice(java.lang.String)
   */
  public void addChoice( String choice)
  {
    widget.addItem( choice);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#insertChoice(int, java.lang.String)
   */
  public void insertChoice( int index, String choice)
  {
    widget.insertItemAt( choice, index);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeAllChoices()
   */
  public void removeAllChoices()
  {
    widget.removeAllItems();
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeChoice(int)
   */
  public void removeChoice( int index)
  {
    widget.removeItemAt( index);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeChoice(java.lang.String)
   */
  public void removeChoice( String choice)
  {
    widget.removeItem( choice);
  }

  private JComboBox widget;
}
