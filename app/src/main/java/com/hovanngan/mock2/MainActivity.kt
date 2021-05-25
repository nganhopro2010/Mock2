package com.hovanngan.mock2

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.hovanngan.mock2.databinding.ActivityMainBinding
import com.hovanngan.mock2.fullscreen.FullScreenActivity
import com.hovanngan.mock2.model.Image
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var adapter: ImagesAdapter
    private lateinit var job: Job

    companion object {
        const val RC_PERMISSION: Int = 100
        const val EXTRA_IMAGES: String = "BUNDLE_IMAGES"
        const val EXTRA_POSITION: String = "EXTRA_POSITION"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        adapter = ImagesAdapter(false) { position, data ->
            startActivity(Intent(this, FullScreenActivity::class.java).apply {
                val bundle = Bundle()
                bundle.putParcelableArrayList(EXTRA_IMAGES, data)
                putExtras(bundle)
                putExtra(EXTRA_POSITION, position)
            })
        }
        mBinding.rvImages.adapter = adapter
        checkPermission()

    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                RC_PERMISSION
            )
        } else {
            launch {
                mBinding.cProgress.visibility = View.VISIBLE
                adapter.setData(getAllImage())
                mBinding.cProgress.visibility = View.GONE

            }
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_PERMISSION &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            launch {
                mBinding.cProgress.visibility = View.VISIBLE
                adapter.setData(getAllImage())
                mBinding.cProgress.visibility = View.GONE
            }

        } else {
            Toast.makeText(
                this,
                getString(R.string.you_have_not_enough_grant_permission),
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:" + this.packageName)
            startActivityForResult(intent, RC_PERMISSION)
        }
    }

    private suspend fun getAllImage(): ArrayList<Image> {
        return withContext(Dispatchers.IO) {
            val galleryImageUrls = ArrayList<Image>()
            val columns = arrayOf(MediaStore.Images.Media._ID)
            val orderBy = MediaStore.Images.Media.DATE_ADDED

            contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                null, null, "$orderBy DESC"
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val imageUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                    )
                    val image = Image(imageUri.toString())
                    galleryImageUrls.add(image)
                }
            }
            return@withContext galleryImageUrls
        }
    }

}
