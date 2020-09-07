package code_setup.app_models.other_

import java.io.Serializable
data class NotificationModel(
    var data: String? = null,
    var action_code: String? = null,
    var appointment_id: String? = null,
    var mText: String? = null,
    var body: String? = null,
    var sound: String? = null,
    var title: String? = null
) : Serializable