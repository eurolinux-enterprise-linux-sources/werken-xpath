
package com.werken.xpath.function;

import com.werken.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.4</b> <code><i>number</i> round(<i>number</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/
public class RoundFunction implements Function
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

    if (num.isNaN())
    {
      return num;
    }

    int rounded = (int) (num.doubleValue() + 0.5);

    // FIXME take care of that wacky -0 rounding thing
    // for values between -0.5 and 0.

    return new Double(rounded);
  }
}

