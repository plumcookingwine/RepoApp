package com.plumcookingwine.repo.view.user.avatar

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.plumcookingwine.repo.R
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.AlbumLoader

/**
 * @author kangf
 * @data 2019/9/10
 * @description class MediaLoad
 */
class MediaLoad :AlbumLoader {
    override fun load(imageView: ImageView?, albumFile: AlbumFile?) {
        load(imageView, albumFile?.path)
    }

    override fun load(imageView: ImageView?, url: String?) {
        if(imageView == null || url == null) {
            return
        }
        Glide.with(imageView)
            .load(url)
            .into(imageView)
    }
}