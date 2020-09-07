package code_setup.ui_.auth.models.response_model

data class VerifyOTPResponse(
    var response_code: Int,
    var response_message: String,
    var response_obj: ResponseObj
) {

}