package com.plumcookingwine.repo.service.interceptor

import java.net.URLEncoder
import java.util.*
import kotlin.collections.HashMap

/**
*@author : HaoBoy
*@date : 2019/1/29
*@description :签名工具类
**/
object SignUtils {

    /**
     * 生成签名，默认UTF-8
     *
     * @param parameters
     * @param key
     * @return
     */
    fun createSign(parameters: HashMap<String, Any?>, key: String?): String {
        val sb = StringBuffer()
        var sbkey = StringBuffer()
        val sortedMap = TreeMap<String, Any?>()
        sortedMap.putAll(parameters)
        val es = sortedMap.entries  //所有参与传参的参数按照accsii排序（升序）
//        val es = parameters.entries  //所有参与传参的参数按照accsii排序（升序）
        val it = es.iterator()
        while (it.hasNext()) {
            val entry = it.next() as java.util.Map.Entry<*, *>
            val k = entry.key as String
            val v = entry.value
            //空值不传递，不参与签名组串
            if (null != v && "" != v) {
                sb.append("$k=$v&")
                sbkey.append("$k=$v&")
            }
        }
        //System.out.println("字符串:"+sb.toString());
        sbkey = sbkey.append("key=$key")
        val encode = URLEncoder.encode(sbkey.toString(), "UTF-8")//utf-8转码
//        println("字符串:" + sbkey.toString())
        //MD5加密,结果转换为小写字符
        val sign = MD5Util.md5Encrypt32Lower(encode.toString())
//        println("MD5加密值:$sign")
        return sb.toString() + "sign=" + sign
    }

    /**
     * 获得生成签名，默认UTF-8
     *
     * @param parameters
     * @param key
     * @return
     */
    fun getSign(parameters: HashMap<String, Any?>, key: String?): String {
        val sb = StringBuffer()
        var sbkey = StringBuffer()

        val sortedMap = TreeMap<String, Any?>()
        sortedMap.putAll(parameters)
        val es = sortedMap.entries  //所有参与传参的参数按照accsii排序（升序）
        val it = es.iterator()

        while (it.hasNext()) {
            val entry = it.next() as java.util.Map.Entry<*, *>
            val k = entry.key as String
            val v = entry.value
            //空值不传递，不参与签名组串
            if (null != v && "" != v) {
                sb.append("$k=$v&")
                sbkey.append("$k=$v&")
            }
        }
        sbkey = sbkey.append("key=$key")
        val encode = URLEncoder.encode(sbkey.toString(), "UTF-8").replace("\\+".toRegex(), "%20")//utf-8转码
        //MD5加密,结果转换为大写字符
        val sign = MD5Util.md5Encrypt32Lower(encode.toString())
        return sign
    }
}
