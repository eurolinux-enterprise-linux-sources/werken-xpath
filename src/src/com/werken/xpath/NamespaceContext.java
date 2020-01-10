
package com.werken.xpath;

/** <p>Specification of the interface required by
 *  {@link com.werken.xpath.ContextSupport} for delegation
 *  of namespace prefix binding resolution.<p>
 *
 *  @autho bob mcwhirter (bob @ werken.com)
 */
public interface NamespaceContext
{

  /** Translate a namespace prefix into a URI
   *
   *  Translate the prefix used in a component of an XPath
   *  into its expanded namespace URI.</p>
   *
   *  @param prefix The namespace prefix
   *
   *  @return The URI matching the prefix
   *
   *  @see com.werken.xpath.ContextSupport#setNamespaceContext
   */
  String translateNamespacePrefix(String prefix);
}
