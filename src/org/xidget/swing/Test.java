/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * @author bdunnagan
 *
 */
public class Test
{
  public static void main( String[] args) throws Exception
  {
    for ( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
    {
      if ( "Nimbus".equals( info.getName()))
      {
        UIManager.setLookAndFeel( info.getClassName());
        break;
      }
    }
    
    JTextField text = new JTextField( "Text");
    System.out.println( text.getPreferredSize());
    
    JButton button = new JButton( "Text");
    System.out.println( button.getPreferredSize());
    
    JPanel panel = new JPanel();
    panel.add( text);
    panel.add( button);
    
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    
    frame.getContentPane().add( panel);
    
    frame.pack();
    System.out.println( button.getPreferredSize());
    frame.setVisible( true);
    
    
  }
}
