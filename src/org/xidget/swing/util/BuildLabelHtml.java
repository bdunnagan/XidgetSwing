/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.swing.util;

/**
 * Build a string containing HTML for display in HTML-aware Swing widgets.  This technique is used
 * to implement the xidget requirement of support for new-lines in labels.
 */
public class BuildLabelHtml
{
  /**
   * Build an HTML string for the specified text that contains new-lines.  If the text does not
   * contain new-lines then it is returned unchanged.
   * @param text The text containing new-lines.
   * @return Returns the html string.
   */
  public static String buildHtml( String text)
  {
    if ( !text.contains( "\n")) return text;

    text = text.replace( "\n", "<br/>");
    
    StringBuilder html = new StringBuilder();
    html.append( "<html>");
    html.append( text);
    html.append( "</html>");
    return html.toString();
  }
}
