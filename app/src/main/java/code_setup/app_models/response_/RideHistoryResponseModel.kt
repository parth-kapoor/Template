package code_setup.app_models.response_

data class RideHistoryResponseModel(
    var response_code: Int,
    var response_message: String,
    var response_obj: List<ResponseObj>
) {
    data class ResponseObj(
        var _id: String,
        var booking_date: String,
        var category_name: String,
        var distance: String,
        var driver_rating: Int,
        var id: String,
        var routes: List<Route>,
        var seats: Int,
        var start_location: StartLocation,
        var status: String,
        var tour_name: String,
        var type: String,
        var userBookings: List<Any>,
        var users: List<Any>
    ) {
        data class Route(
            var _id: String,
            var driver_coordinates: List<Double>,
            var is_arrived: Boolean,
            var lat: Double,
            var lng: Double,
            var name: String,
            var waiting: String
        )

        data class StartLocation(
            var coordinates: List<Double>,
            var name: String,
            var type: String
        )
    }
}