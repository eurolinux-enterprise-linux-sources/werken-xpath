
package com.werken.xpath.test;

import com.werken.xpath.XPath;
import com.werken.xpath.ContextSupport;
import com.werken.xpath.XPathFunctionContext;
import com.werken.xpath.DefaultVariableContext;
import com.werken.xpath.ElementNamespaceContext;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

public class Driver
{
    public static void main(String[] args)
    {
        System.err.println("werken.xpath test-driver");

        if ( args.length != 1 )
        {
            System.err.println("Usage:");
            System.err.println("java com.werken.xpath.test.Driver <test-driver-filename>");
            System.exit(1);
        }

        File driverXML = new File( args[0] ); 

        if ( ! driverXML.exists() )
        {
            System.err.println("error: " + driverXML + " does not exist");
            System.exit(1);
        }

        System.out.println("Using test-cases in [" + args[0] + "]");

        File dataDir = driverXML.getParentFile();

        Driver driver = new Driver(dataDir,
                                   driverXML);

        driver.run();
    
    }

    private File _dataDirectory = null;
    private File _driverXML     = null;
    private Map  _testDocuments = new HashMap();
    private ContextSupport _support = null;

    private Vector _failed = new Vector();
    private int    _passed = 0;

    public Driver(File dataDirectory,
                  File driverXML)
    {
        _dataDirectory = dataDirectory;
        _driverXML     = driverXML;

        _support = new ContextSupport();
    }

    public void run()
    {
    
        SAXBuilder builder = new SAXBuilder();

        try
        {
            Document doc = builder.build( _driverXML );

            Element root = doc.getRootElement();

            List testCases = root.getChildren("testcase");

            Iterator testIter = testCases.iterator();

            while ( testIter.hasNext() )
            {
                doTestCase( (Element) testIter.next() );
            }
      
        }
        catch (JDOMException e)
        {
            e.printStackTrace();
        }

        System.out.println("================================================================================");
        System.out.println("TEST SUMMARY");
        System.out.println("  PASSED: " + _passed);
        System.out.println("  FAILED: " + _failed.size());
        if ( _failed.size() > 0 )
        {
            System.out.println("----------------------------------------------");
            System.out.println("FAILURES");
            System.out.println("----------------------------------------------");

            Iterator failedIter = _failed.iterator();
            Element eachFailure = null;

            while (failedIter.hasNext())
            {
                eachFailure = (Element) failedIter.next();

                System.out.println("  " + eachFailure.getAttributeValue("caseID"));
                System.out.println("  " + eachFailure.getChildTextTrim("description"));
                System.out.println("  " + eachFailure.getAttributeValue("xpath"));
                System.out.println("----------------------------------------------");

            }
        }
        System.out.println("================================================================================");
    }

    private Document getTestDocument(String identifier)
    {
        Document doc = (Document) _testDocuments.get(identifier);

        if (doc == null)
        {
            File testDoc = new File(_dataDirectory,
                                    identifier);

            if ( ! testDoc.exists() )
            {
                System.err.println("error: test document [" + identifier + "] does not exist");
                return null;
            }

            try
            {
                SAXBuilder builder = new SAXBuilder();
                doc = builder.build(testDoc);
            }
            catch (JDOMException e)
            {
                e.printStackTrace();
            }

            if ( doc != null )
            {
                _testDocuments.put(identifier,
                                   doc);
            }
           
        }

        return doc;
    }

    private void doTestCase(Element testCase)
    {
        String caseID = testCase.getAttributeValue("caseID");
        String desc = testCase.getChildTextTrim("description");
        String testDoc = testCase.getAttributeValue("document");
        String startNode = testCase.getAttributeValue("start");
        String xpathStr = testCase.getAttributeValue("xpath");
    
        System.out.println( "--------------------------------------------------------------------------------" );
        System.out.println( "      " + caseID + " :: " + desc);
        System.out.println( " document :: " + testDoc );
        System.out.println( "    start :: " + startNode );
        System.out.println( "    xpath :: " + xpathStr );

        boolean success = false;

        Document doc = getTestDocument( testDoc );
        Document resultDoc = null;
        Element expectedResults = testCase.getChild("results");

        if ( doc != null )
        {
            XPath xpath = new XPath(xpathStr);
            
            _support.setNamespaceContext( new ElementNamespaceContext( testCase ) );
            
            Object results = xpath.applyTo( _support, doc );

            resultDoc = xmlizeResults( results );

            expectedResults = testCase.getChild("results");

            success = compareResults( resultDoc.getRootElement(),
                                      expectedResults );


        }

        System.out.println( "  results :: " + ( success ? "PASSED" : "FAILED" ));

        if ( success )
        {
            ++_passed;
        }
        else
        {
            _failed.add(testCase);
        }
      

        if ( !success )
        { 

            if ( doc == null )
            {
                System.out.println("  !! unable to locate test document !!");
            }
            else
            {
                XMLOutputter outputter = new XMLOutputter("    ", true);
        
                try
                {
          
                    System.out.println( "selected  :: ");
                    outputter.output(resultDoc,
                                     System.out);

                    Element expectedElem = new Element("results");
                    Document expectedDoc = new Document( expectedElem );

                    Iterator expectedIter = expectedResults.getChildren().iterator();

                    while (expectedIter.hasNext())
                    {
                        expectedElem.addContent( (Element) ((Element)expectedIter.next()).clone() );
                    }

                    System.out.println( "expected  :: ");

                    outputter.output(expectedDoc,
                                     System.out);

                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }

    }

    private boolean compareResults(Element results,
                                   Element expected)
    {

        List expectedChildren = expected.getChildren();

        int expectedSize = expectedChildren.size();

        HashSet seen = new HashSet();

        if ( results.getChildren().size() != expectedSize )
        {
            return false;
        }

        Iterator expectedIter = expectedChildren.iterator();
        Element  eachExpected = null;
    
        while ( expectedIter.hasNext() )
        {
            eachExpected = (Element) expectedIter.next();

            if ( ! findInResults( results,
                                eachExpected,
                                seen ) )
            {
                return false;
            }
        }

        return true;

    }

    private boolean findInResults(Element results,
                                  Element expected,
                                  Set seen)
    {
        if (expected.getName().equals("element"))
        {
            return findElementInResults(results,
                                        expected,
                                        seen);
        }

        if (expected.getName().equals("root"))
        {
            return findRootInResults(results,
                                     seen);
        }

        return false;
    }

    private boolean findRootInResults(Element results,
                                      Set seen)
    {
        List rootElems = results.getChildren("root");

        Iterator rootIter = rootElems.iterator();
        Element  eachRoot = null;

        while (rootIter.hasNext())
        {
            eachRoot = (Element) rootIter.next();

            if ( ! seen.contains( eachRoot ) )
            {
                seen.add( eachRoot );
                return true;
            }
        }

        return false;
    }

    private boolean findElementInResults(Element results,
                                         Element expected,
                                         Set seen)
    {
        Element expectedElem = (Element) expected.getChildren().get(0);

        List resultElems = results.getChildren();

        Iterator resultIter = resultElems.iterator();

        Element eachResult = null;
        Element resultElem = null;
        List    resultList = null;

        while (resultIter.hasNext())
        {
            eachResult = (Element) resultIter.next();

            if ( ! seen.contains( eachResult ) )
            {
                resultList = eachResult.getChildren();

                if ( ! resultList.isEmpty())
                {
                    resultElem = (Element) resultList.get(0);
                    
                    if (resultElem.getName().equals( expectedElem.getName() ) )
                    {
                        if ( resultElem.getAttributeValue("id").equals( expectedElem.getAttributeValue("id")) )
                        {
                            seen.add(resultElem);
                            
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private Document xmlizeResults(Object results)
    {
        if (results instanceof List)
        {
            return xmlizeResults( (List) results );
        }

        return null;
    }

    private Document xmlizeResults(List nodeSet)
    {

        Element results = new Element("results");
        Document doc = new Document(results);

        Iterator nodeIter = nodeSet.iterator();
        Object   each     = null;

        Element  resultNode = null;
        Element  node = null;

        String elemID = null;

        while ( nodeIter.hasNext() )
        {
            each = nodeIter.next();

            if ( each instanceof Element )
            {
                node = new Element( "element" );
                resultNode = new Element( ((Element)each).getName() );
                elemID = ((Element)each).getAttributeValue("id");

                if (elemID == null)
                {
                    elemID = "";
                }

                resultNode.addAttribute("id", elemID);
                node.addContent( resultNode );
                results.addContent( node );
            }
            else if ( each instanceof Document )
            {
                resultNode = new Element( "root" );
                results.addContent( resultNode );
            }
        }

        return doc;
    }
}
