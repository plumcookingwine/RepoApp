package com.pulmwine.processweb.ui

import android.os.Bundle
import com.pulmwine.processweb.R
import com.pulmwine.processweb.common.WebConstants
import com.pulmwine.processweb.ui.base.BaseWebFragment

/**
 * Created by xud on 2017/12/16.
 */

class AccountWebFragment : BaseWebFragment() {

    override fun commandLevel(): Int {
        return WebConstants.LEVEL_ACCOUNT
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_common_webview
    }

    companion object {

        fun newInstance(keyUrl: String): AccountWebFragment {
            val fragment = AccountWebFragment()
            fragment.arguments = getBundle(keyUrl)
            return fragment
        }

        private fun getBundle(url: String): Bundle {
            val bundle = Bundle()
            bundle.putString(WebConstants.INTENT_TAG_URL, url)
            return bundle
        }
    }
}
