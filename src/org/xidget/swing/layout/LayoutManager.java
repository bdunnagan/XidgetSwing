package org.xidget.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SpringLayout;
import org.xidget.IXidget;
import org.xidget.layout.LayoutTagHandler.Layout;
import org.xidget.layout.LayoutTagHandler.Relative;
import org.xidget.swing.ISwingWidgetAdapter;

/**
 * A class which interprets xidget layout constraints into Swing/AWT layouts.
 */
public class LayoutManager
{  
  /**
   * Apply the configured layout to the children of the specified xidget. 
   * @param xidget The xidget whose children will receive layouts.
   */
  public void applyLayout( IXidget xidget)
  {
    List<IXidget> children = xidget.getChildren();
    Container widgetParent = getWidget( xidget);
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
    SpringLayout springLayout = (SpringLayout)parent.getLayout();
    Component prev = parent;
    Component curr = children[ 0];
    Component next = null;
    int size = children.length;
    for( int i=1; i<=size; i++)
    {
      Layout layout = xidgets.get( i-1).getLayout();

      // Java 1.5 SpringLayout requires East, South, West, North ordering
      if ( layout.x0 != null)
      {
        Component other = layout.x0.previous? prev: next;
        springLayout.putConstraint( SpringLayout.EAST, curr, layout.x0.offset, convertRelative( layout.x0.relative), other);
      }
      
      if ( layout.y1 != null)
      {
        Component other = layout.y1.previous? prev: next;
        springLayout.putConstraint( SpringLayout.SOUTH, curr, layout.y1.offset, convertRelative( layout.y1.relative), other);
      }
      
      if ( layout.x1 != null)
      {
        Component other = layout.x1.previous? prev: next;
        springLayout.putConstraint( SpringLayout.WEST, curr, layout.x1.offset, convertRelative( layout.x1.relative), other);
      }
      
      if ( layout.y0 != null)
      {
        Component other = layout.y0.previous? prev: next;
        springLayout.putConstraint( SpringLayout.NORTH, curr, layout.y0.offset, convertRelative( layout.y0.relative), other);
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
}
