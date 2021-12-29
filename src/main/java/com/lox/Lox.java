package com.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.lox.Token;
import com.lox.Scanner;

public class Lox {
  static boolean hadError = false;

  public static void main(String args[]) throws Exception {
    if (args.length > 1) {
      System.out.println("Usage: jlox [script]");
      System.exit(64);
    } else if (args.length == 1) {
      runFile(args[0]);
    } else {
      runPrompt();
    }
  }

  private static void runFile(String path) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    run(new String(bytes, Charset.defaultCharset()));

    if (hadError) System.exit(65);
  }

  private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    for(;;) {
      System.out.println("> ");
      String line = reader.readLine();
      if (line == null) break;
      run(line);
      hadError = false;
    }
  }

  private static void run(String source) {
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();
    Parser parser = new Parser(tokens);
    Expr expression = parser.parse();

    if (hadError) return;

    System.out.println(new AstPrinter().print(expression));
    //for (Token token: tokens) {
    //  System.out.println(token);
    //}
  }

  static void error(int line, String error) {
    report(line, "", error);
  }

  static void error(Token token, String error) {
    if (token.type == TokenType.EOF) {
      report(token.line, " at end", error);
    } else {
      report(token.line, " at '" + token.lexeme + "'", error);
    }
  }

  static void report(int line, String where, String message) {
    System.out.println("[line " + line + "] Error" + where + ": " + message);
    hadError = true;
  }

}
