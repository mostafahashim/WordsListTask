package words.list.task.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import words.list.task.model.WordModel

@Database(entities = [WordModel::class], version = 1,exportSchema = false)
//@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordModelDAO(): WordModelDAO
}