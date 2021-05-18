package words.list.task.model.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import words.list.task.TestUtil
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest : TestCase() {

    private lateinit var wordModelDAO: WordModelDAO
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        wordModelDAO = db.wordModelDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertWordsList() {
        var wordModels = TestUtil.initData()
        wordModelDAO.insertAll(*wordModels.toTypedArray())
    }

    @Test
    @Throws(Exception::class)
    fun fetchWordsList() {
        var wordModels = wordModelDAO.getWordModels()
    }
}