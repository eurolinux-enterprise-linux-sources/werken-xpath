
package com.werken.xpath.impl;

import com.werken.xpath.ContextSupport;
import com.werken.xpath.function.BooleanFunction;

import org.jdom.Element;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class Predicate
{
  private Expr _expr = null;

  public Predicate(Expr expr)
  {
    _expr = expr;
  }

  public List evaluateOn(List nodeSet,
                         ContextSupport support,
                         String axis)
  {
    Context context          = new Context( nodeSet,
                                            support );
    Context duplicateContext = null;
    
    List results = new ArrayList();

    int position = 1;
    int max = context.getSize();

    while ( position <= max )
    {
      duplicateContext = context.duplicate();
      duplicateContext.setPosition( position );

      if ( evaluateOnNode( duplicateContext,
                           axis ) )
      {
        results.add( context.getNode( position ) );
      }

      ++position;
    }

    return results;
  }

  public boolean evaluateOnNode(Context context,
                                String axis)
  {
    boolean result = false;

    Object exprResult = _expr.evaluate( context );

    //System.err.println("pred-expr == " + _expr);

    if ( exprResult instanceof Double )
    {
      if ( ((Double)exprResult).equals( new Double( context.getPosition() ) ) )
      {
        result = true;
      }
    } else {
      result = BooleanFunction.evaluate(exprResult).booleanValue();
    }

    return result;
  }

}
