package code_setup.app_models.response_

data class OnBoardingResponseModel(
    var response_code: Int,
    var response_message: String,
    var response_obj: ResponseObj
) {
    data class ResponseObj(
        var user_id: String
    )
}