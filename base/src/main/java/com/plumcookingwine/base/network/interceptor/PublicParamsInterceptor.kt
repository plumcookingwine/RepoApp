package com.plumcookingwine.base.network.interceptor

import com.plumcookingwine.base.utils.SignUtils
import okhttp3.*
import okio.Buffer
import java.io.IOException

/**
 * Created by kangf on 2018/9/3.
 */
class PublicParamsInterceptor : Interceptor {
    private val key = "xfs7Alc74oCVpHXg97etTs"
    override fun intercept(chain: Interceptor.Chain): Response {

        val oldRequest = chain.request()
        var newRequestBuild: Request.Builder? = oldRequest.newBuilder()
        val method = oldRequest.method()
        val postBodyString: String

        val map = HashMap<String, Any?>()
        map["member_id"] = "123"
        map["account_type"] = 10
        map["timestamp"] = System.currentTimeMillis()
        map["device_platform"] = "android"
        //
        map["version_code"] = "101"
        map["push_token"] = ""
        map["channel"] = "umeng"
        map["device_id"] = "test"
        map["network"] = "test"
        map["device_brand"] = "test"
        map["os_version"] = "test"

        if ("POST" == method) {
            val oldBody = oldRequest.body()
            when (oldBody) {
                is FormBody -> {

                    newRequestBuild = oldRequest.newBuilder()
                    val newMap = HashMap<String, Any?>()
                    for (i in 0 until oldBody.size()) {
                        newMap[oldBody.name(i)] = oldBody.value(i)
                    }
                    for (obj in map) {
                        newMap[obj.key] = "${obj.value}"
                    }
                    //生成签名后的参数
                    postBodyString = SignUtils.createSign(newMap, key)
                    newRequestBuild!!.post(
                        RequestBody.create(
                            MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"),
                            postBodyString
                        )
                    )
                }
            }
        } else {
            // 添加新的参数
            val commonParamsUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
            for (obj in map) {
                commonParamsUrlBuilder.addQueryParameter(obj.key, "${obj.value}")
            }

            newRequestBuild = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(commonParamsUrlBuilder.build())
        }
        val newRequest = newRequestBuild!!
            .addHeader("Accept", "application/json")
            .addHeader("Accept-Language", "zh")
            .addHeader("Content-Type", "application/json")
            .build()

        val response = chain.proceed(newRequest)
        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
        return response
            .newBuilder()
            .body(okhttp3.ResponseBody.create(mediaType, content))
            .header("application/json", "Content-Type")
            .addHeader("Content-Type", "application/json")
            .addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept")
            .addHeader("Access-Control-Max-Age", "3600")
            .addHeader("Access-Control-Allow-Origin", "*")
            .addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
            .build()
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (request != null)
                request.writeTo(buffer)
            else
                return ""
            return buffer.readUtf8()
        } catch (e: IOException) {
            return "did not work"
        }

    }

}