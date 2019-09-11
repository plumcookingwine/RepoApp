package com.plumcookingwine.repo.view.user.avatar

import android.widget.Toast
import com.plumcookingwine.base.view.BasePresenter
import com.plumcookingwine.network.callback.INetworkCallback
import com.plumcookingwine.network.config.AbsRequestOptions
import com.plumcookingwine.network.cookie.CookieResultListener
import com.plumcookingwine.network.exception.ApiErrorModel
import com.plumcookingwine.network.manager.HttpManager
import com.plumcookingwine.network.manager.UploadManager
import com.plumcookingwine.repo.RepoApp
import com.plumcookingwine.repo.service.options.UploadPartOpt
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @author kangf
 * @data 2019/9/10
 * @description class UploadImagePresenter
 */
class UploadImagePresenter(mView: UploadImageView) : BasePresenter<UploadImageView>(mView) {


    fun upload(file: File) {

        HttpManager.instance.doHttpDeal(UploadPartOpt(file) { currP, totalP ->

            mView.uploadProgress(currP, totalP, 0)

            val percent =
                BigDecimal(currP.toString()).divide(
                    BigDecimal(totalP.toString()),
                    2,
                    RoundingMode.HALF_UP
                )
                    .multiply(BigDecimal("100"))
            mView.showLoading("正在上传$percent%")

        }, object : INetworkCallback<String>(this) {

            override fun onSuccess(obj: String, cookieListener: CookieResultListener) {
                Toast.makeText(RepoApp.context, obj, Toast.LENGTH_LONG).show()
            }
        })

    }


    fun uploadList(files: List<File>) {

        val options = mutableListOf<AbsRequestOptions<String>>()
        val callbacks = mutableListOf<INetworkCallback<String>>()

        files.mapIndexed { ind, it->
            options.add(UploadPartOpt(it) { currP, totalP ->
                mView.uploadProgress(currP, totalP, ind)
            })
            callbacks.add(object :INetworkCallback<String>(this){

                override fun onSuccess(obj: String, cookieListener: CookieResultListener) {
                    mView.uploadSuccess(ind)
                }

                override fun onError(err: ApiErrorModel?) {
                    mView.uploadError(ind)
                }

            } )
        }

        UploadManager.instance.upload(
            options, files, callbacks
        )
    }
}