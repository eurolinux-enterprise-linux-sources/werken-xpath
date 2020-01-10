
package com.werken.xpath.impl;

import com.werken.xpath.ContextSupport;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Comment;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class NodeTypeStep extends UnAbbrStep
{
  private String _nodeType = null;
  
  public NodeTypeStep(String axis,
                      String nodeType)
  {
    super(axis);
    _nodeType = nodeType;
  }

  protected boolean matches(Object node)
  {

    List results = null;

    if ( "node".equals( _nodeType ) )
    {
      return true;
    }

    results = new ArrayList();

    if ( "text".equals( _nodeType ) && ( node instanceof String ) )
    {
      return true;
    }
    else if ( "comment".equals( _nodeType ) && ( node instanceof Comment ) )
    {
      return true;
    }

    return false;
  }

  public List applyToNode(Object node)
  {
    List nodeSet = new ArrayList(1);
    nodeSet.add( node );

    return applyToNodes( nodeSet );
  }

  public List applyToNodes(List nodeSet)
  {
    List results = new ArrayList();

    Iterator nodeIter = nodeSet.iterator();
    Object   each     = null;

    while ( nodeIter.hasNext() )
    {
      each = nodeIter.next();
      if ( matches( each ) )
      {
        results.add( each );
      }
    }

    return results;
        
  }

  public List applyToChild(Object node,
                           ContextSupport support)
  {
    List results = new ArrayList();

    if ( node instanceof Element )
    {
      if ( isAbsolute() )
      {
        results.addAll(  applyToNodes( ((Element)node).getDocument().getMixedContent() ) );
      }
      else
      {
        results.addAll( applyToNodes( ((Element)node).getMixedContent() ) );
      }
    }
    else if ( node instanceof Document )
    {
      results.addAll( applyToNodes( ((Document)node).getMixedContent() ) );
    }

    return results;
  }

  public List applyToSelf(Object node,
                          ContextSupport support)
  {
    return applyToNode( node );
  }

  public String toString()
  {
    return "[NodeTypeStep [" + _nodeType + "]]";
  }
}
