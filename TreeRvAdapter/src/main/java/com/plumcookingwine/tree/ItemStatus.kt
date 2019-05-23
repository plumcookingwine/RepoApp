package com.plumcookingwine.tree

/**
 * Created by Kevin on 2019/1/22.
 *
 *
 * 用来存储item的状态， 并计算真实索引值
 */
class ItemStatus {

    // 当前item类型
    var viewType: Int = 0
    // 组索引
    var groupItemIndex: Int = 0
    // 子索引
    var subItemIndex: Int = 0

    companion object {
        // 一级item
        val VIEW_TYPE_GROUP_ITEM = 0x00000000
        // 二级item
        val VIEW_TYPE_SUB_ITEM = 0x00000001
    }
}
