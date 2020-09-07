package code_setup.app_models.request_

data class OrderDetailRequestModel(
    var order_id: String,
    var lat: Double=0.0,
    var lng: Double=0.0
)