package code_setup.app_models.response_

data class LoginResponseModel(
    var response_code: Int,
    var response_message: String,
    var response_obj: ResponseObj
) {
    data class ResponseObj(
        var __v: Int,
        var address: String,
        var age: Int,
        var base_price: String,
        var contact: String,
        var contact_string: String,
        var country_code: String,
        var createdAt: String,
        var driver_id: String,
        var education: String,
        var email: String,
        var first_name: String,
        var gender: String,
        var id: String,
        var languages: List<String>,
        var last_name: String,
        var license_no: String,
        var license_proof: String,
        var name: String,
        var overview: String,
        var provider_id: String,
        var status: String,
        var token: String,
        var updatedAt: String,
        var user_image: String
    )
}