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
package org.xidget.swing.slider;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xidget.IXidget;
import org.xidget.ifeature.ISourceFeature;
import org.xidget.ifeature.slider.ISliderFeature;
import org.xidget.swing.feature.SwingWidgetCreationFeature;
import org.xmodel.IModelObject;

/**
 * An implementation of IWidgetCreationFeature for the Swing JSlider widget.
 */
public class JSliderWidgetCreationFeature extends SwingWidgetCreationFeature
{
  public JSliderWidgetCreationFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.swing.feature.SwingWidgetCreationFeature#createSwingWidget()
   */
  @Override
  protected JComponent createSwingWidget()
  {
    jSlider = new JSlider();
    jSlider.addChangeListener( changeListener);
    return jSlider;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetCreationFeature#getLastWidgets()
   */
  public Object[] getLastWidgets()
  {
    return new Object[] { jSlider};
  }

  /**
   * Returns the widget.
   * @return Returns the widget.
   */
  public JSlider getJSlider()
  {
    return jSlider;
  }

  private ChangeListener changeListener = new ChangeListener() {
    public void stateChanged( ChangeEvent e)
    {
      //if ( jSlider.getValueIsAdjusting()) return;
      ISliderFeature sliderFeature = xidget.getFeature( ISliderFeature.class);
      ISourceFeature sourceFeature = xidget.getFeature( ISourceFeature.class);
      IModelObject source = sourceFeature.getSource( ISourceFeature.allChannel);
      if ( source != null) source.setValue( sliderFeature.getValue());
    }
  };
  
  private JSlider jSlider;
}
