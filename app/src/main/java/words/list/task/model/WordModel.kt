package words.list.task.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Comparator

@Entity
data class WordModel(
    @PrimaryKey(autoGenerate = false) var word: String = "",
    var count: Int? = 0,
    var holderType: String? = ""
) : Serializable