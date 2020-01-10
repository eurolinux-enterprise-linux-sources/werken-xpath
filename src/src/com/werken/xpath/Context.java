
package com.werken.xpath;

import com.werken.xpath.function.Function;

import java.util.List;

public interface Context
{
  List   getNodeSet();
  Object getContextNode();
  int    getPosition();
  int    getSize();

  boolean isEmpty();

  String   translateNamespacePrefix(String prefix);
  Object   getVariableValue(String name);
  Function getFunction(String name);

  ContextSupport getContextSupport();

  void setNodeSet(List nodeSet);

  Context duplicate();
}
