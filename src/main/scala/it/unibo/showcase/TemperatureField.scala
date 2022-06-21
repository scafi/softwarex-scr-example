package it.unibo.showcase

import breeze.stats.distributions.{Gaussian, RandBasis}
import it.unibo.scafi.space.Point2D
import org.apache.commons.math3.random.MersenneTwister

/** A trait used to simulate a temperature field */
trait TemperatureField extends (Point2D => Double) {
  def apply(point: Point2D): Double
}

object TemperatureField {
  implicit def funToTemp(fun: Point2D => Double): TemperatureField = (point: Point2D) => fun(point)

  /** create a field in which in each point the nodes perceive the same temperature
    * @param temperature
    *   the constant temperature of the system
    */
  def constant(temperature: Double): TemperatureField =
    (_: Point2D) => temperature

  /** Given a sequence of temperature field, it create a new temperature field in which in each point the object returns
    * the sum of the temperature perceived by each field. E.g.
    *
    * val field = constant(10) :: constant(20) :: constant(30)
    *
    * val overallField = compose(field:_*) // for any point, it will return 10 + 20 + 30
    *
    * @param temperatureFields
    *   the field that I want to combine
    */
  def compose(temperatureFields: TemperatureField*): TemperatureField =
    (point: Point2D) => temperatureFields.map(_.apply(point)).sum

  /** Create a temperature field that obey to a gaussian distribution
    * @param where
    *   the point in which the gaussian in centered
    * @param variance
    *   the sigma value of the gaussian
    * @param amplitude
    *   how much the value are amplified (i.e., a scalar factor used as: g(mu, sigma) * amplitude
    */
  def gaussian(where: Point2D, variance: Double, amplitude: Double): TemperatureField = {
    implicit val random = new RandBasis(new MersenneTwister())
    val gaussian = Gaussian(0, variance)
    (point: Point2D) => {
      val (x, y) = (point.x - where.x, point.y - where.y)
      gaussian(x) * amplitude * gaussian(y)
    }
  }
}
