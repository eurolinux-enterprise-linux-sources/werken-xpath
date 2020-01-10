
package com.werken.xpath.impl;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Attribute;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class LocationPath extends PathExpr
{
  private boolean _isAbsolute  = false;
  private List    _steps       = null;

  public LocationPath()
  {

  }

  public void setIsAbsolute(boolean isAbsolute)
  {
    _isAbsolute = isAbsolute;
  }

  public boolean isAbsolute()
  {
    return _isAbsolute;
  }

  public LocationPath addStep(Step step)
  {
    if ( _steps == null )
    {
      _steps = new ArrayList();
    }

    _steps.add(step);

    return this;
  }

  public List getSteps()
  {
    if ( _steps == null )
    {
      return Collections.EMPTY_LIST;
    }

    return _steps;
  }

  public Object evaluate(Context context)
  {
    return applyTo(context);
  }

  public Object applyTo(Context context)
  {
    
    if ( getSteps().isEmpty() )
    {
      if ( isAbsolute() )
      {
        Iterator nodeIter = context.getNodeSet().iterator();
        Object each = null;

        while ( nodeIter.hasNext() )
        {
          each = nodeIter.next();

          if ( each instanceof Document )
          {
            List results = new ArrayList(1);

            results.add( each );

            return results;
          }
          else if ( each instanceof Element )
          {
            List results = new ArrayList(1);

            results.add( ((Element)each).getDocument() );

            return results;
          }
        }
      }
      else
      {
        return Collections.EMPTY_LIST;
      }
    }

    Iterator stepIter = getSteps().iterator();
    Step     eachStep = null;
    Object   results  = context;
    
    boolean stepped = false;

    while ( stepIter.hasNext() )
    {
      eachStep = (Step) stepIter.next();

      //System.err.println("STEP: " + eachStep);

      if ( (!stepped) && isAbsolute() )
      {
        eachStep.setIsAbsolute(true);
      }
      stepped = true;

      context = eachStep.applyTo( context );
    }

    if (stepped)
    {
      return context.getNodeSet();
    }

    return Collections.EMPTY_LIST;
  }
}
