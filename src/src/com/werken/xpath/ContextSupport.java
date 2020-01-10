
package com.werken.xpath;

import com.werken.xpath.function.Function;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/** <p>ContextSupport maintains information to aid in the
 *  execution of the XPath against a context node.</p>
 * 
 *  <p>It separates the knowledge of functions, variables
 *  and namespace-bindings from the context node to
 *  be walked.</p>
 *
 *  @author bob mcwhirter (bob @ werken.com)
 */
public class ContextSupport
{
    static final ContextSupport     BASIC_CONTEXT_SUPPORT = new ContextSupport();

    private NamespaceContext  _nsContext        = null;
    private FunctionContext   _functionContext  = XPathFunctionContext.getInstance();
    private VariableContext   _variableContext  = null;

    /** Construct a semantically empty ContextSupport
     */
    public ContextSupport()
    {
        // intentionally left blank
    }

    /** Construct a semantically initialized ContextSupport
     *
     *  @param nsContext The NamespaceContext implementation
     *  @param functionContext The FunctionContext implementation
     *  @param variableContext The VariableContext implementation
     */
    public ContextSupport(NamespaceContext nsContext,
                          FunctionContext functionContext,
                          VariableContext variableContext)
    {
        _nsContext = nsContext;
        _functionContext = functionContext;
        _variableContext = variableContext;
    }

    /** Set the NamespaceContext implementation
     *
     *  @param nsContext The NamespaceContext implementation
     */
    public void setNamespaceContext(NamespaceContext nsContext)
    {
        _nsContext = nsContext;
    }

    /** Set the FunctionContext implementation
     *
     *  @param functionContext The FunctionContext implementation
     */
    public void setFunctionContext(FunctionContext functionContext)
    {
        _functionContext = functionContext;
    }

    /** Set the VariableContext implementation
     *
     *  @param variableContext The FunctionContext implementation
     */
    public void setVariableContext(VariableContext variableContext)
    {
        _variableContext = variableContext;
    }

    /** Translate a namespace prefix into a URI
     *
     *  <p>Using the {@link com.werken.xpath.NamespaceContext}
     *  implementation, translate the prefix used in a component of an XPath
     *  into its expanded namespace URI.</p>
     *
     *  @param prefix The namespace prefix
     *
     *  @return The URI matching the prefix
     *
     *  @see #setNamespaceContext
     */
    public String translateNamespacePrefix(String prefix)
    {
        if (_nsContext == null)
        {
            return null;
        }
        return _nsContext.translateNamespacePrefix(prefix);
    }

    /** Retrieve a named function
     *
     *  <p>Retrieve the named function object, or null
     *  if no such function exists.  Delegates to the
     *  {@link com.werken.xpath.FunctionContext} implementation
     *  provided, if any.
     *  
     *  @param name The name of the function sought.
     *
     *  @return The {@link com.werken.xpath.function.Function}
     *          matching the specified name.
     *
     *  @see #setFunctionContext
     */
    public Function getFunction(String name)
    {
        return _functionContext.getFunction(name);
    }

    /** Resolve a variable binding
     *
     *  <p>Retrieve the currently bound value of the named
     *  variable, or null if no such binding exists.  Delegates
     *  to the {@link com.werken.xpath.VariableContext} implementation
     *  provided, if any.
     *
     *  @param name The name of the variable sought.
     *
     *  @return The currently bound value of the variable, or null.
     *
     *  @see #setVariableContext
     */
    public Object getVariableValue(String name)
    {
        if ( _variableContext == null )
        {
            return null;
        }

        return _variableContext.getVariableValue(name);
    }
}
