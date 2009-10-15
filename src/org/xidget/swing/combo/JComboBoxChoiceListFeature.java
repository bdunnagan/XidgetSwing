/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.combo;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import org.xidget.IXidget;
import org.xidget.ifeature.combo.IChoiceListFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;

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
    updateChoice( widget, choice);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.feature.IChoiceListFeature#insertChoice(int, java.lang.String)
   */
  public void insertChoice( int index, String choice)
  {
    JComboBox widget = xidget.getFeature( JComboBox.class);
    widget.insertItemAt( choice, index);
    updateChoice( widget, choice);
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
  
  /**
   * Update the selected choice if the choice was added after the text of the widget was set.
   * @param widget The widget.
   * @param choice The choice that was added.
   */
  private void updateChoice( JComboBox widget, String choice)
  {
    ITextWidgetFeature feature = xidget.getFeature( ITextWidgetFeature.class);
    String text = ((JComboBoxTextWidgetFeature)feature).getText();
    if ( text != null && text.equals( choice)) widget.setSelectedItem( text);
  }

  private IXidget xidget;
}
