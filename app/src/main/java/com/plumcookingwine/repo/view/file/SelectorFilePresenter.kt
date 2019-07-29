package com.plumcookingwine.repo.view.file

import com.plumcookingwine.base.view.BasePresenter

class SelectorFilePresenter(view: SelectorFileView) : BasePresenter<SelectorFileView>(view) {

    init {
        view.setPresenter(this)
    }
}