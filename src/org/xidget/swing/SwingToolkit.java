/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import java.awt.Component;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.xidget.IToolkit;
import org.xidget.IXidget;
import org.xidget.binding.DynamicContainerTagHandler;
import org.xidget.binding.XidgetTagHandler;
import org.xidget.binding.table.TableTagHandler;
import org.xidget.binding.tree.TreeTagHandler;
import org.xidget.config.TagProcessor;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.swing.button.AbstractButtonXidget;
import org.xidget.swing.combo.JComboBoxXidget;
import org.xidget.swing.dialog.JDialogXidget;
import org.xidget.swing.image.ImageFileAssociation;
import org.xidget.swing.menu.JMenuBarXidget;
import org.xidget.swing.menu.JMenuItemXidget;
import org.xidget.swing.menu.MenuTagHandler;
import org.xidget.swing.menu.SubMenuTagHandler;
import org.xidget.swing.table.JTableXidget;
import org.xidget.swing.text.JTextXidget;
import org.xidget.swing.tree.JTreeXidget;
import org.xmodel.Xlate;
import org.xmodel.external.caching.IFileAssociation;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IToolkit for the Swing platform.
 */
public class SwingToolkit implements IToolkit
{
  /* (non-Javadoc)
   * @see org.xidget.IToolkit#configure(org.xidget.config.TagProcessor)
   */
  public void configure( TagProcessor processor)
  {
    processor.addHandler( "app", new XidgetTagHandler( JFrameXidget.class));
    processor.addHandler( "application", new XidgetTagHandler( JFrameXidget.class));
    processor.addHandler( "dialog", new XidgetTagHandler( JDialogXidget.class));
    processor.addHandler( "form", new XidgetTagHandler( JPanelXidget.class));
    processor.addHandler( "tabs", new DynamicContainerTagHandler( JTabbedPaneXidget.class));
    processor.addHandler( "text", new XidgetTagHandler( JTextXidget.class));
    processor.addHandler( "combo", new XidgetTagHandler( JComboBoxXidget.class));
    processor.addHandler( "button", new XidgetTagHandler( AbstractButtonXidget.class));
    processor.addHandler( "menubar", new XidgetTagHandler( JMenuBarXidget.class));
    processor.addHandler( "menu", new MenuTagHandler());
    processor.addHandler( "menu", new SubMenuTagHandler());
    processor.addHandler( "menuItem", new XidgetTagHandler( JMenuItemXidget.class));
    processor.addHandler( "separator", new XidgetTagHandler( JMenuItemXidget.class));
    processor.addHandler( "table", new TableTagHandler( JTableXidget.class));
    processor.addHandler( "tree", new TreeTagHandler( JTreeXidget.class));
  }

  /* (non-Javadoc)
   * @see org.xidget.IToolkit#getImageFileAssociation()
   */
  public IFileAssociation getImageFileAssociation()
  {
    return new ImageFileAssociation();
  }

  /* (non-Javadoc)
   * @see org.xidget.IToolkit#openConfirmDialog(org.xidget.IXidget, org.xmodel.xpath.expression.StatefulContext, java.lang.String, java.lang.Object, java.lang.String, boolean)
   */
  public Confirmation openConfirmDialog( IXidget xidget, StatefulContext context, String title, Object image, String message, boolean allowCancel)
  {
    IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    if ( creationFeature == null) throw new IllegalArgumentException( "Window xidget does not have an IWidgetCreationFeature instance: "+xidget);
    
    Object[] widgets = creationFeature.getLastWidgets();
    if ( widgets.length == 0) throw new IllegalArgumentException( "Window does not have a widget: "+xidget);
    
    int choice = JOptionPane.showConfirmDialog( (Component)widgets[ 0], message, title, allowCancel? JOptionPane.YES_NO_CANCEL_OPTION: JOptionPane.YES_NO_OPTION);
    
    switch( choice)
    {
      case JOptionPane.YES_OPTION: return Confirmation.yes;
      case JOptionPane.NO_OPTION: return Confirmation.no;
      default: return Confirmation.cancel;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.IToolkit#openMessageDialog(org.xidget.IXidget, org.xmodel.xpath.expression.StatefulContext, java.lang.String, java.lang.Object, java.lang.String, org.xidget.IToolkit.MessageType)
   */
  public void openMessageDialog( IXidget xidget, StatefulContext context, String title, Object image, String message, MessageType type)
  {
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
    
    if ( image == null) JOptionPane.showMessageDialog( (Component)widgets[ 0], message, title, swingMessageType);
    else JOptionPane.showMessageDialog( (Component)widgets[ 0], message, title, swingMessageType, (ImageIcon)image);
  }

  /* (non-Javadoc)
   * @see org.xidget.IToolkit#openFileDialog(org.xidget.IXidget, org.xmodel.xpath.expression.StatefulContext, org.xmodel.xpath.expression.IExpression, java.lang.String, org.xidget.IToolkit.FileDialogType)
   */
  public String[] openFileDialog( IXidget xidget, StatefulContext context, IExpression filter, String description, FileDialogType type)
  {
    File dir = new File( Xlate.get( context.getObject(), "/"));
    JFileChooser fileChooser = new JFileChooser( dir);
    fileChooser.setMultiSelectionEnabled( type == FileDialogType.openMany);
    
    // allow selecting directories unless the dialog is intended for picking a non-existing file 
    if ( type != FileDialogType.save) 
      fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
    
    FileFilter fileFilter = new ExpressionFileFilter( context, filter, description);
    fileChooser.setFileFilter( fileFilter);
    
    IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    if ( creationFeature == null) return new String[ 0];
    
    Object[] widgets = creationFeature.getLastWidgets();
    if ( widgets.length == 0) return new String[ 0];
    
    int status = (type == FileDialogType.save)?
      fileChooser.showSaveDialog( (Component)widgets[ 0]):
      fileChooser.showOpenDialog( (Component)widgets[ 0]);
    
    if ( status == JFileChooser.APPROVE_OPTION)
    {
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
      parent.set( "v", pathname.getPath());
      return filter.evaluateBoolean( parent);
    }
    
    private StatefulContext parent;
    private IExpression filter;
    private String description;
  }
}
