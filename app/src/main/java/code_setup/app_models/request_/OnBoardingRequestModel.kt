package code_setup.app_models.request_

data class OnBoardingRequestModel(
    var booking_code: String,
    var booking_id: String,
    var lat: String,
    var lng: String
)