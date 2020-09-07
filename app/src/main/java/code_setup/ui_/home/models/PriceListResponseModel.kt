package code_setup.ui_.home.models

data class PriceListResponseModel(
    var response_code: Int,
    var response_message: String,
    var response_obj: List<ResponseObj>
) {
    data class ResponseObj(
        var capacity: String,
        var category_id: String,
        var est_time: String,
        var image: String,
        var name: String,
        var price: Price,
        var total_price: String,
        var isSelected: Boolean
    ) {
        data class Price(
            var base_price: String,
            var convenience_charges: String,
            var distance_price: String,
            var duration_price: String,
            var surge_multiplier: String,
            var total_distance: String,
            var total_duration: String,
            var per_km_price: String,
            var total_price: String
        )
    }
}