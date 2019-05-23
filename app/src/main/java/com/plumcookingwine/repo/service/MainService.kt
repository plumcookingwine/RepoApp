package com.plumcookingwine.repo.service

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MainService {

    /**
     * 普通用户 -  登录
     */
    @FormUrlEncoded
    @POST("membership/loginService")
    fun login(
        @Field("code") code: String,
        @Field("loginAccount") loginAccount: String,
        @Field("password") password: String,
        @Field("uuid") uid: String,
        // @Field("platform") platform: String? = "20"
        @Field("platform") platform: String? = "android"
    ): Observable<String>
}