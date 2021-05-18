package words.list.task.view.activity.main

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.layout_error.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import words.list.task.R
import words.list.task.databinding.ActivityMainBinding
import words.list.task.view.activity.baseActivity.BaseActivity

class MainActivity : BaseActivity(
    R.string.app_name, false, true, true,
    false, false, false, false, true,
), MainViewModel.Observer {

    lateinit var binding: ActivityMainBinding
    override fun doOnCreate(arg0: Bundle?) {
        binding = putContentView(R.layout.activity_main) as ActivityMainBinding
        binding.viewModel =
            ViewModelProvider(
                this,
                MainViewModelFactory(application)
            )
                .get(MainViewModel::class.java)
        binding.viewModel!!.baseViewModelObserver = baseViewModelObserver
        binding.viewModel!!.observer = this
        binding.lifecycleOwner = this
        initializeViews()
        setListener()
    }


    override fun initializeViews() {
        createWebView()
    }

    private fun createWebView() {
        try {
            val browser = WebView(this)
            browser.visibility = View.INVISIBLE
            browser.setLayerType(View.LAYER_TYPE_NONE, null)
            browser.settings.javaScriptEnabled = true
            browser.settings.blockNetworkImage = true
            browser.settings.domStorageEnabled = false
            browser.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            browser.settings.loadsImagesAutomatically = false
            browser.settings.setGeolocationEnabled(false)
            browser.settings.setSupportZoom(false)
            browser.addJavascriptInterface(
                MainViewModel.JSHtmlInterface(binding.viewModel?.onJavaScriptFinish!!),
                "JSBridge"
            )
            browser.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    if (binding.viewModel?.wordModels.isNullOrEmpty()) {
                        binding.viewModel?.isShowLoader?.value = true
                        binding.viewModel?.isShowError?.value = false
                        binding.viewModel?.isShowNoData?.value = false
                    }
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView, url: String) {
                    browser.loadUrl("javascript:window.JSBridge.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');")
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    if (binding.viewModel?.wordModels.isNullOrEmpty()) {
                        binding.viewModel?.isShowLoader?.value = false
                        binding.viewModel?.isShowError?.value = true
                    }
                    super.onReceivedError(view, request, error)
                }
            }
            browser.loadUrl("https://www.alalmiyalhura.com/")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun setListener() {
        binding.viewModel!!.isShowError.removeObservers(this)
        binding.viewModel!!.isShowError.observe(this, Observer {
            if (it && lifecycle.currentState == Lifecycle.State.RESUMED) {
                binding.layoutError.ivError.setImageResource(
                    R.drawable.error_router_connection_icon
                )
                binding.layoutError.tvErrorTitleConnection.text =
                    getString(R.string.oh_no)
                binding.layoutError.tvErrorBodyConnection.text =
                    getString(R.string.no_internet_connection)
            }
        })

        binding.layoutError.tvRetry.setOnClickListener {
            createWebView()
        }

        binding.viewModel!!.isShowNoData.removeObservers(this)
        binding.viewModel!!.isShowNoData.observe(this, Observer {
            if (it) {
                binding.layoutNoData.tvErrorBodyConnection.text =
                    getString(R.string.no_data_found)
            }
        })

    }

}