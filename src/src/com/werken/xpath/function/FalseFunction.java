
package com.werken.xpath.function;

import com.werken.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.3</b> <code><i>boolean</i> false()</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/
public class FalseFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if (args.size() == 0)
    {
      return evaluate();
    }

    // FIXME: Toss exception
    return null;
  }

  public static Boolean evaluate()
  {
    return Boolean.FALSE;
  }
}

