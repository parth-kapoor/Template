package code_setup.app_models.response_

data class TourListResponseModel(
    var response_code: Int,
    var response_message: String,
    var response_obj: List<ResponseObj>
) {
    data class ResponseObj(
        var _id: String,
        var booking_date: String,
        var status: String,
        var type: String,
        var distance: String,
        var id: String,
        var seats: Int,
        var routes: List<Route>,
        var start_location: StartLocation,
        var category_name: String,
        var tour_name: String,
        var start_time: String,
        var end_time: String,
        var users: List<User>
    ) {
        data class Route(
            var _id: String,
            var bookings: List<Booking>,
            var is_arrived: Boolean,
            var lat: Double,
            var lng: Double,
            var name: String,
            var waiting: String
        ) {
            data class Booking(
                var contact: String,
                var email: String,
                var name: String,
                var unique_code: String
            )
        }

        data class StartLocation(
            var coordinates: List<Double>,
            var name: String,
            var type: String
        )

        data class User(
            var contact: String,
            var email: String,
            var name: String,
            var unique_code: String
        )
    }
}