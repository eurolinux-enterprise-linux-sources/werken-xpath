
package com.werken.xpath.impl;

import org.jdom.Element;
import org.jdom.Attribute;

import java.util.List;

class Operator
{

  static Object evaluate(Context context,
                         Op op,
                         Object lhsValue,
                         Object rhsValue)
  {
    Object result = null;

    if ( op == Op.OR )
    {

    }
    else if ( op == Op.AND )
    {

    }
    else
    {
      // This cascading-if implments section 3.4 ("Booleans") of
      // the XPath-REC spec, for all operators which do not perform
      // short-circuit evaluation (meaning everything except AND and OR);

      if (Operator.bothAreNodeSets(lhsValue,
                                   rhsValue))
      {
        // result = opNodeSetNodeSet();
        result = OpNodeSetNodeSet.evaluate(context,
                                           op,
                                           lhsValue,
                                           rhsValue);
      }
      else if (Operator.eitherIsNodeSet(lhsValue,
                                        rhsValue))
      {
        result = OpNodeSetAny.evaluate(context,
                                       op,
                                       lhsValue,
                                       rhsValue);
      }
      else if (Operator.eitherIsBoolean(lhsValue,
                                        rhsValue))
      {
        result = OpBooleanAny.evaluate(context,
                                       op,
                                       lhsValue,
                                       rhsValue);
      }
      else if (Operator.eitherIsNumber(lhsValue,
                                       rhsValue))
      {
        result = OpNumberAny.evaluate(context,
                                      op,
                                      lhsValue,
                                      rhsValue);
      }
      else if (Operator.eitherIsString(lhsValue,
                                       rhsValue))
      {
        result = OpStringAny.evaluate(context,
                                      op,
                                      lhsValue,
                                      rhsValue);
      }
    }

    //return Collections.EMPTY_LIST;
    return result;
  }
                                
  protected static boolean eitherIsNodeSet(Object lhs,
                                           Object rhs)
  {
    return ( (lhs instanceof List) || (rhs instanceof List) );
  }
  protected static boolean bothAreNodeSets(Object lhs,
                                           Object rhs)
  {
    return ( (lhs instanceof List) && (rhs instanceof List) );
  }
  
  protected static boolean eitherIsNumber(Object lhs,
                                          Object rhs)
  {
    return ( (lhs instanceof Number) || (rhs instanceof Number) );
  }
  
  protected static boolean eitherIsString(Object lhs,
                                          Object rhs )
  {
    return ( (lhs instanceof String) || (rhs instanceof String) );
  }
  
  protected static boolean eitherIsBoolean(Object lhs,
                                           Object rhs)
  {
    return ( (lhs instanceof Boolean) || (rhs instanceof Boolean) );
  }

  protected static String convertToString(Object obj)
  {
    String result = null;

    if (obj instanceof String)
    {
      result = (String) obj;
    }
    else if (obj instanceof Attribute)
    {
      result = ((Attribute)obj).getValue();
    }
    else if (obj instanceof Element)
    {
      result = ((Element)obj).getText();
    }
    else
    {
      result = obj.toString();
    }

    return result;
  }

  protected static Double convertToNumber(Object obj)
  {
    Double result = null;

    if (obj instanceof Double)
    {
      result = (Double) obj;
    }
    else
    {
      result = Double.valueOf( convertToString(obj) );
    }

    return result;
  }

  protected static Boolean convertToBoolean(Object obj)
  {
    Boolean result = null;

    if (obj instanceof Boolean)
    {
      result = (Boolean) obj;
    }
    else
    {
      result = Boolean.valueOf( convertToString(obj) );
    }

    return result;
  }

}
