package code_setup.app_models.response_

data class MonthlyToursListModel(
    var response_code: Int,
    var response_message: String,
    var response_obj: List<ResponseObj>
) {
    data class ResponseObj(
        var _id: Id,
        var count: Int,
        var month: String
    ) {
        data class Id(
            var day: Int,
            var month: Int,
            var year: Int
        )
    }
}