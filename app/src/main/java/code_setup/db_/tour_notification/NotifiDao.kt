package code_setup.db_.tour_notification

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotifiDao {

    @get:Query("SELECT * FROM notifi")
    val all: List<Notifi>

    @Query("SELECT * FROM notifi where notification_id LIKE  :notificationId")
    fun findById(notificationId: String): Notifi

    @Query("SELECT COUNT(*) from notifi")
    fun countTours(): Int

    @Insert
    fun insertAll(vararg notifi: Notifi)

    @Delete
    fun delete(notifi: Notifi)

    @Query("DELETE FROM notifi WHERE notification_id = :notificationId")
    fun deleteById(notificationId: String)

}