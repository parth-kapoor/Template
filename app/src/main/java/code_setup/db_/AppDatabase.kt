package code_setup.db_

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import code_setup.db_.locations_record.RecentLocations
import code_setup.db_.locations_record.RecentLocationsDao
import code_setup.db_.tour_notification.Notifi
import code_setup.db_.tour_notification.NotifiDao
import code_setup.db_.tour_updates.Tour
import code_setup.db_.tour_updates.TourDao

@Database(
    entities = arrayOf(User::class, Tour::class, Notifi::class, RecentLocations::class),
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun tourDao(): TourDao
    abstract fun notificationDao(): NotifiDao
    abstract fun recentLocationDao(): RecentLocationsDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user-database"
                )
                    // allow queries on the main thread.
                    // Don't do this on a real app! See PersistenceBasicSample for an example.
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE as AppDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}