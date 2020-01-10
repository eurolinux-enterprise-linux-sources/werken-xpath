
package com.werken.xpath.function;

import com.werken.xpath.impl.Context;

import org.jdom.Attribute;
import org.jdom.Element;
import java.util.List;
import java.util.Iterator;

/**
   <p><b>4.2</b> <code><i>string</i> string(<i>object</i>)</code> 

   @author bob mcwhirter (bob @ werken.com)
*/
public class StringFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if ( args.size() == 0 )
    {
      return evaluate( context );
    }
    if ( args.size() == 1 )
    {
      return evaluate( args.get(0) );
    }

    // FIXME: Toss an exception
    return null;
  }

  public static String evaluate(Object obj)
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
      result = evaluate( (Element)obj );
    }
    else if (obj instanceof List)
    {
      if ( ((List)obj).size() == 0 )
      {
        result = "";
      }
      else
      {
        result = evaluate( ((List)obj).get(0) );
      }
    }
    else
    {
      result = obj.toString();
    }

    // FIXME should handle List case!

    return result;
  }

  public static String evaluate(Element elem)
  {
    List content = elem.getMixedContent();

    Iterator contentIter = content.iterator();
    Object each = null;

    StringBuffer stringValue = new StringBuffer();

    while (contentIter.hasNext())
    {
      each = contentIter.next();

      if (each instanceof String)
      {
        stringValue.append( (String) each );
      }
      else if (each instanceof Element)
      {
        stringValue.append( StringFunction.evaluate( (Element)each ) );
      }
    }

    return stringValue.toString();
  }
}
