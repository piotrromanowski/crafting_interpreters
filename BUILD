load("@rules_java//java:defs.bzl", "java_binary")

java_binary(
    name = "Lox",
    srcs = [
      "src/main/java/com/lox/Lox.java",
      "src/main/java/com/lox/Scanner.java",
      "src/main/java/com/lox/Expr.java",
      "src/main/java/com/lox/Token.java",
      "src/main/java/com/lox/TokenType.java",
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
