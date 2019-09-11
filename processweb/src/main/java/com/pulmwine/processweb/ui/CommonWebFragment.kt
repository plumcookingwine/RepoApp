package com.pulmwine.processweb.ui

import android.os.Bundle
import android.view.View
import com.pulmwine.processweb.R
import com.pulmwine.processweb.common.WebConstants
import com.pulmwine.processweb.ui.base.BaseWebFragment
import kotlinx.android.synthetic.main.fragment_common_webview.*

/**
 * Created by xud on 2017/8/10.
 */

class CommonWebFragment : BaseWebFragment() {

    override fun commandLevel(): Int{
        return WebConstants.LEVEL_BASE
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_common_webview
    }

    companion object {

        fun newInstance(url: String): CommonWebFragment {
            val fragment = CommonWebFragment()
            val bundle = Bundle()
            bundle.putSerializable(WebConstants.INTENT_TAG_URL, url)
            fragment.arguments = bundle
            return fragment
        }
    }
}
