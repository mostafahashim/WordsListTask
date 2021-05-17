package words.list.task.view.activity.main

import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import androidx.lifecycle.MutableLiveData
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import words.list.task.MyApplication
import words.list.task.adapter.RecyclerWordsAdapter
import words.list.task.model.WordModel
import words.list.task.observer.OnJavaScriptFinish
import words.list.task.observer.OnRecyclerItemClickListener
import words.list.task.view.activity.baseActivity.BaseActivityViewModel
import java.util.stream.IntStream

class MainViewModel(
    application: MyApplication
) : BaseActivityViewModel(application) {
    lateinit var observer: Observer
    var isShowLoader = MutableLiveData<Boolean>()
    var isShowError = MutableLiveData<Boolean>()
    var connectionErrorMessage = ""
    var isShowNoData = MutableLiveData<Boolean>()

    var wordModels: ArrayList<WordModel>? = ArrayList()
    var recyclerWordsAdapter: RecyclerWordsAdapter


    init {
        isShowLoader.value = false
        isShowError.value = false
        isShowNoData.value = false

        recyclerWordsAdapter = RecyclerWordsAdapter(
            wordModels!!, object : OnRecyclerItemClickListener {
                override fun onRecyclerItemClickListener(position: Int) {
                }

            })


    }

    override fun onCleared() {
        super.onCleared()
    }

    class JSHtmlInterface(var onJavaScriptFinish: OnJavaScriptFinish) {
        @JavascriptInterface
        fun showHTML(html: String) {
            val entries: ArrayList<String> = ArrayList()
            Handler(Looper.getMainLooper()).post {
                Jsoup.parse(html).also {doc->
                    doc.getElementsByClass("rtl").also { element->
                        entries.clear()
                        for (i in 0 until element.size) {
                            val text = element[i].text().toString()
                            if (!text.isNullOrEmpty()) {
                                text.split(" ")?.toCollection(entries)
                            }
                        }
                        onJavaScriptFinish.onFinish(entries)
                    }
                }
            }
        }
    }

    var onJavaScriptFinish = object : OnJavaScriptFinish {
        override fun onFinish(wordsList: ArrayList<String>) {
            wordModels = ArrayList()
            for (word in wordsList) {
                var foundIndex = getIndexByProperty(word)
                if (foundIndex == -1) {
                    wordModels?.add(WordModel(word, 1, ""))
                } else {
                    wordModels?.set(foundIndex, WordModel(word, wordModels!![foundIndex].count!! + 1, ""))
                }
            }
            isShowLoader.value = false
            isShowError.value = false
            if (wordModels.isNullOrEmpty()){
                isShowNoData.value = true
            }else{
                recyclerWordsAdapter.setList(wordModels!!)
                recyclerWordsAdapter.notifyDataSetChanged()
            }
        }

    }

    fun getIndexByProperty(word: String): Int {
        for (i in 0 until wordModels?.size!!) {
            if (wordModels!![i] != null && wordModels!![i].word?.compareTo(word) == 0) {
                return i
            }
        }
        return -1 // not there is list
    }

    interface Observer {
    }

}