load("@rules_java//java:defs.bzl", "java_binary")
load("//:generate_ast.bzl", "generate_ast")

generate_ast(name = "generate_ast_file")

java_binary(
    name = "jlox",
    srcs = [
      "src/main/java/com/lox/AstPrinter.java",
      "src/main/java/com/lox/Interpreter.java",
      "src/main/java/com/lox/Lox.java",
      "src/main/java/com/lox/Parser.java",
      "src/main/java/com/lox/RuntimeError.java",
      "src/main/java/com/lox/Scanner.java",
      "src/main/java/com/lox/Token.java",
      "src/main/java/com/lox/TokenType.java",
      "//:Expr.java",
      "//:Stmt.java",
    ],
    main_class = "com.lox.Lox",
)

java_binary(
    name = "generate_ast",
    srcs = [
      "src/main/java/com/tool/GenerateAst.java",
    ],
    main_class = "com.tool.GenerateAst",
)

java_test(
    name = "tests",
    srcs = [
        "src/test/java/com/lox/AstPrinterTest.java",
        "src/test/java/com/lox/ScannerTest.java",
        "src/test/java/com/lox/Tests.java",
    ],
    test_class = "com.lox.Tests",
    deps = [
        ":Lox",
        "@maven//:com_google_guava_guava",
        "@maven//:junit_junit",
    ],
)
