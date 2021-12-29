package com.lox;

import java.util.List;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AstPrinterTest {

  @Test
  public void testPrintExpression() throws Exception {
    Expr expression = new Expr.Binary(
      new Expr.Unary(
        new Token(TokenType.MINUS, "-", null, 1),
        new Expr.Literal(123)),
      new Token(TokenType.STAR, "*", null, 1),
      new Expr.Grouping(new Expr.Literal(45.67))
    );
    AstPrinter printer = new AstPrinter();
    assertEquals(printer.print(expression), "(* (- 123) (group 45.67))");
  }
}
