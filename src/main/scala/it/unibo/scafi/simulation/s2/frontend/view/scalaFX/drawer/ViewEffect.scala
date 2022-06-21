package it.unibo.scafi.simulation.s2.frontend.view.scalaFX.drawer

import it.unibo.scafi.simulation.s2.frontend.model.sensor.SensorConcept.SensorDevice
import it.unibo.scafi.simulation.s2.frontend.view.scalaFX.drawer.ViewEffect.NodeEffect
import it.unibo.scafi.simulation.s2.frontend.view.scalaFX.modelShapeToFXShape
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import scalafx.Includes._

case class ViewEffect(baseTemperature: Int, deltaTemperature: Double, startingHue: Int = 300) extends FXOutputPolicy {
  private val leaderEffect = NodeEffect(strokeSize = 2, radius = 20, viewOrder = -1)
  private val slaveEffect = NodeEffect(strokeSize = 1, radius = 7, viewOrder = 0)
  override type OUTPUT_NODE = javafx.scene.shape.Shape
  override def nodeGraphicsNode(node: NODE): OUTPUT_NODE =
    modelShapeToFXShape(node.shape, node.position)
  override def deviceToGraphicsNode(node: OUTPUT_NODE, dev: DEVICE): Option[OUTPUT_NODE] = None

  override def updateDevice(node: OUTPUT_NODE, dev: DEVICE, graphicsDevice: Option[OUTPUT_NODE]): Unit = {
    dev match {
      case SensorDevice(sens) =>
        sens.value[Any] match {
          case (leader: Boolean, temperature: Double) =>
            val color = colorFromTemperature(temperature)
            val shape = node.asInstanceOf[Circle]
            (if (leader) leaderEffect else slaveEffect).draw(shape, color)
        }
    }
  }

  private def colorFromTemperature(temperature: Double): Color = {
    val maxHueAndBrightness = 1
    val remapped = temperature - baseTemperature
    val base = startingHue
    Color.hsb(base - ((remapped / deltaTemperature) * base), maxHueAndBrightness, maxHueAndBrightness)
  }
}

object ViewEffect {
  case class NodeEffect(strokeSize: Int, radius: Int, viewOrder: Int) {
    def draw(shape: Circle, color: Color): Unit = {
      shape.fill = color
      shape.stroke = Color.BLACK
      shape.strokeWidth = strokeSize
      shape.radius = radius
      shape.setViewOrder(viewOrder)
    }
  }
}
