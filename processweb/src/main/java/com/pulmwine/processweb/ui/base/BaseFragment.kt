package com.pulmwine.processweb.ui.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View

/**
 * Created by xud on 2017/12/16.
 */

abstract class BaseFragment : Fragment() {

    private var mContext: Context? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mContext == null) {
            mContext = context
        }
    }

    override fun getContext(): Context? {
        return if (super.getContext() == null) mContext else super.getContext()
    }

}
