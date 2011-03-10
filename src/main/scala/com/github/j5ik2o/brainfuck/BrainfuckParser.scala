package com.github.j5ik2o.brainfuck

import util.parsing.combinator._

// 式を訪問するビジター
trait ExpressionVisitor {
  def visit(e: Expression): Unit
}

// 式を表すトレイト
trait Expression {
  def accept(visitor: ExpressionVisitor): Unit = {
    visitor.visit(this)
  }
}

// 式の実装 これを使ってASTを構築する
case class IncrementPointerExpression extends Expression
case class DecrementPointerExpression extends Expression
case class IncrementMemoryAtPointerExpression extends Expression
case class DecrementMemoryAtPointerExpression extends Expression
case class OutputMemoryAtPointerExpression extends Expression
case class LoopExpression(expressions:List[Expression]) extends Expression

// Brainfuckパーサ
class BrainfuckParser extends RegexParsers {

  def parse(source:String) = parseAll(brainfuck, source)

  def brainfuck: Parser[List[Expression]] = rep(instruction)

  def instruction: Parser[Expression] = loop | token

  def token: Parser[Expression] = incrementPointer ||| decrementPointer |||
    incrementMemoryAtPointer ||| decrementMemoryAtPointer ||| outputMemoryAtPointer

  def incrementPointer: Parser[Expression] = ">" ^^ {
    case ops => IncrementPointerExpression()
  }

  def decrementPointer: Parser[Expression] = "<" ^^ {
    case ops => DecrementPointerExpression()
  }

  def incrementMemoryAtPointer: Parser[Expression] = "+" ^^ {
    case ops => IncrementMemoryAtPointerExpression()
  }

  def decrementMemoryAtPointer: Parser[Expression] = "-" ^^ {
    case ops => DecrementMemoryAtPointerExpression()
  }

  def outputMemoryAtPointer: Parser[Expression] = "." ^^ {
    case ops => OutputMemoryAtPointerExpression()
  }

  def loop: Parser[Expression] = "["~>brainfuck<~"]" ^^ {
    case exprs => LoopExpression(exprs)
  }

}