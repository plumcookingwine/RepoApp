package com.plumcookingwine.repo.data

/**
 * @author kangf
 * @data 2019/9/10
 * @description class UploadDao
 */
data class UploadDao(
    var id: Int,
    var path: String,
    var showProgress: Boolean,
    var totalSize: Long,
    var uploadSize: Long,
    var isError: Boolean = false
)