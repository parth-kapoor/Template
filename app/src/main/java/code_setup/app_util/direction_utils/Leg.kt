package code_setup.app_util.direction_utils


data class Leg(
    val steps: List<Step>, val duration: Duration = Duration("", 0)
    , val distance: Distance = Distance("", 0)
    , val end_location: EndLocation
    , val start_location: StartLocation
    , val end_address: String
    , val start_address: String
)

class StartLocation {
    val lat: Double = 0.0
    val lng: Double = 0.0

}

class EndLocation {
    val lat: Double = 0.0
    val lng: Double = 0.0

}
