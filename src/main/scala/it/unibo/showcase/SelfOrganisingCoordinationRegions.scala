package it.unibo.showcase
import it.unibo.scafi.incarnations.BasicSimulationIncarnation._
import it.unibo.scafi.simulation.s2.frontend.configuration.environment.ProgramEnvironment.FastPolicy
import it.unibo.scafi.simulation.s2.frontend.incarnation.scafi.bridge.ScafiSimulationInitializer.RadiusSimulation
import it.unibo.scafi.simulation.s2.frontend.incarnation.scafi.bridge.SimulationInfo
import it.unibo.scafi.simulation.s2.frontend.incarnation.scafi.configuration.{
  ScafiProgramBuilder,
  ScafiWorldInformation
}
import it.unibo.scafi.simulation.s2.frontend.incarnation.scafi.world.ScafiWorldInitializer.Random
import it.unibo.scafi.simulation.s2.frontend.view.{ViewSetting, WindowConfiguration}
import it.unibo.scafi.simulation.s2.frontend.view.scalaFX.drawer.ViewEffect
import it.unibo.scafi.space.graphics2D.BasicShape2D.Circle
import it.unibo.scafi.space.{Point2D, Point3D}
import it.unibo.showcase.SelfOrganisingCoordinationRegions._
class SelfOrganisingCoordinationRegions extends AggregateProgram with BuildingBlocks with StandardSensors {
  override def main(): Any = {
    val radius = 300 // average area of interest
    val leader = S(radius, nbrRange) // select leader that will collect information about distributed zone
    val potential = distanceTo(leader) // create a potential field to collect information in the leader
    val averageTemperature =
      collectMean(potential, temperature) // compute the average temperature of the zone in which the leader leads
    // share the average temperature evaluated to the zone in which the leader leads
    val zoneTemperature = broadcast(leader, averageTemperature)
    (leader, broadcast(leader, zoneTemperature))
  }

  def temperature = SelfOrganisingCoordinationRegions.temperatureField(Point3D.toPoint2D(currentPosition()))
}

object SelfOrganisingCoordinationRegions {
  val maxHeight = 1080
  val maxWidth = 1920
  val baseTemperature = 20
  val maxTemperatureSize = 600
  val maxAmplitude = 60
  val baseTemperatureField = TemperatureField.constant(baseTemperature)
  val temperatureField = TemperatureField.compose(
    baseTemperatureField,
    TemperatureField.gaussian(Point2D(0, maxHeight), maxTemperatureSize, maxAmplitude),
    TemperatureField.gaussian(Point2D(maxWidth, 0), maxTemperatureSize, maxAmplitude)
  )
}
object SelfOrganisingCoordinationRegionsMain extends App {
  private val nodes = 1000
  ViewSetting.windowConfiguration = WindowConfiguration()
  // Main used to start the simulation, using scalafx simulator
  ScafiProgramBuilder(
    Random(nodes, maxWidth, maxHeight),
    SimulationInfo(program = classOf[SelfOrganisingCoordinationRegions]),
    RadiusSimulation(radius = 100),
    neighbourRender = true,
    scafiWorldInfo = ScafiWorldInformation(shape = Some(Circle(4))),
    outputPolicy = ViewEffect(baseTemperature, maxAmplitude),
    performance = FastPolicy
  ).launch()
}
