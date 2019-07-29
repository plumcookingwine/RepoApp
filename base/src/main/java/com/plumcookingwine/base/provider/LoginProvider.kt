package com.plumcookingwine.base.provider

interface LoginProvider {

    fun login(username: String, password: String)
}