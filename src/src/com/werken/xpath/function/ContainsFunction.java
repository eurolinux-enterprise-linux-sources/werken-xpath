
package com.werken.xpath.function;

import com.werken.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.2</b> <code><i>boolean</i> contains(<i>string</i>,<i>string</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/

public class ContainsFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if (args.size() == 2)
    {
      return evaluate(args.get(0), args.get(1));
    }

    // FIXME: Toss exception
    return null;
  }

  public static Boolean evaluate(Object strArg,
                                 Object matchArg)
  {
    String str = StringFunction.evaluate(strArg);
    String match = StringFunction.evaluate(matchArg);

    return ( ( str.indexOf(match) >= 0)
             ? Boolean.TRUE
             : Boolean.FALSE
             );
  }
}
