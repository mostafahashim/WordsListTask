package words.list.task.view.activity.splash

import androidx.lifecycle.MutableLiveData
import words.list.task.MyApplication
import words.list.task.view.activity.baseActivity.BaseActivityViewModel
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SplashViewModel(
    application: MyApplication
) : BaseActivityViewModel(application) {

    var compositeDisposable = CompositeDisposable()
    private var delay = 3000L
    var timerFinished = MutableLiveData<Boolean>()
    var connectionFinished = MutableLiveData<Boolean>()
    var isShowLoader = MutableLiveData<Boolean>()

    init {
        isShowLoader.value = true

        timerFinished.value = false
        connectionFinished.value = true
        startTimer()

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun startTimer() {
        compositeDisposable.add(
            Observable.intervalRange(1, delay, 0, 1, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it == delay) {
                        timerFinished.value = true
                    } else {
                    }
                }
        )
    }

}