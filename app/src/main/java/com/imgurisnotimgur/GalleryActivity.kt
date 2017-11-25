package com.imgurisnotimgur

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.imgurisnotimgur.api.ImgurApi
import com.imgurisnotimgur.entities.Image
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.gallery_preferences.*
import kotlinx.android.synthetic.main.navigation_bar.*

class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        galleryAct.isEnabled = false
        subredditAct.setOnClickListener(NavBarButtonHandler(this, SubredditActivity::class.java))
        uploadAct.setOnClickListener(NavBarButtonHandler(this, UploadActivity::class.java))
        searchAct.setOnClickListener(NavBarButtonHandler(this, SearchActivity::class.java))
        profileAct.setOnClickListener(NavBarButtonHandler(this, ProfileActivity::class.java))

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val sortPreference = preferences.getString("sort", "Hot")
        val sectionPreference = preferences.getString("section", "Viral")
        val nsfwEnabled = preferences.getBoolean("nsfw_enabled", false)

        val sectionEntries = resources.getStringArray(R.array.section)
        val sortEntries = resources.getStringArray(R.array.sort)

        val sectionIndex = PreferenceUtils.findIndexOfValue(sectionEntries, sectionPreference)
        val sortIndex = PreferenceUtils.findIndexOfValue(sortEntries, sortPreference)

        sectionSpinner.setSelection(sectionIndex, true)
        sortSpinner.setSelection(sortIndex, true)

        val galleryAdapter = GalleryAdapter(arrayOf(), arrayOf(), this)
        rv_gallery.adapter = galleryAdapter
        sectionSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // We shouldn't need to do anything here as user can't select an empty entry anyways
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val sortPosition = sortSpinner.selectedItemPosition

                AsyncAction({ ImgurApi.getGallery(position, sortPosition, nsfwEnabled) },
                        { images -> galleryAdapter.items = images })
            }
        }
            sortSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // We shouldn't need to do anything here as user can't select an empty entry anyways
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val sectionPosition = sectionSpinner.selectedItemPosition

                    AsyncAction({ ImgurApi.getGallery(sectionPosition, position, nsfwEnabled) },
                            { images -> galleryAdapter.items = images })
                }
            }

            AsyncAction({ ImgurApi.getGallery(sectionIndex, sortIndex, nsfwEnabled) },
                    { images -> galleryAdapter.items = images })
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
