package code_setup.app_models.request_

data class SendMessageRequestModel(
    var booking_id: String,
    var message: String
)