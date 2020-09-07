package code_setup.app_models.response_

data class UpdateStatusResponseModel(
    var response_code: Int,
    var response_message: String,
    var response_obj: ResponseObj
) {
    data class ResponseObj(
        var capacity: Int,
        var status: String
    )
}