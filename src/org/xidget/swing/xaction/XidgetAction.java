package org.xidget.swing.xaction;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.xidget.IXidget;
import org.xidget.config.Configuration;
import org.xidget.config.processor.TagException;
import org.xidget.swing.ISwingWidgetFeature;
import org.xidget.swing.config.SwingXidgetKit;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An XAction which loads the xidget configuration specified by an xpath.
 */
public class XidgetAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    sourceExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected void doAction( IContext context)
  {
    IModelObject root = sourceExpr.queryFirst( context);
    
    try
    {
      Configuration configuration = new Configuration( new SwingXidgetKit());
      List<IXidget> xidgets = configuration.parse( root);
      
      // place all the xidgets in a container
      JPanel rootPanel = new JPanel();
      rootPanel.setBackground( Color.blue);
      rootPanel.setLayout( new GridLayout());
      for( IXidget xidget: xidgets)
      {
        xidget.setContext( (StatefulContext)context);
        xidget.bind();

        // get top-level widget and add to root panel
        ISwingWidgetFeature swingWidgetFeature = xidget.getFeature( ISwingWidgetFeature.class);
        if ( swingWidgetFeature != null)
        {
          JComponent widget = swingWidgetFeature.getWidget();
          rootPanel.add( widget);
        }
      }
      
      // place the root panel in a jframe
      JFrame frame = new JFrame();
      frame.getContentPane().add( rootPanel);
      frame.pack();
      frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
      frame.setVisible( true);
    }
    catch( TagException e)
    {
      throw new XActionException( e);
    }
  }
  
  private IExpression sourceExpr;
}
