package words.list.task.model

import java.io.Serializable
import java.util.Comparator

data class WordModel(
    var word: String? = "",
    var count: Int? = 0,
    var holderType: String? = ""
) : Serializable