package com.plumcookingwine.repo.view.user.avatar

import com.plumcookingwine.base.view.BaseView

/**
 * @author kangf
 * @data 2019/9/10
 * @description class UploadImageView
 */
interface UploadImageView : BaseView {

    fun uploadProgress(currP: Long, total: Long, ind: Int)

    fun uploadSuccess(ind:Int)

    fun uploadError(ind:Int)
}