# Lox Interpreter

Bazel is used to build/test/run the interpreter. There's a AST generator
rule which generates the Expr AST's and produces source for the interpreter library.


When writing this, I was using bazel version 4.2.2.


## Running
tests:

     bazel test --test_output=errors :tests

the interpreter REPL:

    bazel run :jlox

the interpreter:

    bazel run :jlox -- <file>
