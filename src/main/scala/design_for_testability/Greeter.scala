package design_for_testability

import java.time.LocalTime

import cats.Functor
import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.functor._
import cats.syntax.flatMap._
import simulacrum.typeclass

@typeclass trait Now[F[_]] {
  def time: F[LocalTime]
}
object NowDemo extends IOApp {
  implicit val nowImpl: Now[IO] = new Now[IO] {
    def time: IO[LocalTime] = IO.delay(LocalTime.now)
  }
  def run(args: List[String]): IO[ExitCode] = for {
    now <- Now[IO].time
    _   <- IO { println(now) }
  } yield ExitCode.Success
}
object Greeter {
  // 参照等価な部分
  def greetingOf(t: LocalTime): String = {
    def before(h: Int, m: Int, s: Int) = t.isBefore(LocalTime.of(h, m, s))

    if      (before( 5, 0, 0)) "こんばんは"
    else if (before(12, 0, 0)) "おはようございます"
    else if (before(18, 0, 0)) "こんにちは"
    else                       "こんばんは"
  }
  // 参照等価な greetingOf と、参照透過じゃない Now.time の合成
  def greetNow[F[_]: Functor: Now]: F[String] =
    Now[F].time map greetingOf
}

object GreeterDemo extends IOApp {
  import Greeter._

  def putStrLn(s: String): IO[Unit] = IO(println(s))

  implicit val nowImpl: Now[IO] = new Now[IO] {
    def time: IO[LocalTime] = IO.delay(LocalTime.now)
  }
  def run(args: List[String]): IO[ExitCode] = for {
    _ <- (Now[IO].time map greetingOf) >>= putStrLn
    _ <- greetNow[IO] >>= putStrLn
  } yield ExitCode.Success
}
