package org.xidget.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.SpringLayout;
import org.xidget.IXidget;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.swing.ISwingWidgetAdapter;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * A class which creates a layout for its components from the configuration.
 * <layout name="l1">
 *   <x0>[[p|n].[x0|x1|y0|y1|c]][(+/-)N]</x0>
 *   <y0>[[p|n].[x0|x1|y0|y1|c]][(+/-)N]</y0>
 *   <x1>[[p|n].[x0|x1|y0|y1|c]][(+/-)N]</x1>
 *   <y1>[[p|n].[x0|x1|y0|y1|c]][(+/-)N]</y1>
 * </layout>
 */
public class LayoutFactory implements ITagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    return true;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    Layout layout = new Layout();
    
    String text = Xlate.childGet( element, "x0", (String)null);
    if ( text != null) layout.x0 = createCoordinate( text);
    
    text = Xlate.childGet( element, "y0", (String)null);
    if ( text != null) layout.y0 = createCoordinate( text); 

    text = Xlate.childGet( element, "x1", (String)null);
    if ( text != null) layout.x1 = createCoordinate( text); 

    text = Xlate.childGet( element, "y1", (String)null);
    if ( text != null) layout.y1 = createCoordinate( text);
    
    return false;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#exit(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
  }
  
  /**
   * Parse the specified text into a layout coordinate.
   * @param text The text.
   */
  private Coordinate createCoordinate( String text) throws TagException
  {
    Coordinate coordinate = new Coordinate();
    
    char attach = text.charAt( 0);
    if ( attach != 'p' && attach != 'n') 
      throw new TagException( "Illegal: first character must be 'p' or 'n'.");
    
    coordinate.previous = (attach == 'p');
    
    int index = text.indexOf( '.') + 1;
    char letter = text.charAt( index);
    if ( letter != 'x' && letter != 'y')
      throw new TagException( "Illegal: coordinate must follow period."); 

    char number = text.charAt( index++);
    if ( number != '0' && number != '1')
      throw new TagException( "Illegal: coordinate must follow period."); 

    if ( letter == 'x')
    {
      coordinate.relative = (number == '0')? Relative.x0: Relative.x1; 
    }
    else
    {
      coordinate.relative = (number == '0')? Relative.y0: Relative.y1; 
    }
    
    try
    {
      coordinate.offset = Integer.parseInt( text.substring( index));
    }
    catch( NumberFormatException e)
    {
      throw new TagException( "Illegal: coordinate offset is not an integer.", e);
    }
    
    return coordinate;
  }

  /**
   * Apply the layout to a parent xidget. This method assumes that the xidget has 
   * an ISwingWidgetAdapter and that each and every child widget is associate with
   * a xidget.
   * @param parent The parent xidget;
   * @param children The children of the parent.
   */
  public void applyLayout( IXidget parent, List<IXidget> children)
  {
    Container widgetParent = getWidget( parent);
    Component[] widgetChildren = widgetParent.getComponents();
    applyLayout( widgetParent, widgetChildren, children);
  }

  /**
   * Returns the widget of the specified xidget.
   * @param xidget The xidget.
   * @return Returns the widget of the specified xidget.
   */
  private JComponent getWidget( IXidget xidget)
  {
    ISwingWidgetAdapter adapter = (ISwingWidgetAdapter)xidget.getAdapter( ISwingWidgetAdapter.class);
    if ( adapter != null) return adapter.getWidget();    
    return null;
  }
  
  /**
   * Intepret the specified layouts for the specified children.
   * @param parent The parent.
   * @param children The children.
   * @param layouts The layouts.
   */
  private void applyLayout( Container parent, Component[] children, List<IXidget> xidgets)
  {
    SpringLayout springLayout = new SpringLayout();
    Component prev = parent;
    Component curr = children[ 0];
    Component next = null;
    int size = children.length;
    for( int i=1; i<=size; i++)
    {
      String layoutName = references.get( xidgets.get( i-1));
      Layout layout = layouts.get( layoutName); 

      if ( layout.x0 != null)
      {
        Component other = layout.x0.previous? prev: next;
        springLayout.putConstraint( SpringLayout.EAST, curr, layout.x0.offset, convertRelative( layout.x0.relative), other);
      }
      
      if ( layout.y0 != null)
      {
        Component other = layout.y0.previous? prev: next;
        springLayout.putConstraint( SpringLayout.NORTH, curr, layout.y0.offset, convertRelative( layout.y0.relative), other);
      }
      
      if ( layout.x1 != null)
      {
        Component other = layout.x1.previous? prev: next;
        springLayout.putConstraint( SpringLayout.WEST, curr, layout.x1.offset, convertRelative( layout.x1.relative), other);
      }
      
      if ( layout.y1 != null)
      {
        Component other = layout.y1.previous? prev: next;
        springLayout.putConstraint( SpringLayout.SOUTH, curr, layout.y1.offset, convertRelative( layout.y1.relative), other);
      }
      
      prev = curr;
      curr = next;
      next = (i < size)? children[ i]: parent; 
    }      
  }
    
  /**
   * Convert relative side designation.
   * @param relative The relative designation.
   * @return Returns the SpringLayout side designation.
   */
  private String convertRelative( Relative relative)
  {
    switch( relative)
    {
      case x0: return SpringLayout.EAST;
      case y0: return SpringLayout.NORTH;
      case x1: return SpringLayout.WEST;
      case y1: return SpringLayout.SOUTH;
      case center: return SpringLayout.EAST;  
    }
    return null;
  }

  private enum Relative { x0, y0, x1, y1, center};
  
  private class Coordinate
  {
    public boolean previous;
    public Relative relative;
    public int offset;
  }
  
  private class Layout
  {
    public Coordinate x0;
    public Coordinate y0;
    public Coordinate x1;
    public Coordinate y1;
  }
 
  private Map<IXidget, String> references;
  private Map<String, Layout> layouts;
}
