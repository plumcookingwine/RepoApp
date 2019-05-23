package com.plumcookingwine.repo.entity


/**
 * {"code":"2001","msg":"权限不足","sub_code":"无效token"}
 */
data class MainModel(
    var code: String? = null,
    var msg: String? = null,
    var sub_code: String? = null
)