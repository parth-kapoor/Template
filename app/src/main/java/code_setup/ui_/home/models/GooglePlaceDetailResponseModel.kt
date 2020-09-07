package code_setup.ui_.home.models

data class GooglePlaceDetailResponseModel(
    var html_attributions: List<Any>,
    var result: Result,
    var status: String
) {
    data class Result(
        var address_components: List<AddressComponent>,
        var adr_address: String,
        var formatted_address: String,
        var geometry: Geometry,
        var icon: String,
        var id: String,
        var name: String,
        var place_id: String,
        var reference: String,
        var scope: String,
        var types: List<String>,
        var url: String,
        var utc_offset: Int,
        var vicinity: String
    ) {
        data class AddressComponent(
            var long_name: String,
            var short_name: String,
            var types: List<String>
        )

        data class Geometry(
            var location: Location,
            var viewport: Viewport
        ) {
            data class Location(
                var lat: Double,
                var lng: Double
            )

            data class Viewport(
                var northeast: Northeast,
                var southwest: Southwest
            ) {
                data class Northeast(
                    var lat: Double,
                    var lng: Double
                )

                data class Southwest(
                    var lat: Double,
                    var lng: Double
                )
            }
        }
    }
}