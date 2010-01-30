/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.feature;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import org.xidget.IXidget;
import org.xidget.ifeature.IKeyFeature;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.util.KeyTree;
import org.xmodel.xaction.IXAction;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IKeyboardFeature for the Swing Control class.
 */
public class KeyFeature extends KeyAdapter implements IKeyFeature
{
  public KeyFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.keyTree = new KeyTree<IXAction>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IKeyboardFeature#bind(java.lang.String, org.xmodel.xaction.IXAction)
   */
  public void bind( String keys, IXAction script)
  {
    register();
    keyTree.bind( keys, script);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IKeyboardFeature#unbind(java.lang.String, org.xmodel.xaction.IXAction)
   */
  public void unbind( String keys, IXAction script)
  {
    keyTree.unbind( keys);
  }

  /**
   * Register this KeyListener on the JComponent.
   */
  private void register()
  {
    if ( !registered)
    {
      IWidgetCreationFeature feature = xidget.getFeature( IWidgetCreationFeature.class);
      Object[] widgets = feature.getLastWidgets();
      for( int i=0; i<widgets.length; i++)
      {
        JComponent component = (JComponent)widgets[ i];
        component.addKeyListener( this);
      }
      registered = true;
    }
  }
  
  /* (non-Javadoc)
   * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
   */
  @Override
  public void keyPressed( KeyEvent e)
  {
    String key = lookup.get( e.getKeyCode());
    if ( key != null)
    {
      IXAction script = keyTree.keyDown( key);
      if ( script != null)
      {
        e.consume();
        IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
        StatefulContext context = contextFeature.getContext( e.getSource());
        script.run( context);
      }
    }
  }

  /* (non-Javadoc)
   * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
   */
  @Override
  public void keyReleased( KeyEvent e)
  {
    String key = lookup.get( e.getKeyCode());
    if ( key != null) keyTree.keyUp( key);
  }

  private IXidget xidget;
  private boolean registered;
  private KeyTree<IXAction> keyTree;
  
  private static final Map<Integer, String> lookup = new HashMap<Integer, String>();
  
  static
  {
    //
    // Normal printable keys
    //
    lookup.put( KeyEvent.VK_0, "0");
    lookup.put( KeyEvent.VK_1, "1");
    lookup.put( KeyEvent.VK_2, "2");
    lookup.put( KeyEvent.VK_3, "3");
    lookup.put( KeyEvent.VK_4, "4");
    lookup.put( KeyEvent.VK_5, "5");
    lookup.put( KeyEvent.VK_6, "6");
    lookup.put( KeyEvent.VK_7, "7");
    lookup.put( KeyEvent.VK_8, "8");
    lookup.put( KeyEvent.VK_9, "9");
    
    lookup.put( KeyEvent.VK_A, "a");
    lookup.put( KeyEvent.VK_B, "b");
    lookup.put( KeyEvent.VK_C, "c");
    lookup.put( KeyEvent.VK_D, "d");
    lookup.put( KeyEvent.VK_E, "e");
    lookup.put( KeyEvent.VK_F, "f");
    lookup.put( KeyEvent.VK_G, "g");
    lookup.put( KeyEvent.VK_H, "h");
    lookup.put( KeyEvent.VK_I, "i");
    lookup.put( KeyEvent.VK_J, "j");
    lookup.put( KeyEvent.VK_K, "k");
    lookup.put( KeyEvent.VK_L, "l");
    lookup.put( KeyEvent.VK_M, "m");
    lookup.put( KeyEvent.VK_N, "n");
    lookup.put( KeyEvent.VK_O, "o");
    lookup.put( KeyEvent.VK_P, "p");
    lookup.put( KeyEvent.VK_Q, "q");
    lookup.put( KeyEvent.VK_R, "r");
    lookup.put( KeyEvent.VK_S, "s");
    lookup.put( KeyEvent.VK_T, "t");
    lookup.put( KeyEvent.VK_U, "u");
    lookup.put( KeyEvent.VK_V, "v");
    lookup.put( KeyEvent.VK_W, "w");
    lookup.put( KeyEvent.VK_X, "x");
    lookup.put( KeyEvent.VK_Y, "y");
    lookup.put( KeyEvent.VK_Z, "z");
    
    lookup.put( KeyEvent.VK_AMPERSAND, "&");
    lookup.put( KeyEvent.VK_AT, "@");
    lookup.put( KeyEvent.VK_LEFT_PARENTHESIS, "(");
    lookup.put( KeyEvent.VK_RIGHT_PARENTHESIS, ")");
    lookup.put( KeyEvent.VK_BRACELEFT, "{");
    lookup.put( KeyEvent.VK_BRACERIGHT, "}");
    lookup.put( KeyEvent.VK_OPEN_BRACKET, "[");
    lookup.put( KeyEvent.VK_CLOSE_BRACKET, "]");
    lookup.put( KeyEvent.VK_LESS, "<");
    lookup.put( KeyEvent.VK_GREATER, ">");
    lookup.put( KeyEvent.VK_SLASH, "/");
    lookup.put( KeyEvent.VK_BACK_SLASH, "\\");    
    lookup.put( KeyEvent.VK_MINUS, "-");
    lookup.put( KeyEvent.VK_PLUS, "+");
    lookup.put( KeyEvent.VK_ASTERISK, "*");
    lookup.put( KeyEvent.VK_NUMBER_SIGN, "#");
    lookup.put( KeyEvent.VK_PERIOD, ".");
    lookup.put( KeyEvent.VK_QUOTE, "'");
    lookup.put( KeyEvent.VK_QUOTEDBL, "\"");
    lookup.put( KeyEvent.VK_SEMICOLON, ";");
    lookup.put( KeyEvent.VK_UNDERSCORE, "_");
    lookup.put( KeyEvent.VK_COLON, ":");
    
    //
    // Special keys enumerated in IKeyFeature
    //
    lookup.put( KeyEvent.VK_F1, IKeyFeature.Key.f1.toString());
    lookup.put( KeyEvent.VK_F2, IKeyFeature.Key.f2.toString());
    lookup.put( KeyEvent.VK_F3, IKeyFeature.Key.f3.toString());
    lookup.put( KeyEvent.VK_F4, IKeyFeature.Key.f4.toString());
    lookup.put( KeyEvent.VK_F5, IKeyFeature.Key.f5.toString());
    lookup.put( KeyEvent.VK_F6, IKeyFeature.Key.f6.toString());
    lookup.put( KeyEvent.VK_F7, IKeyFeature.Key.f7.toString());
    lookup.put( KeyEvent.VK_F8, IKeyFeature.Key.f8.toString());
    lookup.put( KeyEvent.VK_F9, IKeyFeature.Key.f9.toString());
    lookup.put( KeyEvent.VK_F10, IKeyFeature.Key.f10.toString());
    lookup.put( KeyEvent.VK_F11, IKeyFeature.Key.f11.toString());
    lookup.put( KeyEvent.VK_F12, IKeyFeature.Key.f12.toString());
    lookup.put( KeyEvent.VK_F13, IKeyFeature.Key.f13.toString());
    lookup.put( KeyEvent.VK_F14, IKeyFeature.Key.f14.toString());
    lookup.put( KeyEvent.VK_F15, IKeyFeature.Key.f15.toString());
    lookup.put( KeyEvent.VK_F16, IKeyFeature.Key.f16.toString());
    lookup.put( KeyEvent.VK_F17, IKeyFeature.Key.f17.toString());
    lookup.put( KeyEvent.VK_F18, IKeyFeature.Key.f18.toString());
    lookup.put( KeyEvent.VK_F19, IKeyFeature.Key.f19.toString());
    lookup.put( KeyEvent.VK_F20, IKeyFeature.Key.f20.toString());
    lookup.put( KeyEvent.VK_F21, IKeyFeature.Key.f21.toString());
    lookup.put( KeyEvent.VK_F22, IKeyFeature.Key.f22.toString());
    lookup.put( KeyEvent.VK_F23, IKeyFeature.Key.f23.toString());
    lookup.put( KeyEvent.VK_F24, IKeyFeature.Key.f24.toString());
    
    lookup.put( KeyEvent.VK_ALT, IKeyFeature.Key.alt.toString());
    lookup.put( KeyEvent.VK_ALT_GRAPH, IKeyFeature.Key.altgraph.toString());
    lookup.put( KeyEvent.VK_CONTROL, IKeyFeature.Key.control.toString());
    lookup.put( KeyEvent.VK_META, IKeyFeature.Key.meta.toString());
    lookup.put( KeyEvent.VK_SHIFT, IKeyFeature.Key.shift.toString());
    
    lookup.put( KeyEvent.VK_TAB, IKeyFeature.Key.tab.toString());
    lookup.put( KeyEvent.VK_ESCAPE, IKeyFeature.Key.escape.toString());
    lookup.put( KeyEvent.VK_BACK_SPACE, IKeyFeature.Key.backspace.toString());
    lookup.put( KeyEvent.VK_ENTER, IKeyFeature.Key.enter.toString());
    
    lookup.put( KeyEvent.VK_CAPS_LOCK, IKeyFeature.Key.capslock.toString());
    lookup.put( KeyEvent.VK_NUM_LOCK, IKeyFeature.Key.numlock.toString());
    lookup.put( KeyEvent.VK_SCROLL_LOCK, IKeyFeature.Key.scrolllock.toString());
    
    lookup.put( KeyEvent.VK_HOME, IKeyFeature.Key.home.toString());
    lookup.put( KeyEvent.VK_END, IKeyFeature.Key.end.toString());
    lookup.put( KeyEvent.VK_INSERT, IKeyFeature.Key.insert.toString());
    lookup.put( KeyEvent.VK_PAGE_UP, IKeyFeature.Key.pageup.toString());
    lookup.put( KeyEvent.VK_PAGE_DOWN, IKeyFeature.Key.pagedown.toString());
    
    lookup.put( KeyEvent.VK_CUT, IKeyFeature.Key.cut.toString());
    lookup.put( KeyEvent.VK_PASTE, IKeyFeature.Key.paste.toString());
    lookup.put( KeyEvent.VK_COPY, IKeyFeature.Key.copy.toString());
    
    lookup.put( KeyEvent.VK_MULTIPLY, IKeyFeature.Key.multiply.toString());
    lookup.put( KeyEvent.VK_SUBTRACT, IKeyFeature.Key.subtract.toString());
    
    lookup.put( KeyEvent.VK_ACCEPT, IKeyFeature.Key.accept.toString());
    lookup.put( KeyEvent.VK_AGAIN, IKeyFeature.Key.again.toString());
    lookup.put( KeyEvent.VK_ALL_CANDIDATES, IKeyFeature.Key.allcandidates.toString());
    lookup.put( KeyEvent.VK_ALPHANUMERIC, IKeyFeature.Key.alphanumeric.toString());
    lookup.put( KeyEvent.VK_BEGIN, IKeyFeature.Key.begin.toString());
    lookup.put( KeyEvent.VK_CANCEL, IKeyFeature.Key.cancel.toString());
    lookup.put( KeyEvent.VK_CLEAR, IKeyFeature.Key.clear.toString());
    lookup.put( KeyEvent.VK_CONTEXT_MENU, IKeyFeature.Key.contextmenu.toString());
    lookup.put( KeyEvent.VK_CONVERT, IKeyFeature.Key.convert.toString());
        
    lookup.put( KeyEvent.VK_EURO_SIGN, IKeyFeature.Key.eurosign.toString());
    lookup.put( KeyEvent.VK_FIND, IKeyFeature.Key.find.toString());
    lookup.put( KeyEvent.VK_FULL_WIDTH, IKeyFeature.Key.fullwidth.toString());
    lookup.put( KeyEvent.VK_HALF_WIDTH, IKeyFeature.Key.halfwidth.toString());
    lookup.put( KeyEvent.VK_HELP, IKeyFeature.Key.help.toString());
        
    lookup.put( KeyEvent.VK_KP_UP, IKeyFeature.Key.keypadup.toString());
    lookup.put( KeyEvent.VK_KP_LEFT, IKeyFeature.Key.keypadleft.toString());
    lookup.put( KeyEvent.VK_KP_RIGHT, IKeyFeature.Key.keypadright.toString());
    lookup.put( KeyEvent.VK_KP_DOWN, IKeyFeature.Key.keypaddown.toString());
    
    lookup.put( KeyEvent.VK_UP, IKeyFeature.Key.up.toString());
    lookup.put( KeyEvent.VK_LEFT, IKeyFeature.Key.left.toString());
    lookup.put( KeyEvent.VK_RIGHT, IKeyFeature.Key.right.toString());
    lookup.put( KeyEvent.VK_DOWN, IKeyFeature.Key.down.toString());
    
    lookup.put( KeyEvent.VK_NUMPAD0, IKeyFeature.Key.numpad0.toString());
    lookup.put( KeyEvent.VK_NUMPAD1, IKeyFeature.Key.numpad1.toString());
    lookup.put( KeyEvent.VK_NUMPAD2, IKeyFeature.Key.numpad2.toString());
    lookup.put( KeyEvent.VK_NUMPAD3, IKeyFeature.Key.numpad3.toString());
    lookup.put( KeyEvent.VK_NUMPAD4, IKeyFeature.Key.numpad4.toString());
    lookup.put( KeyEvent.VK_NUMPAD5, IKeyFeature.Key.numpad5.toString());
    lookup.put( KeyEvent.VK_NUMPAD6, IKeyFeature.Key.numpad6.toString());
    lookup.put( KeyEvent.VK_NUMPAD7, IKeyFeature.Key.numpad7.toString());
    lookup.put( KeyEvent.VK_NUMPAD8, IKeyFeature.Key.numpad8.toString());
    lookup.put( KeyEvent.VK_NUMPAD9, IKeyFeature.Key.numpad9.toString());
    
    lookup.put( KeyEvent.VK_PAUSE, IKeyFeature.Key.pause.toString());
    lookup.put( KeyEvent.VK_PRINTSCREEN, IKeyFeature.Key.printscreen.toString());
    lookup.put( KeyEvent.VK_SEPARATOR, IKeyFeature.Key.separator.toString());
    lookup.put( KeyEvent.VK_SPACE, IKeyFeature.Key.space.toString());
    lookup.put( KeyEvent.VK_STOP, IKeyFeature.Key.stop.toString());
    lookup.put( KeyEvent.VK_UNDO, IKeyFeature.Key.undo.toString());
    lookup.put( KeyEvent.VK_WINDOWS, IKeyFeature.Key.windows.toString());
    lookup.put( KeyEvent.VK_COMMA, IKeyFeature.Key.comma.toString());
  }
}
