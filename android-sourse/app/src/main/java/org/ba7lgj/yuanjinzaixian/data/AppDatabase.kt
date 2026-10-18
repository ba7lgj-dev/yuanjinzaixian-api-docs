package org.ba7lgj.yuanjinzaixian.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        VoltageSampleEntity::class,
        BatteryAnchorEntity::class,
        AnchorRequestEntity::class,
        ChargeEventEntity::class,
        AppSettingsEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun voltageSampleDao(): VoltageSampleDao
    abstract fun batteryAnchorDao(): BatteryAnchorDao
    abstract fun anchorRequestDao(): AnchorRequestDao
    abstract fun chargeEventDao(): ChargeEventDao
    abstract fun appSettingsDao(): AppSettingsDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "yuanjin_battery.db"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                    .also { instance = it }
            }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE app_settings ADD COLUMN nickname TEXT")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS anchor_requests (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "type TEXT NOT NULL, " +
                        "requestedAt INTEGER NOT NULL, " +
                        "expiresAt INTEGER NOT NULL, " +
                        "status TEXT NOT NULL)"
                )
                db.execSQL("DELETE FROM battery_anchors")
            }
        }
    }
}
