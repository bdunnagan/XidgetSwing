/*
 * Copyright 2006-2008 Kees de Kooter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.swing.xmleditor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import sun.swing.SwingUtilities2;

/**
 * JTextPane implementation that can handle xml text. The IndentKeyListener
 * implements smart indenting.
 * @author kees, bdunnagan
 * @date 27-jan-2006
 */
@SuppressWarnings("unused")
public class XmlTextPane extends JTextPane
{
  private static final long serialVersionUID = 6270183148379328084L;
  
  public XmlTextPane()
  {
    this.setEditorKitForContentType( "text/xml", new XmlEditorKit());
    this.setContentType( "text/xml");
    this.tabIndent = 2;

    addKeyListener( new AutoKeyListener());
  }
  
  /**
   * Returns the number of spaces to indent.
   * @return Returns the number of spaces to indent.
   */
  private int determineIndent()
  {
    int index = getCaretPosition();
    while( index > 0)
    {
      index--;
      String line = getLineAt( index);
      if ( line.trim().length() > 0) return getIndentAt( index);
    }
    return 0;
  }
  
  /**
   * Returns the start of the line containing the specified index.
   * @param index An index into the document.
   * @return Returns the start of the line containing the specified index.
   */
  private int getStartOfLine( int index)
  {
    String text = getText();
    for( int i=index-1; i>=0; i--)
      if ( text.charAt( i) == '\n')
        return i+1;
    return 0;
  }
  
  /**
   * Returns the end of the line containing the specified index.
   * @param index An index into the document.
   * @return Returns the end of the line containing the specified index.
   */
  private int getEndOfLine( int index)
  {
    String text = getText();
    int end = text.indexOf( '\n', index);
    if ( end < 0) return text.length();
    return end;
  }
  
  /**
   * Returns the entire line containing the specified index not including the EOL.
   * @param index The index of a character in the document.
   * @return Returns the entire line containing the index.
   */
  private String getLineAt( int index)
  {
    // find start and end
    int end = getEndOfLine( index);
    int start = getStartOfLine( index);

    // extract line
    String text = getText();
    return text.substring( start, end);
  }
  
  /**
   * Returns the number of spaces at the beginning of the line containing the specified index.
   * @param index An index into the document.
   */
  private int getIndentAt( int index)
  {
    String text = getText();
    int indent = 0;
    for( int i = getStartOfLine( index); text.charAt( i) != '\n'; i++)
    {
      if ( text.charAt( i) == '\t') indent += tabIndent;
      else if ( text.charAt( i) == ' ') indent++;
      else break;
    }
    return indent;
  }
  
  /**
   * Goto the beginning of the document.
   */
  public void gotoStart()
  {
    setCaretPosition( 0);
  }
  
  /**
   * Goto the end of the document.
   */
  public void gotoEnd()
  {
    setCaretPosition( getDocument().getLength());
  }
  
  /**
   * Goto the beginning of the line containing the caret.
   * @param smart True if line beginning may be either beginning of text or beginning of line.
   */
  public void gotoLineStart( boolean smart)
  {
    int caret = getCaretPosition();
    if ( caret == 0) return;
    
    String text = getText();
    for( int i=caret-1; i>=0; i--)
    {
      if ( text.charAt( i) == '\n')
      {
        int lineStart = getStartOfLine( i+1);
        int indent = getIndentAt( i+1);
        if ( smart && caret != lineStart + indent)
        {
          setCaretPosition( i+1+indent);
        }
        else
        {
          setCaretPosition( i+1);
        }
        break;
      }
    }
  }
  
  /**
   * Goto the end of the line containing the caret.
   */
  public void gotoLineEnd()
  {
    int caret = getCaretPosition();
    String text = getText();
    int index = text.indexOf( '\n', caret);
    if ( index < 0) index = text.length();
    setCaretPosition( index);
  }
  
  /**
   * Delete current line.
   */
  public void deleteLine()
  {
    Document document = getDocument();
    int caret = getCaretPosition();
    int start = getStartOfLine( caret);
    int end = getEndOfLine( caret) + 1;
    try
    {
      if ( end > document.getLength())
      {
        start--;
        end--;
      }
      
      if ( start >= 0) document.remove( start, end-start);
    }
    catch ( BadLocationException e)
    {
      e.printStackTrace();
    }
  }
  
  /**
   * Search for the previous start or end tag and return the index and set the foundTag member. 
   * @param index The start index of the search.
   * @return Returns -1 or the index of the found tag.
   */
  private int findPreviousTag( int index)
  {
    int greaterThanIndex = -1;
    int lessThanIndex = -1;
    
    String text = getText();
    for( int i=index; i>=0; i--)
    {
      char c = text.charAt( i);
      if ( greaterThanIndex >= 0)
      {
        if ( c == '\n') 
        {
          greaterThanIndex = -1;
        }
        else if ( c == '<') 
        {
          lessThanIndex = i;
          break;
        }
      }
      else
      {
        if ( c == '>') greaterThanIndex = i;
      }
    }
    
    if ( lessThanIndex >= 0 && greaterThanIndex >= 0)
    {
      foundTag = text.substring( lessThanIndex, greaterThanIndex+1);
      return lessThanIndex;
    }
    
    return -1;
  }
  
  /**
   * Search for the next start or end tag and return the index and set the foundTag member.
   * @param index The start index of the search.
   * @return Returns -1 or the index of the found tag.
   */
  private int findNextTag( int index)
  {
    int greaterThanIndex = -1;
    int lessThanIndex = -1;
    
    String text = getText();
    for( int i=index; i<text.length(); i++)
    {
      char c = text.charAt( i);
      if ( lessThanIndex >= 0)
      {
        if ( c == '\n') 
        {
          lessThanIndex = -1;
        }
        else if ( c == '>') 
        {
          greaterThanIndex = i;
          break;
        }
      }
      else
      {
        if ( c == '<') lessThanIndex = i;
      }
    }
    
    if ( lessThanIndex >= 0 && greaterThanIndex >= 0)
    {
      foundTag = text.substring( lessThanIndex, greaterThanIndex+1);
      return lessThanIndex;
    }
    
    return -1;
  }
  
  /**
   * Find the previous start tag starting that the specified index and set the foundTag member.
   * @param index The starting index.
   * @return Returns -1 or the index of the tag.
   */
  private int findPreviousStartTag( int index)
  {
    int found = findPreviousTag( index);
    while( found > 0 && isEndTag( foundTag))
    {
      found = findPreviousTag( found-1);
    }
    return found;
  }
  
  /**
   * Find the next end tag starting that the specified index and set the foundTag member.
   * @param index The starting index.
   * @return Returns -1 or the index of the tag.
   */
  private int findNextEndTag( int index)
  {
    int found = findNextTag( index);
    while( found > 0 && !isEndTag( foundTag))
    {
      found = findNextTag( found+1);
    }
    return found;
  }
    
  /**
   * Find the end tag corresponding to the start tag at the specified index and set the foundTag member.
   * @param index The index of a start tag.
   * @param name The start tag element name.
   * @return Returns -1 or the index of the corresponding end tag.
   */
  private int findEndTag( int index, String name)
  {
    int nest = 0;
    int found = findNextTag( index+2);
    while( found > 0)
    {
      String tagName = getNameFromTag( foundTag);
      if ( tagName.equals( name))
      {
        if ( isEndTag( foundTag)) nest--; else nest++;
        if ( nest < 0) return found;
      }
      found = findNextTag( found+1);
    }
    return -1;
  }
  
  /**
   * Returns the element name in the specified start or end tag.
   * @param tag The tag including less-than and greater-than.
   * @return Returns the element name in the specified start or end tag.
   */
  private static String getNameFromTag( String tag)
  {
    int start = (tag.charAt( 1) == '/')? 2: 1;
    int gt = tag.indexOf( '>', start);
    int sp = tag.indexOf( ' ', start);
    
    if ( gt < 0) return "";
    int end = (sp >= 0 && sp < gt)? sp: gt;
    
    String name = tag.substring( start, end);
    name = name.trim();
    if ( name.endsWith( "/")) name = name.substring( 0, name.length() - 1);
    return name;
  }
  
  /**
   * Returns true if the specified tag is an end tag.
   * @param tag The tag.
   * @return Returns true if the specified tag is an end tag.
   */
  private static boolean isEndTag( String tag)
  {
    for( int i=1; i<tag.length(); i++)
    {
      char c = tag.charAt( i);
      if ( c == '/') return true;
      if ( c != ' ') return false;
    }
    return false;
  }
  
  /**
   * Returns true if the specified tag is both the start and end of the tag.
   * @param tag The tag.
   * @return Returns true if the specified tag is both the start and end of the tag.
   */
  private static boolean isEmptyTag( String tag)
  {
    for( int i=tag.length()-2; i>=0; i--)
    {
      if ( tag.charAt( i) == '/') return true;
      if ( tag.charAt( i) != ' ') return false;
    }
    return false;
  }
  
  /**
   * Auto-complete tag if appropriate.
   */
  private void autoCompleteTag()
  {
    int caret = getCaretPosition();
    int startIndex = caret;
    while( true)
    {
      startIndex = findPreviousStartTag( startIndex-1);
      if ( startIndex < 0) return;
      
      String startName = getNameFromTag( foundTag);
      if ( isEmptyTag( foundTag)) continue;
      
      int endIndex = findEndTag( startIndex, startName);
      if ( endIndex < 0)
      {
        try
        {
          // auto-complete
          getDocument().insertString( caret, startName+">", null);
          return;
        }
        catch ( BadLocationException e)
        {
          e.printStackTrace();
        }
      }
      
      if ( endIndex >= caret) return;
    }
  }

  /**
   * Key listener for processing auto-indent, auto-completion and tab-indent.
   */
  private class AutoKeyListener implements KeyListener
  {
    public void keyPressed( KeyEvent event)
    {
      if ( event.getKeyCode() == KeyEvent.VK_ENTER && event.getModifiers() == 0)
      {
        int caretPosition = getCaretPosition();
        int indent = determineIndent();
        try
        {
          char[] spaces = new char[ indent];
          Arrays.fill( spaces, ' ');
          
          StringBuilder sb = new StringBuilder();
          sb.append( '\n');
          sb.append( spaces);
          
          getDocument().insertString( caretPosition, sb.toString(), null);
        } 
        catch( BadLocationException e)
        {
          e.printStackTrace();
        }
        
        event.consume();
      }
      
      else if ( event.getKeyCode() == KeyEvent.VK_TAB)
      {
        boolean increase = event.getModifiers() == 0;

        Document doc = getDocument();
        int cs1 = getSelectionStart();
        int cs2 = getSelectionEnd();
        
        try
        {
          String space = new String();
          for( int i=0; i<tabIndent; i++) space += " ";
          
          String text = doc.getText( 0, cs1);
          
          // search backward for first cr
          int cs0 = cs1-1;
          while( text.charAt( cs0) != '\n' && cs0 > 0) cs0--;
          
          // get expanded selection
          text = doc.getText( cs0, cs2 - cs0 - 1);
          
          // replace cr with cr+indent
          for( int c=cs0, s=0; s<text.length(); s++, c++)
          {
            if ( text.charAt( s) == '\n')
            {
              if ( increase)
              {
                doc.insertString( c+1, space, null);
                c += tabIndent;
              }
              else if ( (s + 1 + tabIndent) < text.length())
              {
                for( int i=1; i<=tabIndent; i++)
                {
                  if ( text.charAt( s+i) != ' ') 
                    return;
                }
                doc.remove( c+1, tabIndent);
              }
            }
          }
        }
        catch( Exception e)
        {
          e.printStackTrace( System.err);
        }
        
        event.consume();
      }
      
      else if ( event.getKeyCode() == KeyEvent.VK_HOME && event.getModifiers() == 0)
      {
        gotoLineStart( true);
        event.consume();
      }
      
      else if ( event.getKeyCode() == KeyEvent.VK_END && event.getModifiers() == 0)
      {
        gotoLineEnd();
        event.consume();
      }
      
      else if ( event.getKeyCode() == KeyEvent.VK_HOME && event.getModifiers() == KeyEvent.META_MASK)
      {
        gotoStart();
        event.consume();
      }
      
      else if ( event.getKeyCode() == KeyEvent.VK_END && event.getModifiers() == KeyEvent.META_MASK)
      {
        gotoEnd();
        event.consume();
      }
    }

    public void keyReleased( KeyEvent event)
    {
      if ( event.getKeyChar() == '/' && event.getModifiers() == 0)
      {
        int caret = getCaretPosition();
        String text = getText();
        if ( caret > 1 && text.charAt( caret - 2) == '<')
          autoCompleteTag();
      }
      
      if ( event.getKeyCode() == KeyEvent.VK_BACK_SPACE && event.getModifiers() == KeyEvent.META_MASK)
      {
        deleteLine();
      }
    }

    public void keyTyped( KeyEvent event)
    {
    }
  }

  private int tabIndent;
  private String foundTag;
}
