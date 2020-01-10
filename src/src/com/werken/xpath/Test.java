
package com.werken.xpath;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.util.List;
import java.util.Iterator;

import java.io.File;
import java.io.IOException;

/** Example Test driver for werken.xpath
 *
 *  @author bob mcwhirter (bob @ werken.com)
 */
public class Test
{
  public static void main(String[] args)
  {

    System.out.println("werken xpath -- test driver\n");
    
    if ( args.length != 2 ) {
      System.err.println("Usage: java ...TestDriver <input file> <xpath_expr>");
      System.exit(1);
    }
    
    SAXBuilder builder = new SAXBuilder();
    
    File inFile = new File(args[0]);
    String xpathExpr = args[1];
    
    try
    {
      Document doc = builder.build(inFile);

      XPath xpath = new XPath(xpathExpr);
      System.err.println("XPath :: " + xpath.getString());

      ContextSupport contextSupport = new ContextSupport();
      DefaultVariableContext vc = new DefaultVariableContext();
      vc.setVariableValue("foo", "cheese burgers");

      System.err.println("Context :: " + doc);

      NamespaceContext nc = new ElementNamespaceContext( doc.getRootElement() );

      contextSupport.setFunctionContext( XPathFunctionContext.getInstance() );
      contextSupport.setVariableContext( vc );
      contextSupport.setNamespaceContext( nc );

      Object results = xpath.applyTo(contextSupport,
                                     doc);

      System.err.println("Results :: " + results);

    }
    catch (JDOMException jde)
    {
      jde.printStackTrace(System.err);
    }

  }

}
