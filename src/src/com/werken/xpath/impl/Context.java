
package com.werken.xpath.impl;

import com.werken.xpath.ContextSupport;

import com.werken.xpath.function.Function;

import org.jdom.Document;
import org.jdom.Element;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class Context implements Cloneable
{
  private ContextSupport _contextSupport = null;
  private List           _nodeSet        = null;
  private int            _position       = 0;

  public Context(Document doc,
                 ContextSupport contextSupport)
  {
    _nodeSet = new ArrayList(1);
    _nodeSet.add(doc);

    _contextSupport = contextSupport;
  }

  public Context(Element elem,
                 ContextSupport contextSupport)
  {
    _nodeSet = new ArrayList(1);
    _nodeSet.add(elem);

    _contextSupport = contextSupport;
  }

  public Context(List nodeSet,
                 ContextSupport contextSupport)
  {
    _nodeSet = nodeSet;
    
    _contextSupport = contextSupport;
  }

  public List getNodeSet()
  {
    if ( _position == 0 )
    {
      return _nodeSet;
    }

    List nodeOnly = new ArrayList(1);

    nodeOnly.add( getContextNode() );

    return nodeOnly;
  }

  public int getSize()
  {
    return _nodeSet.size();
  }

  public boolean isEmpty()
  {
    return _nodeSet.isEmpty();
  }

  public int getPosition()
  {
    return _position;
  }

  public void setPosition(int position)
  {
    _position = position;
  }

  public Object getContextNode()
  {
    return getNode( _position );
  }

  public Object getNode(int index)
  {
    return _nodeSet.get( index - 1 );
  }

  public void setNodeSet(List nodeSet)
  {
    if ( nodeSet.isEmpty() )
    {
      _nodeSet = Collections.EMPTY_LIST;
    }
    else
    {
      _nodeSet = nodeSet;
    }
    _position = 0;
  }

  public ContextSupport getContextSupport()
  {
    return _contextSupport;
  }

  public Iterator iterator()
  {
    return _nodeSet.iterator();
  }

  public String translateNamespacePrefix(String prefix)
  {
    return _contextSupport.translateNamespacePrefix( prefix );
  }

  public Object getVariableValue(String variableName)
  {
    return _contextSupport.getVariableValue( variableName );
  }

  public Function getFunction(String name)
  {
    return _contextSupport.getFunction( name );
  }

  public Context duplicate()
  {
    Context dupe = null;

    try
    {
      dupe = (Context) this.clone();

      dupe._nodeSet = (ArrayList) ((ArrayList)this._nodeSet).clone();
    }
    catch (CloneNotSupportedException e)
    {
      dupe = null;
    }

    return dupe;
  }

  public String toString()
  {
    return ("[Context " + _nodeSet + "]");
  }
}
