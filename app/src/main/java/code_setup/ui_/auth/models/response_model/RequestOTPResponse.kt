package code_setup.ui_.auth.models.response_model

data class RequestOTPResponse(
    var response_code: Int,
    var response_message: String,
    var response_obj: ResponseObj
) {
    data class ResponseObj(
        var contact: Long,
        var country_code: String
    )
}