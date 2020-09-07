package code_setup.app_models.request_

data class UpdateRequestModel(
    var booking_id: String,
    var lat: Double,
    var lng: Double,
    var location_id: String? = null
)