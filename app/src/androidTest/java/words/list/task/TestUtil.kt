package words.list.task

import words.list.task.model.WordModel

/**
 * Created by AhmedEltaher
 */

object TestUtil {
    var wordModels: ArrayList<WordModel> = ArrayList()
    fun initData(): ArrayList<WordModel> {
        var wordModel = WordModel("test",1,"")
        wordModels.add(wordModel)
        return wordModels
    }
}
