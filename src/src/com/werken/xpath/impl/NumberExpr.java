
package com.werken.xpath.impl;

import org.jdom.Element;

public class NumberExpr extends Expr
{

  private Double _number = null;

  public NumberExpr(String number)
  {
    _number = new Double( number );
  }

  public String toString()
  {
    return ("[(NumberExpr) " + _number + "]");
  }

  public Object evaluate(Context context)
  {
    return _number;
  }

}
