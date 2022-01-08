package com.lox;

import java.util.List;

class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

  private Environment env = new Environment();

  void interpret(List<Stmt> statements) {
    try {
      for (Stmt stmt: statements) {
        execute(stmt);
      }
    } catch (RuntimeError err) {
      Lox.runtimeError(err);
    }
  }

  void execute(Stmt stmt) {
    stmt.accept(this);
  }

  @Override
  public Void visitExpressionStmt(Stmt.Expression stmt){
    evaluate(stmt.expression);
    return null;
  }

  @Override
  public Void visitVarStmt(Stmt.Var stmt) {
    Object value = null;
    if (stmt.initializer != null) {
      value = evaluate(stmt.initializer);
    }

    env.define(stmt.name.lexeme, value);
    return null;
  }

  @Override
  public Void visitPrintStmt(Stmt.Print stmt){
    Object object = evaluate(stmt.expression);
    System.out.println(stringify(object));
    return null;
  }

  @Override
  public Object visitAssignExpr(Expr.Assign expr) {
    Object value = evaluate(expr.value);
    env.assign(expr.name, value);
    return value;
  }

  @Override
  public Object visitBinaryExpr(Expr.Binary expr) {
    Object left = evaluate(expr.left);
    Object right = evaluate(expr.right);

    switch (expr.operator.type) {

      case MINUS:
        checkNumberOperands(expr.operator, left, right);
        return (double) left - (double) right;
      case PLUS:
        if (left instanceof Double && right instanceof Double) {
          return (double) left + (double) right;
        }
        if (left instanceof String && right instanceof String) {
          return (String) left + (String) right;
        }
        throw new RuntimeError(expr.operator,
            "Operands must be two numbers or two strings");
      case SLASH:
        // TODO: division by zero exception
        checkNumberOperands(expr.operator, left, right);
        return (double) left / (double) right;
      case STAR:
        checkNumberOperands(expr.operator, left, right);
        return (double) left * (double) right;
      case LESS:
        checkNumberOperands(expr.operator, left, right);
        return (double) left < (double) right;
      case LESS_EQUAL:
        checkNumberOperands(expr.operator, left, right);
        return (double) left <= (double) right;
      case GREATER:
        checkNumberOperands(expr.operator, left, right);
        return (double) left > (double) right;
      case GREATER_EQUAL:
        checkNumberOperands(expr.operator, left, right);
        return (double) left >= (double) right;
      case EQUAL_EQUAL:
        checkNumberOperands(expr.operator, left, right);
        return isEqual(left, right);
      case BANG_EQUAL:
        checkNumberOperands(expr.operator, left, right);
        return !isEqual(left, right);
    }

    throw new RuntimeError(expr.operator,
      "Unsupported binary expression");
  }

  @Override
  public Object visitGroupingExpr(Expr.Grouping expr) {
    return evaluate(expr.expression);
  }

  @Override
  public Object visitLiteralExpr(Expr.Literal expr) {
    return expr.value;
  }

  @Override
  public Object visitVariableExpr(Expr.Variable expr) {
    return env.get(expr.name);
  }

  @Override
  public Object visitUnaryExpr(Expr.Unary expr) {
    Object right = evaluate(expr.right);

    switch (expr.operator.type) {
      case MINUS:
        checkNumberOperands(expr.operator, expr.right);
        return -(double) right;
      case BANG:
        return !isTruthy(right);
    }

    // unreachable
    return null;
  }

  private void checkNumberOperands(Token operator, Object ...operands) {
    for (Object operand: operands) {
      if  (!(operand instanceof Double)) {
        throw new RuntimeError(operator, "Operand must be a number");
      }
    }
  }

  private boolean isTruthy(Object o) {
    if (o == null) return false;
    if (o instanceof Boolean) return (boolean) o;
    if (o instanceof String) return ((String) o).length() != 0;
    if (o instanceof Double) return ((double) o) != 0;

    return false;
  }

  private boolean isEqual(Object a, Object b) {
    if (a == null || b == null) return a == b;
    return a.equals(b);
  }

  private Object evaluate(Expr expr){
    return expr.accept(this);
  }

  private String stringify(Object value){
    if (value == null) return "nil";

    if (value instanceof Double) {
      String text = value.toString();
      if (text.endsWith(".0")) {
        text = text.substring(0, text.length() - 2);
      }
      return text;
    }

    return value.toString();
  }
}
