package com.plumcookingwine.webview.remote.wrapper

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem

class CallbackWrapperBase(
    private val mWrappedCustomCallback: ActionMode.Callback,
    private val mWrappedSystemCallback: ActionMode.Callback
) : ActionMode.Callback {

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
        try {
            mWrappedCustomCallback.onDestroyActionMode(mode)
            mWrappedSystemCallback.onDestroyActionMode(mode)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
