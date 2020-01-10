
package com.werken.xpath.impl;

import com.werken.xpath.ContextSupport;
import com.werken.xpath.util.Partition;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Attribute;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public abstract class UnAbbrStep extends Step
{
  private String _axis       = null;
  private List   _predicates = null;

  public UnAbbrStep(String axis)
  {
    _axis = axis;
  }

  public String getAxis()
  {
    return _axis;
  }

  public Step addPredicate(Predicate pred)
  {
    if ( _predicates == null )
    {
      _predicates = new ArrayList();
    }

    _predicates.add(pred);

    return this;
  }

  public List getPredicates()
  {
    if ( _predicates == null )
    {
      return Collections.EMPTY_LIST;
    }

    return _predicates;
  }


  public Context applyTo(Context context)
  {
    if ( context.isEmpty() )
    {
      return context;
    }

    List results =  applyTo(context.getNodeSet(),
                            context.getContextSupport(),
                            getAxis(),
                            true);

    context.setNodeSet(results);

    return context;
  }

  public List applyTo(List nodeSet,
                      ContextSupport support,
                      String axis)
  {
    return applyTo(nodeSet,
                   support,
                   axis,
                   false);
  }
  
  public List applyTo(List nodeSet,
                      ContextSupport support,
                      String axis,
                      boolean doPreds)
  {


    List aggregateResults = new ArrayList();
    List results          = null;

    Iterator nodeIter = nodeSet.iterator();
    Object   each     = null;

    while ( nodeIter.hasNext() )
    {

      each = nodeIter.next();

      if ( "self".equals(axis) )
      {
        results = applyToSelf( each,
                               support );
      }
      else if ( "ancestor".equals(axis) )
      {
        results = applyToAncestor( each,
                                   support );
      }
      else if ( "ancestor-or-self".equals(axis) )
      {
        results = applyToAncestorOrSelf( each,
                                         support );
      }
      else if ( "attribute".equals(axis) )
      {
        results = applyToAttribute( each,
                                    support );
      }
      else if ( "child".equals(axis) )
      {
        results = applyToChild( each,
                                support );
      }
      else if ( "descendant".equals(axis) )
      {
        results = applyToDescendant( each,
                                     support );
      }
      else if ( "descendant-or-self".equals(axis) )
      {
        results = applyToDescendantOrSelf( each,
                                           support );
      }
      else if ( "following".equals(axis) )
      {
        results = applyToFollowing( each,
                                    support );
      }
      else if ( "following-sibling".equals(axis) )
      {
        results = applyToFollowingSibling( each,
                                           support );
      }
      else if ( "namespace".equals(axis) )
      {
        // FIXME
      }
      else if ( "parent".equals(axis) )
      {
        results = applyToParent( each,
                                 support );
      }
      else if ( "preceeding".equals(axis) )
      {
        results = applyToPreceeding( each,
                                     support );
      }
      else if ( "preceeding-sibling".equals(axis) )
      {
        results = applyToPreceedingSibling( each,
                                            support );
      }
      else
      {
        results = Collections.EMPTY_LIST;
      }

      if ( doPreds ) {
        aggregateResults.addAll( applyPredicates( results,
                                                  support) );
      }
      else
      {
        aggregateResults.addAll( results );
      }
    }

    return aggregateResults;;
  }

  private List applyPredicates(List nodeSet,
                               ContextSupport support)
  {

    List results = nodeSet;

    if ( _predicates == null )
    {
      return nodeSet;
    }

    Iterator predIter = _predicates.iterator();

    Predicate eachPred = null;

    while ( predIter.hasNext() )
    {
      eachPred = (Predicate) predIter.next();

      results = eachPred.evaluateOn(results,
                                    support, 
                                    _axis );
    }

    return results;
  }

  public List applyToSelf(Object node,
                          ContextSupport support)
  {
    return Collections.EMPTY_LIST;
  }

  public List applyToChild(Object node,
                           ContextSupport support)
  {
    return Collections.EMPTY_LIST;
  }

  public List applyToDescendant(Object node,
                                ContextSupport support)
  {
    List results = new ArrayList();

    results.addAll( applyToChild( node,
                                  support ) );

    if ( node instanceof Element )
    {
      List children = ((Element)node).getMixedContent();
      
      results.addAll( applyTo( children,
                               support,
                               "descendant" ) );
    }
    else if ( node instanceof Document )
    {
      List children = ((Document)node).getMixedContent();
      
      results.addAll( applyTo( children,
                               support,
                               "descendant" ) );
    }

    return results;
  }

  public List applyToDescendantOrSelf(Object node,
                                      ContextSupport support)
  {

    List results = new ArrayList();

    results.addAll( applyToSelf( node,
                                 support ) );

    if ( node instanceof Element )
    {
      List children = ((Element)node).getMixedContent();
      
      results.addAll( applyTo( children,
                               support,
                               "descendant-or-self" ) );
    }
    else if ( node instanceof Document )
    {
      List children = ((Document)node).getMixedContent();

      results.addAll( applyTo( children,
                               support,
                               "descendant-or-self" ) );
       
    }
    
    return results;
  }

  public List applyToParent(Object node, 
                            ContextSupport support)
  {
    Object parent = ParentStep.findParent(node);

    List results = new ArrayList();

    results.addAll( applyToSelf( parent,
                                 support ) );

    return results;
  }

  public List applyToAncestor(Object node,
                              ContextSupport support)
  {
    List results = new ArrayList();

    results.addAll( applyToParent( node,
                                   support ) );

    Object parent = ParentStep.findParent( node );

    if ( parent != null )
    {
      results.addAll( applyToAncestor( parent,
                                       support ) );
    }

    return results;
  }

  public List applyToAncestorOrSelf(Object node,
                                    ContextSupport support)
  {
    List results = new ArrayList();

    results.addAll( applyToSelf( node,
                                 support ) );

    results.addAll( applyToAncestor( node,
                                     support ) );

    return results;
  }

  public List applyToAttribute(Object node,
                               ContextSupport support)
  {
    return Collections.EMPTY_LIST;
  }

  public List applyToPreceeding(Object node,
                                ContextSupport support)
  {
    List results = new ArrayList();

    if ( node instanceof Element)
    {
      List preceeding = Partition.preceeding( (Element)node );
      
      results.addAll( applyTo( preceeding,
                               support,
                               "self" ) );
    }

    return results;
  }
  
  public List applyToFollowing(Object node,
                               ContextSupport support)
  {
    List results = new ArrayList();

    if ( node instanceof Element) {
      
      List following = Partition.following( (Element)node );
      
      results.addAll( applyTo( following,
                               support,
                               "self" ) );
    }
    
    return results;
  }

  public List applyToPreceedingSibling(Object node,
                                       ContextSupport support)
  {

    List results = new ArrayList();

    if ( node instanceof Element) {
      
      List preceedingSiblings = Partition.preceedingSiblings( (Element)node );
      
      results.addAll( applyTo( preceedingSiblings,
                               support,
                               "self") );
    }

    return results;
  }

  public List applyToFollowingSibling(Object node,
                                      ContextSupport support)
  {
    List results = new ArrayList();

    if ( node instanceof Element) {
      
      List followingSiblings = Partition.followingSiblings( (Element)node );
      
      results.addAll( applyTo( followingSiblings,
                               support,
                               "self" ) );
    }
    
    return results;
  }
}
