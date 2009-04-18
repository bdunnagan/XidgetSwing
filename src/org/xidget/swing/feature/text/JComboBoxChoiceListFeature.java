/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature.text;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import org.xidget.IXidget;
import org.xidget.ifeature.combo.IChoiceListFeature;

/**
 * An implementation of IChoiceListFeature which is backed by a JComboBox.
 */
public class JComboBoxChoiceListFeature implements IChoiceListFeature
{
  public JComboBoxChoiceListFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#getChoices()
   */
  public List<String> getChoices()
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
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
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.addItem( choice);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#insertChoice(int, java.lang.String)
   */
  public void insertChoice( int index, String choice)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.insertItemAt( choice, index);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeAllChoices()
   */
  public void removeAllChoices()
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.removeAllItems();
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeChoice(int)
   */
  public void removeChoice( int index)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.removeItemAt( index);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#removeChoice(java.lang.String)
   */
  public void removeChoice( String choice)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.removeItem( choice);
  }

  private IXidget xidget;
}
