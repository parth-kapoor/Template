package code_setup.db_

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @get:Query("SELECT * FROM user")
    val all: List<User>

    @Query("SELECT * FROM user where first_name LIKE  :firstName AND last_name LIKE :lastName")
    fun findByName(firstName: String, lastName: String): User

    @Query("SELECT COUNT(*) from user")
    fun countUsers(): Int

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

}