
package com.werken.xpath.impl;

import com.werken.xpath.ContextSupport;

import com.werken.xpath.function.BooleanFunction;

import org.jdom.Element;
import org.jdom.Attribute;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class BinaryExpr extends Expr
{

  private Op    _op   = null;
  private Expr  _lhs  = null;
  private Expr  _rhs  = null;

  public BinaryExpr(Op op,
                    Expr lhs, Expr rhs)
  {
    _op  = op;
    _lhs = lhs;
    _rhs = rhs;
  }

  public Object evaluate(Context context)
  {

    //System.err.println( _op + " " + _lhs + " " + _rhs );

    Context duplicateContext = context.duplicate();

    Object result = null;
    
    Object lhsValue = _lhs.evaluate( context );

    Object rhsValue = null;


    // Short-circuit the boolean AND and OR operators
    // returning early if LHS is all that is needed
    // to determine truthfulness. 

    if ( _op == Op.OR )
    {

      if (BooleanFunction.evaluate(lhsValue).booleanValue())
      {
        return Boolean.TRUE;
      }

      rhsValue = _rhs.evaluate( duplicateContext );

      if (BooleanFunction.evaluate(rhsValue).booleanValue())
      {
        return Boolean.TRUE;
      }

      return Boolean.FALSE;

    }
    else if ( _op == Op.AND )
    {

      if ( ! BooleanFunction.evaluate(lhsValue).booleanValue())
      {
        return Boolean.FALSE;
      }

      rhsValue = _rhs.evaluate( duplicateContext );

      if ( ! BooleanFunction.evaluate(rhsValue).booleanValue())
      {
        return Boolean.FALSE;
      }

      return Boolean.TRUE;

    }
    else
    {
      rhsValue = _rhs.evaluate( duplicateContext );

      result = Operator.evaluate(duplicateContext,
                                 _op,
                                 lhsValue,
                                 rhsValue);
    }

    return result;
  }
}
