package words.list.task.view.activity.baseActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import words.list.task.MyApplication

class BaseViewModelFactory(
    var application: MyApplication
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BaseActivityViewModel(application) as T
    }
}