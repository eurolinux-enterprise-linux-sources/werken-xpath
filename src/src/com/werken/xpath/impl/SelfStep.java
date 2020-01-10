
package com.werken.xpath.impl;

import com.werken.xpath.ContextSupport;

import org.jdom.Element;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class SelfStep extends AbbrStep
{
  
  public SelfStep()
  {
  }

/*
  public Context applyTo(Context context)
  {
    return context;
  }

*/
  public List applyToSelf(Object node,
                          ContextSupport support)
  {
    System.err.println("SelfStep.applyToSelf(" + node + ")");
    List results = new ArrayList(1);
    results.add(node);

    return results;
  }
}
