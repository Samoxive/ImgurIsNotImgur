package com.imgurisnotimgur

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.imgurisnotimgur.api.ImgurApi
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.navigation_bar.*
import java.io.ByteArrayOutputStream

class UploadActivity : AppCompatActivity() {
    private val GALLERY_RESULT = 0
    private val CAMERA_RESULT = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        val selectPictureString = getString(R.string.select_picture)

        uploadAct.isEnabled = false
        subredditAct.setOnClickListener(NavBarButtonHandler(this, SubredditActivity::class.java))
        galleryAct.setOnClickListener(NavBarButtonHandler(this, GalleryActivity::class.java))
        searchAct.setOnClickListener(NavBarButtonHandler(this, SearchActivity::class.java))
        profileAct.setOnClickListener(NavBarButtonHandler(this, ProfileActivity::class.java))

        cameraUpload.setOnClickListener {
            val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_RESULT)
        }

        pickUpload.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, selectPictureString), GALLERY_RESULT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            AsyncAction<Unit, String>({
                val uri = data?.data
                val accessToken = SecretUtils.getSecrets(this).second.accessToken
                val file = if (requestCode == GALLERY_RESULT) {
                    contentResolver.openInputStream(uri).readBytes()
                } else {
                    // Currently we upload the thumbnail camera sends, replace this to upload the actual file
                    val bitmap = data!!.extras.get("data") as Bitmap
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    stream.toByteArray()
                }
                return@AsyncAction ImgurApi.uploadImage(file, accessToken)
            }, { imageUrl ->
                val imageUploadedString = getString(R.string.image_uploaded)
                val imageUploadSuccessString = getString(R.string.image_uploaded_success)
                val okString = getString(R.string.ok)
                val openString = getString(R.string.open)
                val dialog = AlertDialog.Builder(this)
                        .setMessage("$imageUploadedString $imageUrl")
                        .setTitle(imageUploadSuccessString)
                        .setPositiveButton(okString, { _, _ ->  })
                        .setNeutralButton(openString, { _, _ ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl))
                            startActivity(intent)
                        })
                        .setCancelable(true)
                        .create()
                dialog.show()
            }).exec()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.settingsButton -> {
                val intent = Intent(this, PreferencesActivity::class.java)
                startActivity(intent)
            }
            R.id.quitButton -> System.exit(0)
        }

        return super.onOptionsItemSelected(item)
    }
}
