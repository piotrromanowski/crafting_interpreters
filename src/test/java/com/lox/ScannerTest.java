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
}
