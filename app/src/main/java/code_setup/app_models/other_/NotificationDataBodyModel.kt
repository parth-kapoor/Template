package code_setup.app_models.other_

import java.io.Serializable

data class NotificationDataBodyModel(
        var booking_date: String,
        var delivery_address: DeliveryAddress,
        var delivery_loc: DeliveryLoc,
        var id: String,
        var order_no: String,
        var products_count: Int,
        var pickup_address: PickupAddress,
        var pickup_loc: PickupLoc,
        var time_left: Int,
        var user: User
) {
    data class DeliveryAddress(
            var address: String,
            var landmark: String,
            var type: String
    )

    data class DeliveryLoc(
            var coordinates: List<Double>,
            var type: String
    )

    data class PickupAddress(
            var address: String,
            var contact: String,
            var name: String
    )

    data class PickupLoc(
            var coordinates: List<Double>,
            var type: String
    )

    data class User(
            var contact: String,
            var name: String
    )
}