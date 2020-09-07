package code_setup.app_models.response_


data class OrderDetailResponseModel(
    var response_code: Int,
    var response_message: String,
    var response_obj: ResponseObj
) {
    data class ResponseObj(
        var _id: String,
        var address: Address,
        var address_id: String,
        var amount: String,
        var booking_date: String,
        var createdAt: String,
        var delivery_loc: DeliveryLoc,
        var id: String,
        var loc: Loc,
        var order_no: String,
        var payment_mode: String,
        var payment_status: String,
        var pickup_address: PickupAddress,
        var pickup_loc: PickupLoc,
        var products_count: Int,
        var provider_id: Any,
        var status: String,
        var user: User,
        var user_id: String
    ) {
        data class Address(
            var address: String,
            var landmark: String,
            var type: String
        )

        data class DeliveryLoc(
            var lat: Double,
            var lng: Double
        )

        data class Loc(
            var coordinates: List<Double>,
            var type: String
        )

        data class PickupAddress(
            var address: String,
            var contact: String,
            var name: String
        )

        data class PickupLoc(
            var lat: Double,
            var lng: Double
        )

        data class User(
            var contact: String,
            var name: String
        )
    }
}