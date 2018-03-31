package org.lamcalcj.utils

import org.scalatest.FunSpec
import org.lamcalcj.ast.Lambda._
import org.lamcalcj.utils.Utils._

class UtilsTest extends FunSpec {
  describe("Alpha conversion") {
    it("Trivial terms should not be converted") {
      val id_x: Identifier = Identifier("x")
      val id_y: Identifier = Identifier("y")
      assert(alphaConversion(Var(id_x)) == (Var(id_x)))
      assert(alphaConversion(Abs(Var(id_x), Var(id_x))) == (Abs(Var(id_x), Var(id_x))))
      assert(alphaConversion(
        Abs(Var(id_x)) {
          Abs(Var(id_y)) {
            App(Var(id_x), Var(id_y))
          }
        }) ==
        Abs(Var(id_x)) {
          Abs(Var(id_y)) {
            App(Var(id_x), Var(id_y))
          }
        })
    }
    it("Overlapping bound variable should be converted") {
      val id_x0: Identifier = Identifier("x")
      val id_x1: Identifier = Identifier("x")
      assert(alphaConversion(Abs(Var(id_x0), Abs(Var(id_x1), Var(id_x1))))
        .asInstanceOf[Abs].term.asInstanceOf[Abs].variable.identifier != id_x1)
    }
    it("Overlapping free variable should not be converted") {
      val id_x0: Identifier = Identifier("x")
      val id_x1: Identifier = Identifier("x")
      assert(alphaConversion(Abs(Var(id_x0), Var(id_x1)))
        .asInstanceOf[Abs].term.asInstanceOf[Var].identifier == id_x1)
    }
  }
}