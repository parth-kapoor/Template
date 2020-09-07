package code_setup.app_models.request_

data class StartMovingRequestModel(
    var booking_id: String,
    var lat: Double,
    var lng: Double
)