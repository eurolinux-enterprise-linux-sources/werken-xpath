
package com.werken.xpath.function;

import com.werken.xpath.impl.Context;

import org.jdom.Attribute;
import org.jdom.Element;

import java.util.List;

/**
   <p><b>4.1</b> <code><i>string</i> namespace-uri(<i>node-set?</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/
public class NamespaceUriFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if (args.size() == 0)
    {
      return evaluate( context );
    }

    // FIXME: Toss exception
    return null;
  }

  public static String evaluate(Context context)
  {
    List list = context.getNodeSet();
    
    if ( ! list.isEmpty() )
    {
      Object first = list.get(0);
      
      if (first instanceof Element)
      {
        return ((Element)first).getNamespaceURI();
      }
      else if (first instanceof Attribute)
      {
        return ((Attribute)first).getNamespaceURI();
      }
    }

    return "";
  }
}
