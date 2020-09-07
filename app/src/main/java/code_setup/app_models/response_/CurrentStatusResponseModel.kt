package code_setup.app_models.response_

import code_setup.app_models.other_.NotificationDataBodyModel

data class CurrentStatusResponseModel(
    var response_code: Int,
    var response_message: String,
    var response_obj: ResponseObj
) {
    data class ResponseObj(
        var notification_data: NotificationDataBodyModel,
        var order_id: String,
        var status: String
    ) /*{
        data class NotificationData(
            var booking_date: String,
            var delivery_address: DeliveryAddress,
            var delivery_loc: DeliveryLoc,
            var expiration_date: Long,
            var id: String,
            var last_timestamp: String,
            var order_no: String,
            var pickup_address: PickupAddress,
            var pickup_loc: PickupLoc,
            var products_count: Int,
            var status: String,
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
    }*/
}