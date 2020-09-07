package code_setup.ui_.auth.models.response_model

data class ResponseObj(
        var contact: Long,
        var contact_string: String,
        var contact_verified: Boolean,
        var country_code: String,
        var createdAt: String,
        var email: String,
        var id: String,
        var is_active: Boolean,
        var is_email_verified: Boolean,
        var is_stored: Boolean,
        var joined_date: String,
        var name: String,
        var status: String,
        var token: String,
        var user_image: String,
        var verification_code: String
    )