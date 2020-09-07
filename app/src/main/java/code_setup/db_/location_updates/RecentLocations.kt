package code_setup.db_.locations_record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recentLocations")
class RecentLocations {

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "user_id")
    var userId: String? = null

    @ColumnInfo(name = "user_status")
    var userStatus: String? = null

    @ColumnInfo(name = "location")
    var ulocation: String? = null

    @ColumnInfo(name = "saveDateTime")
    var saveDateTime: String? = null

    @ColumnInfo(name = "location_lat")
    var ulocationLat: Double? = 0.0

    @ColumnInfo(name = "location_lng")
    var ulocationLng: Double? = 0.0

    @ColumnInfo(name = "time_millisec")
    var timeMillisec: Long? = 0

    @ColumnInfo(name = "is_mock_location")
    var isMockLocation: Boolean = false

    @ColumnInfo(name = "update_status")
    var updateStatus: Boolean? = false

}