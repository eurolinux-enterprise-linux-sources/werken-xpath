
package com.werken.xpath.function;

import com.werken.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.4</b> <code><i>number</i> ceiling(<i>number</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/
public class CeilingFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if (args.size() == 1)
    {
      return evaluate(args.get(1));
    }

    // FIXME: Toss exception
    return null;
  }

  public static Double evaluate(Object obj)
  {
    Double num = NumberFunction.evaluate(obj);
    int possibly = num.intValue();

    if ( possibly < num.doubleValue())
    {
      return new Double(possibly + 1);
    }
    return new Double( possibly);
  }
}

