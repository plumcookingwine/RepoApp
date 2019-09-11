package com.plumcookingwine.repo.service.options

import android.annotation.SuppressLint
import android.util.Log
import com.plumcookingwine.network.body.ProgressRequestBody
import com.plumcookingwine.network.callback.UploadProgressListener
import com.plumcookingwine.network.config.AbsRequestOptions
import com.plumcookingwine.repo.service.CommonService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import java.io.File

/**
 * @author kangf
 * @data 2019/9/10
 * @description class UploadPartOpt  使用MultiPart上传方式,可监听进度
 */
class UploadPartOpt(private val file: File, private val progressListener: (Long, Long) -> Unit) :
    AbsRequestOptions<String>() {

    init {
        isShowProgress = true
        isCache = false
        baseUrl = "http://hb9.api.yesapi.cn/"
    }

    override fun createService(retrofit: Retrofit): Observable<String> {
        val builder: StringBuilder = StringBuilder()
        for (i in 0..64) {
            builder.append("$i")
        }
        val token =
            RequestBody.create(MediaType.parse("text/plain"), builder.toString().substring(0, 64))
        val appKey =
            RequestBody.create(MediaType.parse("text/plain"), "D7A3C4B98CE8424303E8AFAFDD0331F7")
        val uuid =
            RequestBody.create(MediaType.parse("text/plain"), "7F12EB8334CBEB1F227BBE287C5C09B4")

        val fileName = file.name
        val suffix = fileName.substring(fileName.lastIndexOf(".") + 1)
        val requestBody = RequestBody.create(MediaType.parse("image/$suffix"), file)
        val part = MultipartBody.Part.createFormData(
            "file", fileName,
            ProgressRequestBody(requestBody, object : UploadProgressListener {
                @SuppressLint("CheckResult")
                override fun onProgress(currentBytesCount: Long, totalBytesCount: Long) {
                    /*回到主线程中，可通过timer等延迟或者循环避免快速刷新数据*/
                    Observable.just(currentBytesCount)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { aLong ->
                            progressListener(aLong, totalBytesCount)
                            Log.i("TAG", "正在上传 $aLong/$totalBytesCount")
                        }
                }

            })
        )

        return retrofit.create(CommonService::class.java)
            .uploadPart(token, uuid, appKey, part)
    }


}