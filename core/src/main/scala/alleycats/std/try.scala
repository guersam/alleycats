package alleycats
package std

import cats.Bimonad
import scala.util.Try

object try_ extends TryInstances

trait TryInstances {

  // There are various concerns people have over Try's ability to
  // satisfy the monad laws. For example, consider the following code:
  //
  //     def verify(n: Int): Int =
  //       if (n == 0) sys.error("nope") else n
  //
  //     val x = Try(0).flatMap(verify)
  //     val y = verify(0)
  //
  // The monad laws require that `x` and `y` produce the same value,
  // but in this case `x` is a `Failure(_)` and `y` is undefined (due
  // an error being thrown).
  //
  // Since `verify` is not a total function, it is arguable whether
  // this constitutes a law violation, but there is enough concern
  // that the Monad[Try] instance has ended up here in Alleycats.
  //
  // Furthermore, since Cats has introduced a Bimonad[A], the Monad[Try]
  // and Comanad[Try] instances have been replaced by a single Bimonad[Try]
  // instance.
  implicit val tryBimonad: Bimonad[Try] =
    new Bimonad[Try] {
      def pure[A](a: A): Try[A] = Try(a)
      override def map[A, B](fa: Try[A])(f: A => B): Try[B] = fa.map(f)
      def flatMap[A, B](fa: Try[A])(f: A => Try[B]): Try[B] = fa.flatMap(f)
      def coflatMap[A, B](fa: Try[A])(f: Try[A] => B): Try[B] = Try(f(fa))
      def extract[A](p: Try[A]): A = p.get
  }
}
