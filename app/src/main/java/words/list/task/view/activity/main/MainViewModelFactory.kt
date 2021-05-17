package words.list.task.view.activity.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import words.list.task.MyApplication

class MainViewModelFactory(
    var application: MyApplication
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}