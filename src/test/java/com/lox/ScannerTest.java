package com.lox;

import java.util.List;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ScannerTest {

  @Test
  public void testVariableAssignment() throws Exception {
    String input = "var x = 4;";
    Scanner scanner = new Scanner(input);
    List<Token> tokens = scanner.scanTokens();
    assertEquals(tokens.get(0).type, TokenType.VAR);
    assertEquals(tokens.get(1).type, TokenType.IDENTIFIER);
    assertEquals(tokens.get(2).type, TokenType.EQUAL);
    assertEquals(tokens.get(3).type, TokenType.NUMBER);
    assertEquals(tokens.get(4).type, TokenType.SEMICOLON);
  }

  @Test
  public void testFunction() throws Exception {
    String input = "fun(a, b){"+
    "var c = a + b;" +
    "return c;" +
    "}";
    Scanner scanner = new Scanner(input);
    List<Token> tokens = scanner.scanTokens();
    assertEquals(tokens.get(0).type, TokenType.FUN);
    assertEquals(tokens.get(1).type, TokenType.LEFT_PAREN);
    assertEquals(tokens.get(2).type, TokenType.IDENTIFIER);
    assertEquals(tokens.get(3).type, TokenType.COMMA);
    assertEquals(tokens.get(4).type, TokenType.IDENTIFIER);
    assertEquals(tokens.get(5).type, TokenType.RIGHT_PAREN);
    assertEquals(tokens.get(6).type, TokenType.LEFT_BRACE);
    assertEquals(tokens.get(7).type, TokenType.VAR);
    assertEquals(tokens.get(8).type, TokenType.IDENTIFIER);
    assertEquals(tokens.get(9).type, TokenType.EQUAL);
    assertEquals(tokens.get(10).type, TokenType.IDENTIFIER);
    assertEquals(tokens.get(11).type, TokenType.PLUS);
    assertEquals(tokens.get(12).type, TokenType.IDENTIFIER);
    assertEquals(tokens.get(13).type, TokenType.SEMICOLON);
    assertEquals(tokens.get(14).type, TokenType.RETURN);
    assertEquals(tokens.get(15).type, TokenType.IDENTIFIER);
    assertEquals(tokens.get(16).type, TokenType.SEMICOLON);
    assertEquals(tokens.get(17).type, TokenType.RIGHT_BRACE);
  }
}
