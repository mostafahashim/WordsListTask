package words.list.task.view.activity.main

import android.os.Handler
import android.os.Looper
import android.util.Log
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
                Jsoup.parse(html).also { doc ->
                    Handler(Looper.getMainLooper()).postDelayed({
                        doc.getElementsByClass("rtl").also { elements ->
                            Log.d("ElementsSize: ", elements.size.toString())
                            Handler(Looper.getMainLooper()).postDelayed({
                                entries.clear()
                                for (i in 0 until elements.size) {
                                    val text = elements[i].text().toString()
                                    if (!text.isNullOrEmpty()) {
                                        text.split(" ")?.toCollection(entries)
                                    }
                                }
                                onJavaScriptFinish.onFinish(entries)
                            }, 300)
                        }
                    }, 300)
                }
            }
        }
    }

    var onJavaScriptFinish = object : OnJavaScriptFinish {
        override fun onFinish(wordsList: ArrayList<String>) {
            var newWordModels: ArrayList<WordModel> = ArrayList()
            for (word in wordsList) {
                var foundIndex = getIndexByProperty(word, newWordModels)
                if (foundIndex == -1) {
                    newWordModels.add(WordModel(word, 1, ""))
                } else {
                    newWordModels[foundIndex] =
                        WordModel(word, newWordModels[foundIndex].count!! + 1, "")
                }
            }
            isShowLoader.value = false
            isShowError.value = false
            if (newWordModels.isNullOrEmpty()) {
                isShowNoData.value = true
            } else {
                isShowNoData.value = false
                wordModels = ArrayList()
                wordModels = newWordModels
                recyclerWordsAdapter.setList(wordModels!!)
                recyclerWordsAdapter.notifyDataSetChanged()
            }
        }
    }

    fun getIndexByProperty(word: String, list: ArrayList<WordModel>): Int {
        for (i in 0 until list.size) {
            if (list[i].word?.compareTo(word) == 0) {
                return i
            }
        }
        return -1 // not there is list
    }

    interface Observer {
    }

}