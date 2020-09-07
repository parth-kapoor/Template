package code_setup.app_models.request_

data class SubmitReviewRequest(
    var booking_id: String,
    var comment: String,
    var rating: String
)