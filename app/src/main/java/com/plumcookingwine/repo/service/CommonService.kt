package com.plumcookingwine.repo.service

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @author kangf
 * @data 2019/9/10
 * @description class CommonService
 */

interface CommonService {


    /**
     * 图片上传
     */
    @FormUrlEncoded
    @POST("?s=App.CDN.UploadImgByBase64")
    fun upload(
        @Field("file") file: String,
        @Field("token") token: String,
        @Field("file_name") name: String,
        @Field("uuid") uuid: String = "7F12EB8334CBEB1F227BBE287C5C09B4",
        @Field("app_key") appKey: String = "D7A3C4B98CE8424303E8AFAFDD0331F7"
    ): Observable<String>


    /**
     * part上传方式
     */
    @Multipart
    @POST("?s=App.CDN.UploadImg")
    fun uploadPart(
        @Part("token") token: RequestBody,
        @Part("uuid") uuid: RequestBody,
        @Part("app_key") appKey: RequestBody,
        @Part file: MultipartBody.Part

    ): Observable<String>
}