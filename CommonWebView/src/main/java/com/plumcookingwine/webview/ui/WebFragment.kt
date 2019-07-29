package com.plumcookingwine.webview.ui


import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.plumcookingwine.webview.R
import kotlinx.android.synthetic.main.fragment_web.*

/**
 * 通用的WebView
 */

private const val ARG_WEB_URL = "ARG_WEB_URL"
private const val ARG_WEB_TITLE = "ARG_WEB_TITLE"

class WebFragment : Fragment() {

    private var mActivity: Activity? = null

    private var mTitle: String? = null

    private var mUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity?.window?.setFormat(PixelFormat.TRANSLUCENT)
        mUrl = arguments?.getString(ARG_WEB_URL)
        mTitle = arguments?.getString(ARG_WEB_TITLE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        logic()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context != null && context is Activity) {
            mActivity = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }


    private fun logic() {
        mWebView.loadUrl(mUrl)
    }

    fun canGoBack(): Boolean {
        if (mWebView.canGoBack()) {
            mWebView.goBack()
            return true
        }
        return false
    }

    companion object {
        fun newInstance(url: String? = "", title: String? = ""): WebFragment {
            return WebFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_WEB_TITLE, title)
                    putString(ARG_WEB_URL, url)
                }
            }
        }
    }

}
