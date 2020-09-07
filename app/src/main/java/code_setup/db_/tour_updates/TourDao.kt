package code_setup.db_.tour_updates

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TourDao {

    @get:Query("SELECT * FROM tour")
    val all: List<Tour>

    @Query("SELECT * FROM tour where tour_id LIKE  :tourId")
    fun findById(tourId: String): Tour

    @Query("SELECT COUNT(*) from tour")
    fun countTours(): Int

    @Insert
    fun insertAll(vararg tour: Tour)

    @Delete
    fun delete(tour: Tour)

}