package com.imgurisnotimgur

import android.app.Activity
import android.app.AlertDialog
import android.app.Notification
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import com.imgurisnotimgur.api.ImgurApi
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.navigation_bar.*
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.*


class UploadActivity : AppCompatActivity() {
    private val GALLERY_RESULT = 0
    private val CAMERA_RESULT = 1

    fun getTempFileUri() = Uri.parse("${getExternalFilesDir(Environment.DIRECTORY_PICTURES).absolutePath}/temp.jpg")

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val stream = FileOutputStream(getTempFileUri().toString())
        val writer = OutputStreamWriter(stream)
        writer.write("")
        writer.flush()
        writer.close()
        stream.close()
        return File(getTempFileUri().toString())
    }

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
            try {
                val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                val photoFile = createImageFile()
                val photoURI = FileProvider.getUriForFile(this,
                        "com.imgurisnotimgur.fileprovider",
                        photoFile)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(cameraIntent, CAMERA_RESULT)
            } catch (e: Exception) {
                Log.wtf("Upload", e)
            }
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
            AsyncAction({
                val file = if (requestCode == GALLERY_RESULT) {
                    contentResolver.openInputStream(data?.data).readBytes()
                } else {
                    try {
                        val stream = FileInputStream(File(getTempFileUri().toString()))
                        val buffer = arrayListOf<Byte>()

                        var b: Int = stream.read()
                        while (b != -1) {
                            buffer.add(b.toByte())
                            b = stream.read()
                        }

                        buffer.toByteArray()
                    } catch (e: Exception) {
                        null
                    }
                }

                if (file == null) {
                    throw Exception("Something something")
                }

                val accessToken = SecretUtils.getSecrets(this).second.accessToken
                return@AsyncAction ImgurApi.uploadImage(file, accessToken)
            }, { imageUrl ->
                com.imgurisnotimgur.Notification.uploadedNotification(this,imageUrl)
            })
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
