package com.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

class GenerateAst {

  public static void main(String[] args) throws Exception {

    if (args.length == 0) {
      System.err.println("Usage: generate_ast <output directory>");
      System.exit(64);
    }
    String outputDir = args[0];

    defineAst(outputDir, "Expr", Arrays.asList(
      "Assign   : Token name, Expr value",
      "Binary   : Expr left, Token operator, Expr right",
      "Grouping : Expr expression",
      "Literal  : Object value",
      "Logical  : Expr left, Token operator, Expr right",
      "Variable : Token name",
      "Unary    : Token operator, Expr right"
    ));

    defineAst(outputDir, "Stmt", Arrays.asList(
      "Expression : Expr expression",
      "If         : Expr condition, Stmt thenBranch, Stmt elseBranch",
      "Var        : Token name, Expr initializer",
      "Print      : Expr expression",
      "Block      : List<Stmt> statements"
    ));
  }

  private static void defineAst(
      String outputDir, String baseName, List<String> types)
    throws IOException {

    String path = outputDir + "/" + baseName + ".java";
    System.out.println(path);
    PrintWriter writer = new PrintWriter(path, "UTF-8");

    writer.println("package com.lox;");
    writer.println();
    writer.println("import java.util.List;");
    writer.println();
    writer.println("abstract class " + baseName + " {");
    writer.println();

    defineVisitor(writer, baseName, types);
    writer.println();

    writer.println("  abstract <R> R accept(Visitor<R> Visitor);");
    writer.println();

    for (String type: types) {
      String className = type.split(":")[0].trim();
      String fields = type.split(":")[1].trim();
      defineType(writer, baseName, className, fields);
      writer.println();
    }

    writer.println("}");
    writer.close();
  }

  private static void defineVisitor(
      PrintWriter writer, String baseName, List<String> types) {

    writer.println("  interface Visitor<R> {");
    for (String type: types) {
      String typeName = type.split(":")[0].trim();
      writer.println("    R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
    }
    writer.println("  }");
  }

  private static void defineType(
      PrintWriter writer, String baseName, String className, String fieldList){

    writer.println("  static class " + className + " extends " + baseName + " {");
    writer.println();

    String[] fields = fieldList.split(", ");
    for (String field: fields) {
      writer.println("    final " + field + ";");
    }
    writer.println();
    // constructor
    writer.println("    " + className + "(" + fieldList + ") {");

    for (String field: fields) {
      String name = field.split(" ")[1];
      writer.println("      this." + name + " = " + name + ";");
    }

    writer.println("    }");
    writer.println();


    writer.println("    @Override");
    writer.println("    <R> R accept(Visitor<R> visitor) {");
    writer.println("      return visitor.visit" + className + baseName + "(this);");
    writer.println("    }");

    writer.println("  }");
  }
}
