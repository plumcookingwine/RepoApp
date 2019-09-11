package com.plumcookingwine.repo.view.user.avatar

import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import com.plumcookingwine.base.view.BaseActivity
import com.plumcookingwine.repo.R
import com.plumcookingwine.repo.data.UploadDao
import com.yanzhenjie.album.Album
import kotlinx.android.synthetic.main.activity_upload_image.*
import java.io.File

class UploadImageActivity : BaseActivity<UploadImagePresenter>(), UploadImageView {


    private val mUploads = mutableListOf<UploadDao>()

    private var mAdapter: ImageAdapter? = null

    override fun resLayoutId(): Int {
        return R.layout.activity_upload_image
    }

    override fun init() {
        mAdapter = ImageAdapter(mUploads)
        val manager = GridLayoutManager(this, 4)
        mRvImg.layoutManager = manager
        mRvImg.adapter = mAdapter
    }

    override fun logic() {
        mIvSingle.setOnClickListener {
            val album = Album.image(this)
            if (mRbSingle.isChecked) {
                album.singleChoice()
            } else {
                album.multipleChoice()
            }.camera(true)
                .onResult {
                    if (it.size <= 0) {
                        Toast.makeText(this@UploadImageActivity, "图片选择失败", Toast.LENGTH_SHORT)
                            .show()
                        return@onResult
                    }
                    if (mRbSingle.isChecked) {
                        mUploads.clear()
                    }
                    it.map {
                        mUploads.add(UploadDao(0, it.path, false, 0, 0))
                    }
                    mAdapter?.notifyDataSetChanged()
                }
                .onCancel {
                    Toast.makeText(this@UploadImageActivity, "cancel", Toast.LENGTH_SHORT).show()
                }
                .start()
        }

        mAdapter?.setOnItemClick({

        }, {

        })


        mBtnUpload.setOnClickListener {
            if (mUploads.isEmpty()) {
                Toast.makeText(this@UploadImageActivity, "请选择图片", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mRbSingle.isChecked) {
                val file = File(mUploads[0].path)
                if (!file.exists()) {
                    Toast.makeText(this@UploadImageActivity, "图片不存在", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                mPresenter.upload(file)
            } else {

                val files = mutableListOf<File>()
                mUploads.map {
                    files.add(File(it.path))
                }
                mPresenter.uploadList(files)
                Toast.makeText(this@UploadImageActivity, "多图上传", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun initPresenter(): UploadImagePresenter {
        return UploadImagePresenter(this)
    }

    @Synchronized
    override fun uploadProgress(currP: Long, total: Long, ind: Int) {
        mUploads[ind].totalSize = total
        mUploads[ind].uploadSize = currP
        mUploads[ind].showProgress = true
        mAdapter?.notifyDataSetChanged()
    }

    override fun uploadSuccess(ind: Int) {
        mUploads[ind].showProgress = false
        mAdapter?.notifyDataSetChanged()
        Toast.makeText(this, "上传成功", Toast.LENGTH_LONG).show()
    }

    override fun uploadError(ind: Int) {
        mUploads[ind].isError = true
        mAdapter?.notifyDataSetChanged()
        Toast.makeText(this, "上传失败===$ind", Toast.LENGTH_LONG).show()
    }

}
