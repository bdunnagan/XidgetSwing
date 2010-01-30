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
    
    lookup.put( KeyEvent.VK_F1, "f1");
    lookup.put( KeyEvent.VK_F2, "f2");
    lookup.put( KeyEvent.VK_F3, "f3");
    lookup.put( KeyEvent.VK_F4, "f4");
    lookup.put( KeyEvent.VK_F5, "f5");
    lookup.put( KeyEvent.VK_F6, "f6");
    lookup.put( KeyEvent.VK_F7, "f7");
    lookup.put( KeyEvent.VK_F8, "f8");
    lookup.put( KeyEvent.VK_F9, "f9");
    lookup.put( KeyEvent.VK_F10, "f10");
    lookup.put( KeyEvent.VK_F11, "f11");
    lookup.put( KeyEvent.VK_F12, "f12");
    lookup.put( KeyEvent.VK_F13, "f13");
    lookup.put( KeyEvent.VK_F14, "f14");
    lookup.put( KeyEvent.VK_F15, "f15");
    lookup.put( KeyEvent.VK_F16, "f16");
    lookup.put( KeyEvent.VK_F17, "f17");
    lookup.put( KeyEvent.VK_F18, "f18");
    lookup.put( KeyEvent.VK_F19, "f19");
    lookup.put( KeyEvent.VK_F20, "f20");
    lookup.put( KeyEvent.VK_F21, "f21");
    lookup.put( KeyEvent.VK_F22, "f22");
    lookup.put( KeyEvent.VK_F23, "f23");
    lookup.put( KeyEvent.VK_F24, "f24");
    
    lookup.put( KeyEvent.VK_ALT, "alt");
    lookup.put( KeyEvent.VK_ALT_GRAPH, "altgraph");
    lookup.put( KeyEvent.VK_CONTROL, "control");
    lookup.put( KeyEvent.VK_META, "meta");
    lookup.put( KeyEvent.VK_SHIFT, "shift");
    
    lookup.put( KeyEvent.VK_TAB, "tab");
    lookup.put( KeyEvent.VK_ESCAPE, "escape");
    lookup.put( KeyEvent.VK_BACK_SPACE, "backspace");
    lookup.put( KeyEvent.VK_ENTER, "enter");
    
    lookup.put( KeyEvent.VK_CAPS_LOCK, "capslock");
    lookup.put( KeyEvent.VK_NUM_LOCK, "numlock");
    lookup.put( KeyEvent.VK_SCROLL_LOCK, "scrolllock");
    
    lookup.put( KeyEvent.VK_HOME, "home");
    lookup.put( KeyEvent.VK_END, "end");
    lookup.put( KeyEvent.VK_INSERT, "insert");
    lookup.put( KeyEvent.VK_PAGE_UP, "pageup");
    lookup.put( KeyEvent.VK_PAGE_DOWN, "pagedown");
    
    lookup.put( KeyEvent.VK_CUT, "cut");
    lookup.put( KeyEvent.VK_PASTE, "paste");
    lookup.put( KeyEvent.VK_COPY, "copy");
    
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
    lookup.put( KeyEvent.VK_MULTIPLY, "multiply");
    lookup.put( KeyEvent.VK_SUBTRACT, "subtract");
    lookup.put( KeyEvent.VK_ASTERISK, "*");
    lookup.put( KeyEvent.VK_NUMBER_SIGN, "#");
    
    lookup.put( KeyEvent.VK_ACCEPT, "accept");
    lookup.put( KeyEvent.VK_AGAIN, "again");
    lookup.put( KeyEvent.VK_ALL_CANDIDATES, "allcandidates");
    lookup.put( KeyEvent.VK_ALPHANUMERIC, "alphanumeric");
    lookup.put( KeyEvent.VK_BEGIN, "begin");
    lookup.put( KeyEvent.VK_CANCEL, "cancel");
    lookup.put( KeyEvent.VK_CLEAR, "clear");
    lookup.put( KeyEvent.VK_CONTEXT_MENU, "contextmenu");
    lookup.put( KeyEvent.VK_CONVERT, "convert");
        
    lookup.put( KeyEvent.VK_EURO_SIGN, "eurosign");
    lookup.put( KeyEvent.VK_FINAL, "final");
    lookup.put( KeyEvent.VK_FIND, "find");
    lookup.put( KeyEvent.VK_FULL_WIDTH, "fullwidth");
    lookup.put( KeyEvent.VK_HALF_WIDTH, "halfwidth");
    lookup.put( KeyEvent.VK_HELP, "help");
        
    lookup.put( KeyEvent.VK_KP_UP, "keypadup");
    lookup.put( KeyEvent.VK_KP_LEFT, "keypadleft");
    lookup.put( KeyEvent.VK_KP_RIGHT, "keypadright");
    lookup.put( KeyEvent.VK_KP_DOWN, "keypaddown");
    
    lookup.put( KeyEvent.VK_UP, "up");
    lookup.put( KeyEvent.VK_LEFT, "left");
    lookup.put( KeyEvent.VK_RIGHT, "right");
    lookup.put( KeyEvent.VK_DOWN, "down");
    
    
    lookup.put( KeyEvent.VK_NUMPAD0, "numpad0");
    lookup.put( KeyEvent.VK_NUMPAD1, "numpad1");
    lookup.put( KeyEvent.VK_NUMPAD2, "numpad2");
    lookup.put( KeyEvent.VK_NUMPAD3, "numpad3");
    lookup.put( KeyEvent.VK_NUMPAD4, "numpad4");
    lookup.put( KeyEvent.VK_NUMPAD5, "numpad5");
    lookup.put( KeyEvent.VK_NUMPAD6, "numpad6");
    lookup.put( KeyEvent.VK_NUMPAD7, "numpad7");
    lookup.put( KeyEvent.VK_NUMPAD8, "numpad8");
    lookup.put( KeyEvent.VK_NUMPAD9, "numpad9");
    
    lookup.put( KeyEvent.VK_PAUSE, "pause");
    lookup.put( KeyEvent.VK_PERIOD, ".");
    lookup.put( KeyEvent.VK_PRINTSCREEN, "printscreen");
    lookup.put( KeyEvent.VK_QUOTE, "'");
    lookup.put( KeyEvent.VK_QUOTEDBL, "\"");
    lookup.put( KeyEvent.VK_SEMICOLON, ";");
    lookup.put( KeyEvent.VK_SEPARATOR, "separator");
    lookup.put( KeyEvent.VK_SPACE, "space");
    lookup.put( KeyEvent.VK_STOP, "stop");
    lookup.put( KeyEvent.VK_UNDERSCORE, "_");
    lookup.put( KeyEvent.VK_UNDO, "undo");
    lookup.put( KeyEvent.VK_WINDOWS, "windows");
    lookup.put( KeyEvent.VK_COMMA, "comma");
    lookup.put( KeyEvent.VK_COLON, ":");
  }
}
