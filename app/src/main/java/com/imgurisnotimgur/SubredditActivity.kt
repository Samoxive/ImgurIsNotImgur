package com.imgurisnotimgur

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.entities.SubredditImage

import kotlinx.android.synthetic.main.activity_subreddit.*
import kotlinx.android.synthetic.main.gallery_preferences.*
import kotlinx.android.synthetic.main.navigation_bar.*

class SubredditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subreddit)

        subredditAct.isEnabled = false
        galleryAct.setOnClickListener(NavBarButtonHandler(this, GalleryActivity::class.java))
        uploadAct.setOnClickListener(NavBarButtonHandler(this, UploadActivity::class.java))
        searchAct.setOnClickListener(NavBarButtonHandler(this, SearchActivity::class.java))
        profileAct.setOnClickListener(NavBarButtonHandler(this, ProfileActivity::class.java))

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val subredditPreference = preferences.getString("subreddit", "CatsStandingUp")

        subredditInput.setText(subredditPreference, TextView.BufferType.EDITABLE)

        val subredditGalleryAdapter = SubredditGalleryAdapter(arrayOf(), arrayOf(), this)
        rv_gallery.adapter = subredditGalleryAdapter

        button.setOnClickListener {
            AsyncAction({
                ImgurApi.getSubredditGallery(subredditInput.text.toString())
            }, { images ->
                subredditGalleryAdapter.items = images
            })
        }

        rv_gallery.requestFocus()

        AsyncAction({ ImgurApi.getSubredditGallery(subredditPreference) },
                { images -> subredditGalleryAdapter.items = images })
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
