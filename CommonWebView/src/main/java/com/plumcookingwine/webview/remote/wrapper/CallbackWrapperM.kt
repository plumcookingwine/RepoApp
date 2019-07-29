package com.plumcookingwine.webview.remote.wrapper

import android.annotation.TargetApi
import android.graphics.Rect
import android.os.Build
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View

@TargetApi(Build.VERSION_CODES.M)
class CallbackWrapperM(
    private val mWrappedCustomCallback: ActionMode.Callback,
    private val mWrappedSystemCallback: ActionMode.Callback
) : ActionMode.Callback2() {

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        return mWrappedCustomCallback.onCreateActionMode(mode, menu)
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return mWrappedCustomCallback.onPrepareActionMode(mode, menu)
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        return mWrappedCustomCallback.onActionItemClicked(mode, item)
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        mWrappedCustomCallback.onDestroyActionMode(mode)
        mWrappedSystemCallback.onDestroyActionMode(mode)
    }

    override fun onGetContentRect(mode: ActionMode, view: View, outRect: Rect) {
        if (mWrappedCustomCallback is ActionMode.Callback2) {
            mWrappedCustomCallback.onGetContentRect(mode, view, outRect)
        } else if (mWrappedSystemCallback is ActionMode.Callback2) {
            mWrappedSystemCallback.onGetContentRect(mode, view, outRect)
        } else {
            super.onGetContentRect(mode, view, outRect)
        }
    }
}
