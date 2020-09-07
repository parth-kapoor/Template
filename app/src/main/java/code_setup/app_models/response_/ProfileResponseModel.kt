package code_setup.app_models.response_

data class ProfileResponseModel(
    var response_code: Int,
    var response_message: String,
    var response_obj: ResponseObj
) {
    data class ResponseObj(
        var _id: String,
        var address: String,
        var age: Int,
        var driver_id: String,
        var base_price: String,
        var token: String,
        var complements: List<Complement>,
        var contact: Long,
        var contact_string: String,
        var country_code: String,
        var createdAt: String,
        var education: String,
        var email: String,
        var gender: String,
        var id: String,
        var languages: List<String>,
        var license_no: String,
        var license_proof: String,
        var name: String,
        var overview: String,
        var provider_id: String,
        var rating: String,
        var reviews: List<Review>,
        var status: String,
        var updatedAt: String,
        var user_image: String
    ) {
        data class Complement(
            var count: Int,
            var image: String,
            var name: String
        )

        data class Review(
            var name: String,
            var review: String
        )
    }
}