package com.plumcookingwine.tree.dao

import java.io.Serializable

/**
 * Created by Kevin on 2019/1/22.
 */
class TreeListDao<K, V>(
        // 组数据
        var groupDao: K,
        // 子数据
        var subList: List<V>) : Cloneable, Serializable {

    /**
     * 配置项
     */
    // 是否可展开收起
    var isEnableExpand = true
    // 默认是否展开状态
    var isExpand = false
    // 收起状态最少展示子item数量
    var minCount = 6

    @Throws(CloneNotSupportedException::class)
    override fun clone(): Any {
        return super.clone()
    }
}
