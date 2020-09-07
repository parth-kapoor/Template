package code_setup.db_.tour_notification

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notifi")
class Notifi {

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "notification_type")
    var notificationType: String? = null

    @ColumnInfo(name = "notification_id")
    var notificationId: String? = null

    @ColumnInfo(name = "notification_status")
    var notificationStatus: String? = null

    @ColumnInfo(name = "notification_data")
    var notificatcionData: String? = null

    @ColumnInfo(name = "last_updated")
    var lastUpdated: Long = 0

    @ColumnInfo(name = "notifictaion_time")
    var notifictaionTime: Long? = null

    @ColumnInfo(name = "other_value")
    var otherValue: String? = null

}