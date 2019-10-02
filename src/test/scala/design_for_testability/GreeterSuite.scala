package design_for_testability

import java.time.LocalTime

import cats.Id
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatestplus.scalacheck.Checkers
import cats.syntax.apply._
import org.scalacheck.Prop._
import org.scalacheck.cats.implicits._

import org.scalatest.FunSuite
import org.scalatest.Matchers._

class GreeterSuite extends FunSuite with Checkers with TableDrivenPropertyChecks {
  import Greeter._

  test("与えられた時刻があいさつに正しく対応付けられる") {
    val greetings = Table(
      ("時刻",                   "あいさつ"),
      (( 0,  0,  0,         0), "こんばんは"),
      (( 4, 59, 59, 999999999), "こんばんは"),
      (( 5,  0,  0,         0), "おはようございます"),
      ((11, 59, 59, 999999999), "おはようございます"),
      ((12,  0,  0,         0), "こんにちは"),
      ((17, 59, 59, 999999999), "こんにちは"),
      ((18,  0,  0,         0), "こんばんは"),
      ((23, 59, 59, 999999999), "こんばんは"),
    )
    forAll(greetings) { case ((h, m, s, n), expected) =>
      val t = LocalTime.of(h, m, s, n)
      greetingOf(t) should equal (expected)
    }
  }
  test("現在時刻があいさつに正しく対応付けられる") {
    implicit val arbTime: Arbitrary[LocalTime] = Arbitrary {
      val hourGen = Gen.choose(0, 23)
      val minGen  = Gen.choose(0, 59)
      val secGen  = Gen.choose(0, 59)
      val nanoGen = Gen.choose(0, 999999999)
      (hourGen, minGen, secGen, nanoGen).mapN { case (h, m, s, n) => LocalTime.of(h, m, s, n) }
    }
    check { t: LocalTime =>
      implicit val nowProvider: Now[Id] = new Now[Id] {
        def time: Id[LocalTime] = t
      }
      Greeter.greetNow[Id] == Greeter.greetingOf(t)
    }
  }
}
