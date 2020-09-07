package code_setup.db_.locations_record

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import code_setup.db_.locations_record.RecentLocations

@Dao
interface RecentLocationsDao {

    @get:Query("SELECT * FROM recentLocations")
    val all: List<RecentLocations>

    @Query("SELECT * FROM recentLocations where user_id LIKE  :userId")
    fun findById(userId: String): RecentLocations

    @Query("SELECT * FROM recentLocations where update_status LIKE  :updateStatus")
    fun findByUpdateStatus(updateStatus: Boolean): RecentLocations


    @Query("SELECT COUNT(*) from recentLocations")
    fun countLocations(): Int


    @Insert
    fun insertAll(vararg recentLocations: RecentLocations)

    @Query("DELETE FROM recentLocations")
    fun deleteAll()

    @Delete
    fun delete(recentLocations: RecentLocations)

}