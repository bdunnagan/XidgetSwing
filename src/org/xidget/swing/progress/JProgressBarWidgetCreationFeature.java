/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * JLabelWidgetCreationFeature.java
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
package org.xidget.swing.progress;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xidget.IXidget;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.model.ISingleValueUpdateFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IWidgetCreationFeature for the Swing JProgressBar widget.
 */
public class JProgressBarWidgetCreationFeature extends SwingWidgetCreationFeature implements ILabelFeature
{
  public JProgressBarWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    IModelObject config = xidget.getConfig();
    
    jProgress = new JProgressBar();
    jProgress.addChangeListener( changeListener);
    
    boolean vertical = Xlate.get( xidget.getConfig(), "orientation", "horizontal").equals( "vertical");
    jProgress.setOrientation( vertical? SwingConstants.VERTICAL: SwingConstants.HORIZONTAL);
    
    // create extra container to hold label and widget
    if ( Xlate.get( config, "label", Xlate.childGet( config, "label", (String)null)) != null)
    {
      jLabel = new JLabel( "");
      jLabel.setHorizontalAlignment( vertical? SwingConstants.CENTER: SwingConstants.RIGHT);
      
      GridBagLayout layout = new GridBagLayout();
      GridBagConstraints constraints = new GridBagConstraints();
      if ( vertical)
      {
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.insets = new Insets( 0, 0, 4, 0);
      }
      else
      {
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets( 0, 0, 0, 4);
      }
      layout.setConstraints( jLabel, constraints);
      
      constraints = new GridBagConstraints();
      constraints.fill = GridBagConstraints.BOTH;
      if ( vertical)
      {
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weighty = 1;
        constraints.gridy = 1;
      }
      else
      {
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
      }
      
      JPanel jPanel = new JPanel( layout);
      layout.setConstraints( jProgress, constraints);
      jPanel.add( jLabel);
      jPanel.add( jProgress);
      
      component = jPanel;
    }
    
    if ( component == null) component = jProgress;
    return component;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    if ( component != jProgress) return new Object[] { component, jProgress};
    return new Object[] { jProgress};
  }
  /**
   * 
   * Returns the container widget which holds the label and slider widgets.
   * @return Returns the container widget which holds the label and slider widgets.
   */
  public JComponent getContainer()
  {
    return component;
  }

  /**
   * Returns the widget.
   * @return Returns the widget.
   */
  public JProgressBar getJProgressBar()
  {
    return jProgress;
  }

  /**
   * Returns the label widget.
   * @return Returns null or the label widget.
   */
  public JLabel getLabelWidget()
  {
    return jLabel;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#setWidth(int)
   */
  public void setLabelWidth( int width)
  {
    if ( jLabel == null) return;
    Dimension size = jLabel.getPreferredSize();
    jLabel.setPreferredSize( new Dimension( width, size.height));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#getLabelWidth()
   */
  public int getLabelWidth()
  {
    if ( jLabel == null) return 0;
    Dimension size = jLabel.getPreferredSize();
    return size.width;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILabelFeature#setText(java.lang.String)
   */
  public void setLabelText( String text)
  {
    if ( jLabel != null) jLabel.setText( text);
  }
      
  private ChangeListener changeListener = new ChangeListener() {
    public void stateChanged( ChangeEvent e)
    {
      ISingleValueUpdateFeature feature = xidget.getFeature( ISingleValueUpdateFeature.class);
      feature.updateModel();
    }
  };
  
  private JComponent component;
  private JLabel jLabel;
  private JProgressBar jProgress;
}
