package com.plumcookingwine.repo.view.file

import com.plumcookingwine.repo.base.BasePresenter

class SelectorFilePresenter(view: SelectorFileView) : BasePresenter<SelectorFileView>(view) {

    init {
        view.setPresenter(this)
    }
}