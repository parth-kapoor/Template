package code_setup.ui_.auth.models.request_model

class RequestOTPModel(var country_code: String,
                      var contact: String,
                      var name: String?="",
                      var email: String?="",
                      var id: String?="",
                      var type: String?=""
)