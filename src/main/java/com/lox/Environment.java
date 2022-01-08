package com.lox;

import java.util.HashMap;
import java.util.Map;

class Environment {
  private Environment parent;

  public Environment() {
    this(null);
  }

  public Environment(Environment parent) {
    this.parent = parent;
  }

  private final Map<String, Object> values = new HashMap();

  public void define(String name, Object value) {
    values.put(name, value);
  }

  public void assign(Token name, Object value) {
    if (values.containsKey(name.lexeme)) {
      values.put(name.lexeme, value);
      return;
    }
    if (parent != null) {
      parent.assign(name, value);
    }

    throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
  }

  public Object get(Token name) {
    if (values.containsKey(name.lexeme)) {
      return values.get(name.lexeme);
    }
    if (parent != null) {
      return parent.get(name);
    }

    throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
  }
}
