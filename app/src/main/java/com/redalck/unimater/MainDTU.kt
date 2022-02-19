package com.redalck.unimater

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.redalck.unimater.databinding.ActivityDtuBinding

class MainDTU : AppCompatActivity() {
    private lateinit var binding: ActivityDtuBinding
    private lateinit var url : String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        binding = ActivityDtuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPreferences = getSharedPreferences("dtu_assist", Context.MODE_PRIVATE)

        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnResume -> {
                        url = "https://www.rm.dtu.ac.in/"
                    }
                    R.id.btnWebsite -> {
                        url = "http://www.dtu.ac.in/"
                    }
                    R.id.btnERP -> {
                        url = "https://cumsdtu.in/student_dtu/login/login.jsp"
                    }
                }
                load()
            }
        }
        binding.toggleGroup.check(R.id.btnResume)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun load(){
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.domStorageEnabled = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.overScrollMode = WebView.OVER_SCROLL_NEVER
        binding.webView.settings.setSupportZoom(true)
        binding.webView.loadUrl(url)
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                binding.progressBar.visibility = View.VISIBLE
                return true
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                binding.webView.reload()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = GONE
            }
        }
        binding.toolbar.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.webView.loadUrl(url)
        }

        binding.webView.setDownloadListener { p0, _, _, _, _ ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(p0)))
            binding.progressBar.visibility = GONE
        }
    }

    override fun onBackPressed() {
        if(binding.webView.canGoBack()){
            binding.webView.goBack()
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }
}