package com.github.j5ik2o.brainfuck

case class BrainfuckException(msg:String) extends RuntimeException

class BrainfuckRuntime(parser: BrainfuckParser, size: Int) {
  require(size > 0)

  val memory = Array.fill(size)(0)

  var pointer = 0

  var counter = 0

  val evaluator = new Evaluator()

  def this(parser: BrainfuckParser) = this (parser, 3000)

  // Brainfuckのスクリプトを実行する
  def execute(script: String) {
    val parseResult = parser.parse(script)
    if (parseResult.successful) {
      evaluateExpressions(parseResult.get)
      println
    } else {
      throw new BrainfuckException("parse error")
    }
  }

  // ASTを評価するビジター
  class Evaluator extends ExpressionVisitor {

    override def visit(expression: Expression): Unit = expression match {
      case IncrementPointerExpression() => incrementPointer
      case DecrementPointerExpression() => decrementPointer
      case IncrementMemoryAtPointerExpression() => incrementMemoryAtPointer
      case DecrementMemoryAtPointerExpression() => decrementMemoryAtPointer
      case OutputMemoryAtPointerExpression() => outputMemoryAtPointer
      case LoopExpression(expressions: List[Expression]) => loop(expressions)
    }

  }

  // 以下、ランタイムが持つAPI

  private def validateRange =
    if (counter > size) throw new BrainfuckException("limit over")

  private def m = {
    validateRange
    memory(pointer)
  }

  private def m(b: Int) = {
    validateRange
    memory(pointer) = b
  }


  private def incrementPointer {
    validateRange
    pointer += 1
    counter += 1
  }

  private def decrementPointer {
    validateRange
    pointer -= 1
    counter += 1
  }

  private def incrementMemoryAtPointer {
    m(m + 1)
  }

  private def decrementMemoryAtPointer {
    m(m - 1)
  }

  private def outputMemoryAtPointer {
    print(m.toChar)
  }

  private def loop(expressions: List[Expression]) {
    while (memory(pointer) != 0) {
      evaluateExpressions(expressions)
    }
  }

  private def evaluateExpressions(expressions: List[Expression]) {
    expressions.foreach(_.accept(evaluator))
  }


}