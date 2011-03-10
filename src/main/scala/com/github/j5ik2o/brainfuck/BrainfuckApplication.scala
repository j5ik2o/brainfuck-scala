package com.github.j5ik2o.brainfuck

object BrainfuckApplication {

  def main(args:Array[String]) =
    new BrainfuckRuntime(new BrainfuckParser).execute(args(0))
  
}