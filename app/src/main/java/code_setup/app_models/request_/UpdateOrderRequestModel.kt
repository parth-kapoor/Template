package code_setup.app_models.request_

data class UpdateOrderRequestModel(
    var lat: Double,
    var lng: Double,
    var order_id: String
)