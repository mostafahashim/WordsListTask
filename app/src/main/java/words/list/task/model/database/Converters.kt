package words.list.task.model.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import words.list.task.model.WordModel

class Converters {
    @TypeConverter
    fun tabModelsToJson(value: List<WordModel>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToTabModels(value: String) = Gson().fromJson(value, Array<WordModel>::class.java).toList().toCollection(ArrayList())

    @TypeConverter
    fun tabModelToJson(value: WordModel?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToTabModel(value: String) = Gson().fromJson(value, WordModel::class.java)
}