package com.github.j5ik2o.brainfuck

import org.scalatest.FunSuite

class BrainfuckParserTest extends FunSuite {

  test("") {
    val helloWorld = """>+++++++++[<++++++++>-]<.>+++++++[<++++>-]<+.+++++++..+++.[-]>++++++++[<++
++>-]<.>+++++++++++[<+++++>-]<.>++++++++[<+++>-]<.+++.------.--------.[-]>
++++++++[<++++>-]<+.[-]++++++++++."""

    val parser = new BrainfuckParser()
    val runtime = new BrainfuckRuntime(parser)

    runtime.execute(helloWorld)

  }

}