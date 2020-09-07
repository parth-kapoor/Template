package code_setup.db_.tour_updates

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tour")
class Tour {

    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "tour_name")
    var tourName: String? = null

    @ColumnInfo(name = "tour_id")
    var tourId: String? = null

    @ColumnInfo(name = "tour_status")
    var tourStatus: String? = null

    @ColumnInfo(name = "tour_data")
    var tourData: String? = null

    @ColumnInfo(name = "last_updated")
    var lastUpdated: Long = 0

    @ColumnInfo(name = "other_value")
    var otherValue: String? = null

}