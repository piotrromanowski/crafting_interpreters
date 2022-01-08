package com.lox;

import java.util.ArrayList;
import java.util.List;

/*
 *  program        -> declaration* EOF;
 *  declaration    -> varDecl
 *                    | statement;
 *  varDecl        -> "var" IDENTIFIER ( "=" expression)? ";";
 *  statement      -> printStatement
 *                    | expression;
 *                    | block;
 *  printStatement -> "print" expression;
 *  expression     -> assignment;
 *  block          -> "{" statement "}";
 *  assignment     -> IDENTIFIER "=" assignment
 *                    | equality;
 *  equality       -> comparison (("!=" | "==") comparison)*;
 *  comparison     -> term ((">" | ">=" | "<" | "<=") term)*;
 *  term           -> factor (("+" | "-") factor)*;
 *  factor         -> unary (("/" | "*") unary)*;
 *  unary          -> ("!" | "-") unary
 *                    | primary;
 *  primary        -> NUMBER | STRING | "true" | "false" | "nil"
 *                    | "(" expression ")"
 *                    | IDENTIFIER;
 */

class Parser {

  private class ParseError extends RuntimeException {}

  private final List<Token> tokens;
  private int current;

  Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  public List<Stmt> parse() {
    List<Stmt> statements = new ArrayList();
    while (!isAtEnd()) {
      statements.add(declaration());
    }

    return statements;
  }

  private Stmt declaration() {
    try {
      if (match(TokenType.VAR)) return varDeclaration();

      return statement();
    } catch (ParseError error) {
      synchronize();
      return null;
    }
  }

  private Stmt varDeclaration() {
    Token name = consume(TokenType.IDENTIFIER, "Expected variable name.");

    Expr initializer = null;
    if (match(TokenType.EQUAL)) {
      initializer = expression();
    }
    consume(TokenType.SEMICOLON, "Expected ';' after variable declaration.");

    return new Stmt.Var(name, initializer);
  }

  private Stmt statement() {
    if (match(TokenType.PRINT)) {
      return printStatement();
    }
    if (match(TokenType.LEFT_BRACE)) {
      return new Stmt.Block(block());
    }

    return expressionStatement();
  }

  private Stmt printStatement() {
    Expr value = expression();
    consume(TokenType.SEMICOLON, "Expect ';' after value.");
    return new Stmt.Print(value);
  }

  private List<Stmt> block() {
    List<Stmt> stmts = new ArrayList();

    while (!check(TokenType.RIGHT_BRACE)) {
      stmts.add(declaration());
    }

    consume(TokenType.RIGHT_BRACE, "Expected '}' after block.");

    return stmts;
  }

  private Stmt expressionStatement() {
    Expr value = expression();
    consume(TokenType.SEMICOLON, "Expect ';' after value.");
    return new Stmt.Expression(value);
  }

  private Expr expression() {
    return assignment();
  }

  private Expr assignment() {
    Expr expr = equality();

    if (match(TokenType.EQUAL)) {
      Token equals = previous();
      Expr value = assignment();

      if (expr instanceof Expr.Variable) {
        Token name = ((Expr.Variable)expr).name;
        return new Expr.Assign(name, value);
      }

      error(equals, "Invalid assignment target.");
    }

    return expr;
  }

  private Expr equality(){
    Expr expr = comparison();
    while  (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
      Token operator = previous();
      Expr right = comparison();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  private boolean match(TokenType ...types) {
    for (TokenType type: types) {
      if (check(type)) {
        advance();
        return true;
      }
    }
    return false;
  }

  private Token consume(TokenType type, String message) {
    if (check(type)) {
      return advance();
    }
    throw error(peek(), message);
  }

  private boolean check(TokenType type) {
    if (isAtEnd()) return false;
    return peek().type == type;
  }

  private Token peek() {
    return tokens.get(current);
  }

  private Token previous() {
    return tokens.get(current - 1);
  }

  private boolean isAtEnd() {
    return peek().type == TokenType.EOF;
  }

  private Token advance() {
    if (!isAtEnd()) {
      current++;
    }
    return previous();
  }

  private Expr comparison() {
    Expr expr = term();

    while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
      Token operator = previous();
      Expr right = term();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  private Expr term() {
    Expr expr = factor();

    while (match(TokenType.MINUS, TokenType.PLUS)) {
      Token operator = previous();
      Expr right = factor();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  private Expr factor() {
    Expr expr = unary();

    while (match(TokenType.SLASH, TokenType.STAR)) {
      Token operator = previous();
      Expr right = unary();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  private Expr unary() {
    if (match(TokenType.BANG, TokenType.MINUS)) {
      Token operator = previous();
      Expr expr = unary();
      return new Expr.Unary(operator, expr);
    }
    return primary();
  }

  private Expr primary() {
    if (match(TokenType.FALSE)) return new Expr.Literal(false);
    if (match(TokenType.TRUE)) return new Expr.Literal(true);
    if (match(TokenType.NIL)) return new Expr.Literal(null);

    if (match(TokenType.NUMBER, TokenType.STRING)) {
      Token t = previous();
      return new Expr.Literal(t.literal);
    }

    if (match(TokenType.IDENTIFIER)) {
      return new Expr.Variable(previous());
    }

    if (match(TokenType.LEFT_PAREN)) {
      Expr expr = expression();
      consume(TokenType.RIGHT_PAREN, "Expect ')' after expression");
      return new Expr.Grouping(expr);
    }

    throw error(peek(), "Expected expression.");
  }

  private ParseError error(Token token, String message) {
    Lox.error(token, message);
    return new ParseError();
  }

  private void synchronize() {
    advance();
    while (!isAtEnd()) {
      if (previous().type == TokenType.SEMICOLON) return;

      switch (peek().type) {
        case CLASS:
        case FOR:
        case FUN:
        case IF:
        case PRINT:
        case RETURN:
        case VAR:
        case WHILE:
          return;
      }
      advance();
    }
  }
}
