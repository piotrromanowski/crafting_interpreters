

def _impl(ctx):
  ctx.actions.run(
    inputs = [],
    outputs = [ctx.outputs.expr],
    arguments = [ctx.outputs.expr.dirname],
    executable = ctx.executable._generate,
  )

generate_ast = rule(
  _impl,
  attrs = {
    "_generate": attr.label(
      executable = True,
      cfg = "host",
      allow_files = True,
      default = Label("//:generate_ast")
    )
  },
  outputs = {
    "expr": "Expr.java"
  }
)
