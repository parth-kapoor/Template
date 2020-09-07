package code_setup.ui_.auth.models.request_model

import java.io.Serializable

data class RequestSocialLogin(
    var name: String,
    var email: String,
    var id: String,
    var type: String
):Serializable