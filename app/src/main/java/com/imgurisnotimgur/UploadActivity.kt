package com.imgurisnotimgur

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.content.res.ResourcesCompat
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.gallery_preferences.*
import kotlinx.android.synthetic.main.navigation_bar.*

class UploadActivity : AppCompatActivity() {
    private val GALLERY_RESULT = 0
    private val CAMERA_RESULT = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        uploadAct.isEnabled = false
        subredditAct.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.fontawesome))
        uploadAct.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.fontawesome))
        searchAct.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.fontawesome))
        profileAct.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.fontawesome))
        galleryAct.setTypeface(ResourcesCompat.getFont(this.applicationContext, R.font.fontawesome))
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
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_RESULT)
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
