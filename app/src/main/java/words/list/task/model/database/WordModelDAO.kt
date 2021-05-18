package words.list.task.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import words.list.task.model.WordModel

@Dao
interface WordModelDAO {
    @Query("SELECT * FROM WordModel")
    fun getWordModels(): Flowable<List<WordModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg wordModels: WordModel): Completable

    @Query("DELETE FROM WordModel")
    fun deleteAll(): Completable
}