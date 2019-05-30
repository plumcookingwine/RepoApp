package com.plumcookingwine.repo.service.options

import com.plumcookingwine.network.config.AbsRequestOptions
import com.plumcookingwine.repo.service.MainService
import io.reactivex.Observable
import retrofit2.Retrofit

class LoginOpt(
    private val code: String,
    private val loginAccount: String,
    private val password: String,
    private val uid: String
) : AbsRequestOptions<String>() {

    init {
        isCache = true
        cacheUrl = "login"
    }

    override fun createService(retrofit: Retrofit): Observable<String> {
        return retrofit.create(MainService::class.java).login(
            code, loginAccount, password, uid
        )
    }

}