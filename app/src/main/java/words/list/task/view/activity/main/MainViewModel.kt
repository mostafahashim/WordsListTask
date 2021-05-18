package words.list.task.view.activity.main

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.JavascriptInterface
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import words.list.task.MyApplication
import words.list.task.adapter.RecyclerWordsAdapter
import words.list.task.model.WordModel
import words.list.task.model.database.WordModelDAO
import words.list.task.observer.OnJavaScriptFinish
import words.list.task.observer.OnRecyclerItemClickListener
import words.list.task.view.activity.baseActivity.BaseActivityViewModel

class MainViewModel(
    application: MyApplication
) : BaseActivityViewModel(application) {
    lateinit var observer: Observer
    var isShowLoader = MutableLiveData<Boolean>()
    var isShowError = MutableLiveData<Boolean>()
    var isShowNoData = MutableLiveData<Boolean>()

    var compositeDisposable = CompositeDisposable()
    val wordModelDAO: WordModelDAO = db.wordModelDAO()
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
        getLocalWords()
    }

    private fun getLocalWords() {
        compositeDisposable.add(wordModelDAO.getWordModels()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
            }.subscribe({ wordsModelsList ->
                if (!wordsModelsList.isNullOrEmpty()) {
                    isShowNoData.value = false
                    isShowLoader.value = false
                    isShowError.value = false
                    wordModels = ArrayList()
                    wordsModelsList.toCollection(wordModels!!)
                    recyclerWordsAdapter.setList(wordModels!!)
                    recyclerWordsAdapter.notifyDataSetChanged()
                }
            }, {
                Log.i("DBError", it?.message!!)
            })
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    class JSHtmlInterface(var onJavaScriptFinish: OnJavaScriptFinish) {
        @JavascriptInterface
        fun showHTML(html: String) {
            Handler(Looper.getMainLooper()).post {
                Jsoup.parse(html).also { doc ->
                    Handler(Looper.getMainLooper()).postDelayed({
                        var text = doc.text().toString()
                        Log.d("textElementsBefore: ", text)
                        //\p{Arabic}\s\p{N}
                        //\u0621-\u064A
                        //^[\u0621-\u064A\s0-9]+$
                        //[^a-zA-Z0-9-\u0621-\u064A\s\p{N}]
                        text = text.replace(
                            "[^a-zA-Z0-9-\\u0620-\\u06FF\\s\\p{N}]".toRegex(),
                            " "
                        )
                        Log.d("textElementsAfter: ", text)
                        val entries: ArrayList<String> = ArrayList()
                        if (!text.isNullOrEmpty()) {
                            text.split(" ")?.toCollection(entries)
                        }
                        onJavaScriptFinish.onFinish(entries)
                    }, 300)
                }
            }
        }
    }

    var onJavaScriptFinish = object : OnJavaScriptFinish {
        override fun onFinish(wordsList: ArrayList<String>) {
            var newWordModels: ArrayList<WordModel> = ArrayList()
            for (word in wordsList) {
                if (word.trim().isNotEmpty()) {
                    var foundIndex = getIndexByProperty(word, newWordModels)
                    if (foundIndex == -1) {
                        newWordModels.add(WordModel(word, 1, ""))
                    } else {
                        newWordModels[foundIndex] =
                            WordModel(word, newWordModels[foundIndex].count!! + 1, "")
                    }
                }
            }
            isShowLoader.value = false
            isShowError.value = false
            if (newWordModels.isNullOrEmpty() && wordModels.isNullOrEmpty()) {
                isShowNoData.value = true
            } else if (!newWordModels.isNullOrEmpty()) {
                isShowNoData.value = false
                wordModels = ArrayList()
                wordModels = newWordModels
                insertWordsToLocalDB()
            }
        }
    }

    private fun insertWordsToLocalDB() {
        compositeDisposable.add(
            //delete current tabs then insert tabs
            wordModelDAO.insertAll(
                *wordModels?.toTypedArray() ?: ArrayList<WordModel>().toTypedArray()
            )
                .doOnError {
                    Log.i("DBError", it?.message!!)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                    }, {
                        Log.i("DBError", it?.message!!)
                    }
                )
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private fun getIndexByProperty(word: String, list: ArrayList<WordModel>): Int {
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