package words.list.task.view.activity.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import words.list.task.MyApplication

class SplashViewModelFactory(
    var application: MyApplication
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(application) as T
    }
}