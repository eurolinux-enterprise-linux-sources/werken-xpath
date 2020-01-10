
package com.werken.xpath;

import com.werken.xpath.parser.XPathLexer;
import com.werken.xpath.parser.XPathRecognizer;

import com.werken.xpath.impl.Context;
import com.werken.xpath.impl.Expr;

import org.jdom.Document;
import org.jdom.Element;

import antlr.CharBuffer;
import antlr.*;

import java.io.StringReader;

import java.util.List;
import java.util.ArrayList;

/** <p>Main run-time interface into the XPath functionality</p>
 * 
 *  <p>The XPath object embodies a textual XPath as described by
 *  the W3C XPath specification.  It can be applied against a
 *  context node (or nodeset) along with context-helpers to
 *  produce the result of walking the XPath.</p>
 *
 *  <p>Example usage:</p>
 *
 *  <pre>
 *  <code>
 *
 *      // Create a new XPath
 *      XPath xpath = new XPath("a/b/c/../d/.[@name="foo"]);
 *
 *      // Create the ContextSupport
 *      ContextSupport helper = new ContextSupport();
 *
 *      // Use the XPathFunctionContext instance as the implement
 *      // for function resolution.
 *      helper.setFunctionContext( XPathFunctionContext.getInstance() );
 *
 *      // Apply the XPath to your root context.
 *      Object results = xpath.applyTo(helper, myContext);
 *
 *  </code>
 *  </pre>
 *
 *
 *  @see com.werken.xpath.ContextSupport
 *  @see com.werken.xpath.NamespaceContext
 *  @see com.werken.xpath.VariableContext
 *  @see com.werken.xpath.FunctionContext
 *  @see com.werken.xpath.XPathFunctionContext
 *
 *  @author bob mcwhirter (bob @ werken.com)
 */
public class XPath
{

    private String      _xpath = "";
    private Expr        _expr  = null;

    /** Construct an XPath
     */
    public XPath(String xpath) {
        _xpath = xpath;
        parse();
    }

    public String toString()
    {
        return "[XPath: " + _xpath + " " + _expr + "]";
    }

    /** Retrieve the textual XPath string used to initialize this Object
     *
     *  @return The XPath string
     */
    public String getString()
    {
        return _xpath;
    }

    private void parse() {

        StringReader reader   = new StringReader(_xpath);
        InputBuffer  buf      = new CharBuffer(reader);

        XPathLexer      lexer = new XPathLexer(buf);
        XPathRecognizer recog = new XPathRecognizer(lexer);

        try
        {
            _expr = recog.xpath();
        }
        catch (RecognitionException e)
        {
            e.printStackTrace();
        }
        catch (TokenStreamException e)
        {
            e.printStackTrace();
        }
    }


    public List applyTo(Document doc)
    {
        return applyTo( ContextSupport.BASIC_CONTEXT_SUPPORT,
                        doc );
    }
    
    public List applyTo(List nodes)
    {
        return applyTo( ContextSupport.BASIC_CONTEXT_SUPPORT,
                        nodes );
    }
    
    public List applyTo(Element node)
    {
        return applyTo( ContextSupport.BASIC_CONTEXT_SUPPORT,
                        node );
    }

    public List applyTo(ContextSupport contextSupport,
                          Document doc)
    {
        return (List) _expr.evaluate( new Context( doc,
                                                   contextSupport ) );
    }
  
    /** Apply this XPath to a list of nodes
     *
     * @param contextSupport Walk-assisting state
     * @param nodes Root NodeSet context
     */
    public List applyTo(ContextSupport contextSupport,
                          List nodes)
    {
        return (List) _expr.evaluate( new Context( nodes,
                                                   contextSupport ) );
    }
  
    /** Apply this XPath to a single root node
     *
     * @param contextSupport Walk-assisting state
     * @param node The root context node
     */
    public List applyTo(ContextSupport contextSupport,
                          Element node)
    {
        return (List) _expr.evaluate( new Context( node,
                                                   contextSupport ) );
    }

}
