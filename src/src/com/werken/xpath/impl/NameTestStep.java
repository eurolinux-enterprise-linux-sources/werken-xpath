
package com.werken.xpath.impl;

import com.werken.xpath.ContextSupport;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.Namespace;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class NameTestStep extends UnAbbrStep
{

  private String _namespacePrefix = null;
  private String _localName       = null;
  
  public NameTestStep(String axis,
                      String namespacePrefix,
                      String localName)
  {
    super(axis);
    _namespacePrefix = namespacePrefix;
    _localName       = localName;
  }

  public List applyToSelf(Object node,
                          ContextSupport support)
  {
    List results  = new ArrayList();

    if ( node instanceof Element )
    {

        if ( ( "*".equals( _localName ) || ((Element)node).getName().equals(_localName) )
             &&
             ((Element)node).getNamespaceURI().equals( support.translateNamespacePrefix( _namespacePrefix ) ) )
        {
          results.add( node );
        }
      }
      else if ( node instanceof Attribute )
      {
        if ( ((Attribute)node).getName().equals(_localName)
             &&
             ((Attribute)node).getNamespaceURI().equals( support.translateNamespacePrefix( _namespacePrefix ) ) )
        {
          results.add( node );
        }
      }
    
    return results;
  }
  
  public List applyToAttribute(Object node,
                             ContextSupport support)
    
  {
    List      results = new ArrayList();
    Attribute attr    = null;
    Namespace ns      = null;
    String    nsURI   = null;
    
    if ( node instanceof Element )
    {
      if ( "*".equals( _localName ) )
      {
        results.addAll( ((Element)node).getAttributes() );
      }
      else
      {
        attr = null;
        
        if ( "".equals( _namespacePrefix ) )
        {
          attr = ((Element)node).getAttribute( _localName );
        }
        else
        {
          nsURI = support.translateNamespacePrefix( _namespacePrefix );
          
          ns = Namespace.getNamespace(nsURI);
          
          attr = ((Element)node).getAttribute( _localName,
                                               ns );
          
        }
        
        if ( attr != null )
        {
          results.add( attr );
        }
      }
    }
    
    return results;
  }
  
  public List applyToChild(Object node,
                           ContextSupport support)
  {
    List results = new ArrayList();
    String nsURI = null;
    Namespace ns = null;

    if ( _namespacePrefix != null )
    {
      nsURI = support.translateNamespacePrefix( _namespacePrefix );
    }

    if ( node instanceof Document )
    {
      Element child = ((Document)node).getRootElement();
      
      if ( child.getName().equals( _localName ) )
      {
        if ( nsURI == null )
        {
          results.add( child );
        }
        else if ( nsURI.equals( child.getNamespaceURI() ) )
        {
          results.add( child );
        }
      }
    }
    else if ( node instanceof Element )
    {
      if ( "*".equals( _localName ) )
      {
        List children = ((Element)node).getChildren();
        
        if ( nsURI == null )
        {
          results.addAll( children );
        }
        else
        {
          Iterator childIter = children.iterator();
          Element  nodeChild = null;
          
          while ( childIter.hasNext() )
          {
            nodeChild = (Element) childIter.next();
            
            if ( nsURI.equals( nodeChild.getNamespaceURI() ) )
            {
              results.add( nodeChild );
            }
          }
        }
      }
      else
      {
        ns = Namespace.getNamespace( nsURI );
        
        results.addAll( ((Element)node).getChildren( _localName,
                                                     ns ) );
      }
    }

    return results;
  }

  public String toString()
  {
    return "[NameTestStep [" + _namespacePrefix + ":" + _localName + "]]";
  }
}
