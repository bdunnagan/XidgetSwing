/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author bdunnagan
 *
 */
public class Fun
{
  public static void main( String[] args) throws Exception
  {    
    final MyPanel panel = new MyPanel();
    
    JFrame frame = new JFrame();
    frame.add( panel);
    frame.pack();
    
    Timer timer = new Timer( 20, new ActionListener() {
      public void actionPerformed( ActionEvent e)
      {
        panel.repaint();
      }
    });
    
    timer.setInitialDelay( 3000);
    timer.start();
    
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
    frame.setSize( 1800, 800);
    frame.setVisible( true);
  }
  
  private float oscillator( int t)
  {
  }
  
  private static class MyPanel extends JPanel 
  {
    public MyPanel()
    {
      setBorder( BorderFactory.createLineBorder( Color.black));
    }

    public Dimension getPreferredSize()
    {
      return new Dimension( 1800, 800);
    }

    public void paintComponent( Graphics g)
    {
      super.paintComponent( g);
      
      g.setColor( Color.gray);
      g.drawLine( 0, 400, 1800, 400);
      
      g.setColor( Color.blue);
      
      long t0 = System.nanoTime();

      double q = (1800 / (p * 2 * Math.PI));
      String text = String.format( "%3.2f", q);
      g.drawString( text, 200, 20);
      
      for( x=0; x<1800; )
      {
        int ox = x;
        int oy = y;
        
        a += b / q;
        b -= a / q;
                
        y = (int)Math.round( a * 200) + 400;
        x++;
        
        g.drawLine( ox, oy, x, y);
      }
      
      long t1 = System.nanoTime();
      text = String.format( "%3.2f us", ((t1-t0) / 1000f));
      g.drawString( text, 5, 20);
    }
    
    Random random = new Random();
    double a = 0;
    double b = 1;
    double c = 0;
    double d = 1;
    double p = 4;
    int x;
    int y;
  }
}
