/*
 * XidgetSwing - A Java Swing implementation of Xidgets
 * 
 * Toolkit.java
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
package org.xidget.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.xidget.Creator;
import org.xidget.IToolkit;
import org.xidget.IXidget;
import org.xidget.binding.XidgetTagHandler;
import org.xidget.binding.table.TableTagHandler;
import org.xidget.binding.tree.TreeTagHandler;
import org.xidget.config.TagProcessor;
import org.xidget.ifeature.IAsyncFeature;
import org.xidget.ifeature.IColorFeature;
import org.xidget.ifeature.IFocusFeature;
import org.xidget.ifeature.IPrintFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.swing.applet.JAppletXidget;
import org.xidget.swing.button.AbstractButtonXidget;
import org.xidget.swing.calendar.CalendarXidget;
import org.xidget.swing.chart.line.LineChartXidget;
import org.xidget.swing.chart.line.XAxisXidget;
import org.xidget.swing.chart.line.YAxisXidget;
import org.xidget.swing.chart.pie.PieChartXidget;
import org.xidget.swing.combo.JComboBoxXidget;
import org.xidget.swing.dialog.JDialogXidget;
import org.xidget.swing.feature.AsyncFeature;
import org.xidget.swing.feature.SwingColorFeature;
import org.xidget.swing.feature.SwingFocusFeature;
import org.xidget.swing.feature.SwingPrintFeature;
import org.xidget.swing.form.JPanelXidget;
import org.xidget.swing.frame.JFrameXidget;
import org.xidget.swing.image.ImageFileAssociation;
import org.xidget.swing.label.JLabelTagHandler;
import org.xidget.swing.label.JLabelXidget;
import org.xidget.swing.menu.JMenuBarXidget;
import org.xidget.swing.menu.JMenuItemXidget;
import org.xidget.swing.menu.MenuTagHandler;
import org.xidget.swing.menu.SubMenuTagHandler;
import org.xidget.swing.progress.JProgressBarXidget;
import org.xidget.swing.slider.JSliderXidget;
import org.xidget.swing.table.JTableXidget;
import org.xidget.swing.tabs.JTabbedPaneXidget;
import org.xidget.swing.text.JTextXidget;
import org.xidget.swing.tree.JTreeXidget;
import org.xidget.swing.util.BuildLabelHtml;
import org.xidget.swing.xmleditor.XmlTextPaneXidget;
import org.xmodel.IDispatcher;
import org.xmodel.IModelObject;
import org.xmodel.ModelRegistry;
import org.xmodel.caching.IFileAssociation;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpression.ResultType;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IToolkit for the Swing platform.
 */
public class Toolkit implements IToolkit
{
  public Toolkit()
  {
    asyncFeature = new AsyncFeature();
    focusFeature = new SwingFocusFeature();
    colorFeature = new SwingColorFeature();
    printFeature = new SwingPrintFeature();
    
    // define the dispatcher in the xmodel
    ModelRegistry.getInstance().getModel().setDispatcher( new IDispatcher() {
      public void execute( Runnable runnable)
      {
        SwingUtilities.invokeLater( runnable);
      }
    });
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IFeatured#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IAsyncFeature.class) return (T)asyncFeature;
    if ( clss == IFocusFeature.class) return (T)focusFeature;
    if ( clss == IColorFeature.class) return (T)colorFeature;
    if ( clss == IPrintFeature.class) return (T)printFeature;
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IToolkit#configure(org.xidget.config.TagProcessor)
   */
  public void configure( TagProcessor processor)
  {
    processor.addHandler( "applet", new XidgetTagHandler( JAppletXidget.class));
    processor.addHandler( "window", new XidgetTagHandler( JFrameXidget.class));
    processor.addHandler( "calendar", new XidgetTagHandler( CalendarXidget.class));
    processor.addHandler( "dialog", new XidgetTagHandler( JDialogXidget.class));
    processor.addHandler( "form", new XidgetTagHandler( JPanelXidget.class));
    processor.addHandler( "chart", new XidgetTagHandler( PieChartXidget.class, "style", "pie"));
    processor.addHandler( "chart", new XidgetTagHandler( LineChartXidget.class, "style", "line"));
    processor.addHandler( "tabs", new XidgetTagHandler( JTabbedPaneXidget.class));
    processor.addHandler( "text", new XidgetTagHandler( JTextXidget.class));
    processor.addHandler( "combo", new XidgetTagHandler( JComboBoxXidget.class));
    processor.addHandler( "button", new XidgetTagHandler( AbstractButtonXidget.class));
    processor.addHandler( "label", new JLabelTagHandler( JLabelXidget.class));
    processor.addHandler( "menubar", new XidgetTagHandler( JMenuBarXidget.class));
    processor.addHandler( "menu", new MenuTagHandler());
    processor.addHandler( "menu", new SubMenuTagHandler());
    processor.addHandler( "menuItem", new XidgetTagHandler( JMenuItemXidget.class));
    processor.addHandler( "password", new XidgetTagHandler( JTextXidget.class));
    processor.addHandler( "progress", new XidgetTagHandler( JProgressBarXidget.class));
    processor.addHandler( "separator", new XidgetTagHandler( JMenuItemXidget.class));
    processor.addHandler( "slider", new XidgetTagHandler( JSliderXidget.class));
    //processor.addHandler( "spinner", new XidgetTagHandler( JSpinnerXidget.class));
    processor.addHandler( "table", new TableTagHandler( JTableXidget.class));
    processor.addHandler( "tree", new TreeTagHandler( JTreeXidget.class));
    processor.addHandler( "xml", new XidgetTagHandler( XmlTextPaneXidget.class));
    processor.addHandler( "ruler", new XidgetTagHandler( XAxisXidget.class, "style", "horizontal"));
    processor.addHandler( "ruler", new XidgetTagHandler( YAxisXidget.class, "style", "vertical"));
  }

  /* (non-Javadoc)
   * @see org.xidget.IToolkit#getFonts()
   */
  @Override
  public List<String> getFonts()
  {
    GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] families = environment.getAvailableFontFamilyNames();
    return Arrays.asList( families);
  }

  /* (non-Javadoc)
   * @see org.xidget.IToolkit#getImageFileAssociation()
   */
  public IFileAssociation getImageFileAssociation()
  {
    return new ImageFileAssociation();
  }

  /**
   * Returns the root xidget.
   * @param xidget A xidget.
   * @return Returns the root xidget.
   */
  private static IXidget getWindow( IXidget xidget)
  {
    IXidget parent = xidget.getParent();
    while( parent != null)
    {
      xidget = parent;
      parent = parent.getParent();
    }
    return xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IToolkit#openConfirmDialog(org.xmodel.xpath.expression.StatefulContext, java.lang.String, java.lang.Object, java.lang.String, boolean)
   */
  public Confirmation openConfirmDialog(StatefulContext context, String title, Object image, String message, boolean allowCancel)
  {
    IFocusFeature focusFeature = Creator.getToolkit().getFeature( IFocusFeature.class);
    IXidget xidget = getWindow( focusFeature.getFocus());
    
    IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    if ( creationFeature == null) throw new IllegalArgumentException( "Window xidget does not have an IWidgetCreationFeature instance: "+xidget);
    
    Object[] widgets = creationFeature.getLastWidgets();
    if ( widgets.length == 0) throw new IllegalArgumentException( "Window does not have a widget: "+xidget);
    
    message = BuildLabelHtml.buildHtml( message);
    
    int choice = JOptionPane.showConfirmDialog( (Component)widgets[ 0], message, title, allowCancel? JOptionPane.YES_NO_CANCEL_OPTION: JOptionPane.YES_NO_OPTION);
    
    switch( choice)
    {
      case JOptionPane.YES_OPTION: return Confirmation.yes;
      case JOptionPane.NO_OPTION: return Confirmation.no;
      default: return Confirmation.cancel;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.IToolkit#openMessageDialogorg.xmodel.xpath.expression.StatefulContext, java.lang.String, java.lang.Object, java.lang.String, org.xidget.IToolkit.MessageType)
   */
  public void openMessageDialog( StatefulContext context, String title, Object image, String message, MessageType type)
  {
    IFocusFeature focusFeature = Creator.getToolkit().getFeature( IFocusFeature.class);
    IXidget xidget = getWindow( focusFeature.getFocus());
    
    IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    if ( creationFeature == null) throw new IllegalArgumentException( "Window xidget does not have an IWidgetCreationFeature instance: "+xidget);
    
    Object[] widgets = creationFeature.getLastWidgets();
    if ( widgets.length == 0) throw new IllegalArgumentException( "Window does not have a widget: "+xidget);

    int swingMessageType = JOptionPane.PLAIN_MESSAGE;
    switch( type)
    {
      case error: swingMessageType = JOptionPane.ERROR_MESSAGE; break;
      case warning: swingMessageType = JOptionPane.WARNING_MESSAGE; break;
      case information: swingMessageType = JOptionPane.INFORMATION_MESSAGE; break;
    }
    
    message = BuildLabelHtml.buildHtml( message);
    
    if ( image == null) JOptionPane.showMessageDialog( (Component)widgets[ 0], message, title, swingMessageType);
    else JOptionPane.showMessageDialog( (Component)widgets[ 0], message, title, swingMessageType, (ImageIcon)image);
  }

  /* (non-Javadoc)
   * @see org.xidget.IToolkit#openFileDialog(org.xmodel.xpath.expression.StatefulContext, org.xmodel.xpath.expression.IExpression, java.lang.String, org.xidget.IToolkit.FileDialogType)
   */
  public String[] openFileDialog( StatefulContext context, IExpression dir, IExpression filter, String desc, FileDialogType type)
  {
    IFocusFeature focusFeature = Creator.getToolkit().getFeature( IFocusFeature.class);
    IXidget xidget = getWindow( focusFeature.getFocus());
    
    String folder = (dir != null)? dir.evaluateString( context): ".";
    
    JFileChooser fileChooser = new JFileChooser( folder);
    fileChooser.setMultiSelectionEnabled( type == FileDialogType.openMany);
    
    // programmatically select details view
    Stack<JComponent> stack = new Stack<JComponent>();
    stack.push( fileChooser);
    while( !stack.empty())
    {
      JComponent component = stack.pop();
      
      if( component instanceof JToggleButton) 
      {  
        JToggleButton button = (JToggleButton)component;
        if ( button.getToolTipText().equals( "Details")) { button.doClick(); break;}
      }  
      
      for( Component child: component.getComponents())
        if ( child instanceof JComponent)
          stack.push( (JComponent)child);
    }
    
    // allow selecting directories unless the dialog is intended for picking a non-existing file 
    if ( type != FileDialogType.save) 
      fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
    
    if ( filter != null)
    {
      FileFilter fileFilter = new ExpressionFileFilter( context, filter, desc);
      fileChooser.setFileFilter( fileFilter);
    }
    
    IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    if ( creationFeature == null) return new String[ 0];
    
    Object[] widgets = creationFeature.getLastWidgets();
    if ( widgets.length == 0) return new String[ 0];
    
    int status = (type == FileDialogType.save)?
      fileChooser.showSaveDialog( (Component)widgets[ 0]):
      fileChooser.showOpenDialog( (Component)widgets[ 0]);
    
    if ( status == JFileChooser.APPROVE_OPTION)
    {
      if ( dir != null && dir.getType( context) == ResultType.NODES)
      {
        IModelObject dirNode = dir.queryFirst( context);
        if ( dirNode != null) dirNode.setValue( fileChooser.getCurrentDirectory());
      }
      
      if ( type == FileDialogType.openMany)
      {
        File[] files = fileChooser.getSelectedFiles();
        String[] result = new String[ files.length];
        for( int i=0; i<files.length; i++) result[ i] = files[ i].getPath();
        return result;
      }
      else
      {
        File file = fileChooser.getSelectedFile();
        return new String[] { file.getPath()};
      }
    }
    
    return new String[ 0];
  }
  
  /**
   * An implementation of FileFilter that uses an xpath expression to filter the files.
   */
  private static class ExpressionFileFilter extends FileFilter
  {
    public ExpressionFileFilter( StatefulContext parent, IExpression filter, String description)
    {
      this.filter = filter;
      this.parent = new StatefulContext( parent);
      this.description = description;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    @Override
    public String getDescription()
    {
      return description;
    }

    /* (non-Javadoc)
     * @see java.io.FileFilter#accept(java.io.File)
     */
    public boolean accept( File pathname)
    {
      parent.set( "value", pathname.getPath());
      return filter.evaluateBoolean( parent);
    }
    
    private StatefulContext parent;
    private IExpression filter;
    private String description;
  }
  
  private IAsyncFeature asyncFeature;
  private IFocusFeature focusFeature;
  private IColorFeature<Color, Graphics2D> colorFeature;
  private IPrintFeature printFeature;
}
