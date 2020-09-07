package code_setup.app_models.response_

data class MessageListResponseModel(
    var response_code: Int,
    var response_message: String,
    var response_obj: List<ResponseObj>
) {
    data class ResponseObj(
        var _id: String,
        var createdAt: String,
        var message: String,
        var sender_id: String
    )
}