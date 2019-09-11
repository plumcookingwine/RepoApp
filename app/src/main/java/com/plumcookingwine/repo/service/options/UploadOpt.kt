package com.plumcookingwine.repo.service.options

import com.plumcookingwine.network.config.AbsRequestOptions
import com.plumcookingwine.repo.service.CommonService
import com.plumcookingwine.repo.util.ImageUtils
import io.reactivex.Observable
import retrofit2.Retrofit
import java.io.File

/**
 * @author kangf
 * @data 2019/9/10
 * @description class UploadOpt
 * 使用base64 上传方式
 */
class UploadOpt(private val file: File) : AbsRequestOptions<String>() {

    init {
        isCache = false
        baseUrl = "http://hb9.api.yesapi.cn/"
    }

    override fun createService(retrofit: Retrofit): Observable<String> {

        val builder: StringBuilder = StringBuilder()
        for (i in 0..64) {
            builder.append("$i")
        }
        val token = builder.toString().substring(0, 64)
        val fileName = file.name
        val suffix = fileName.substring(fileName.lastIndexOf(".") + 1)
        return retrofit.create(CommonService::class.java)
            .upload(
                "data:image/" + suffix + ";base64," + ImageUtils.imageToBase64(file.path),
                token,
                fileName
            )
    }


}