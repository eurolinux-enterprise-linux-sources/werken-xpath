
package com.werken.xpath;

import com.werken.xpath.function.Function;

/** <p>Specification of the interface required by
 *  {@link com.werken.xpath.ContextSupport} for delegation
 *  of function resolution.</p>
 *
 *  @author bob mcwhirter (bob @ werken.com)
 */
public interface FunctionContext
{

  /** Retrieve a named function
   *
   *  <p>Retrieve the named function object, or null
   *  if no such function exists.  
   *  
   *  @param name The name of the function sought.
   *
   *  @return The {@link com.werken.xpath.function.Function}
   *          matching the specified name.
   *
   *  @see com.werken.xpath.ContextSupport#setFunctionContext
   */
  Function getFunction(String name);
}
