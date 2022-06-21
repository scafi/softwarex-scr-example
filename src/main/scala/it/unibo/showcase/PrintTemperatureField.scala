package it.unibo.showcase

import breeze.linalg.DenseMatrix
import breeze.plot._
import it.unibo.scafi.space.Point2D
import SelfOrganisingCoordinationRegions._

/** This main is used to show the temperature field used in this simulation */
object PrintTemperatureField extends App {
  val field = temperatureField
  val tick = 1
  val rows = 1080 / tick
  val column = 1920 / tick
  val result =
    DenseMatrix.ones[Int](rows, column).mapPairs { case ((y, x), _) => field(Point2D(x * tick, (1080 - y) * tick)) }
  val f2 = Figure()
  f2.subplot(0) += image(result, scale = GradientPaintScale(0, baseTemperature + maxAmplitude, PaintScale.Hot))
  f2.saveas("image.png")
}
