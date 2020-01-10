
package com.werken.xpath.impl;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.ProcessingInstruction;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class PIStep extends NodeTypeStep
{
  private String _target = null;
  
  public PIStep(String axis,
                String target)
  {
    super(axis, null);
    _target = target;
  }

  protected boolean matches(Object node)
  {
    
    if ( node instanceof ProcessingInstruction )
    {
      if ( _target == null )
      {
        return true;
      }
      else if ( _target.equals( ((ProcessingInstruction)node).getTarget() ) )
      {
        return true;
      }
    }

    return false;
  }
}
