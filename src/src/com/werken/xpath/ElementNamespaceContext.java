
package com.werken.xpath;

import org.jdom.Element;
import org.jdom.Namespace;

import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import java.util.List;
import java.util.Iterator;

/** <p>A {@link com.werken.xpath.NamespaceContext} which gets it's mappings
 *  from an Element in a JDOM tree.</p>
 *
 *  <p><b>It currently DOES NOT WORK</b></p>
 * 
 *  @author bob mcwhirter (bob @ werken.com)
 */
public class ElementNamespaceContext implements NamespaceContext
{
  private Element _element          = null;
  private Map     _namespaceMapping = null;

  /** Construct the NamespaceContext from a JDOM Element
   *
   *  @param element The JDOM element to use for prefix->nsURI mapping
   */
  public ElementNamespaceContext(Element element)
  {
    _element = element;
  }

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
  public String translateNamespacePrefix(String prefix)
  {
    // Initialize the prefix->URI mapping upon the first
    // call to this method.  Traverse from the <<root>>
    // to the current Element, accumulating namespace
    // declarations.

    if ( prefix == null || "".equals( prefix ) ) 
    {
      return "";
    }

    if ( _namespaceMapping == null )
    {
      _namespaceMapping = new HashMap();

      Stack lineage = new Stack();

      lineage.push(_element);

      Element elem = _element.getParent();

      while (elem != null)
      {
        lineage.push(elem);
        elem = elem.getParent();
      }

      List      nsList = null;
      Iterator  nsIter = null;
      Namespace eachNS = null;

      while ( ! lineage.isEmpty() )
      {
        elem = (Element) lineage.pop();

        nsList = elem.getAdditionalNamespaces();

        if ( ! nsList.isEmpty() )
        {
          nsIter = nsList.iterator();

          while (nsIter.hasNext())
          {
            eachNS = (Namespace) nsIter.next();

            _namespaceMapping.put( eachNS.getPrefix(),
                                   eachNS.getURI() );
          }
        }
      }
    }

    return (String) _namespaceMapping.get( prefix );

  }
}
